package integration;

import com.github.sddisk.usernotesbackend.UserNotesBackendApplication;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @ActiveProfiles("test")
@ContextConfiguration(classes = UserNotesBackendApplication.class)
@AutoConfigureMockMvc @AutoConfigureJdbc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERNAME = "username";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "qwertyuiopasdfghjklzxcvbnm1234567890@#$%^&+=!";

    private static final RegisterRequestDto REGISTER_REQUEST
            = new RegisterRequestDto(USERNAME, EMAIL, PASSWORD);



    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(
                "truncate table user_table, note_table cascade"
        );
    }

    // register
    @Test
    void register_shouldRegisterUser() throws Exception {
        var requestBuilder = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(REGISTER_REQUEST));

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(1),
                        jsonPath("$.accessToken").exists(),
                        jsonPath("$.accessToken").isNotEmpty(),
                        cookie().exists("refreshToken"),
                        cookie().httpOnly("refreshToken", true)
                );

        boolean isUserExists = userRepository.existsUserByEmail(EMAIL);
        assertThat(isUserExists).isEqualTo(true);

        Optional<User> savedUser = userRepository.findByEmail(EMAIL);
        assertThat(savedUser).isPresent();

        savedUser.ifPresent( user -> {
            assertThat(user.getEmail()).isEqualTo(EMAIL);
            assertThat(user.getUsername()).isEqualTo(USERNAME);
        });
    }

}
