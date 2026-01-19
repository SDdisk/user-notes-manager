package controller;

import com.github.sddisk.usernotesbackend.UserNotesBackendApplication;
import com.github.sddisk.usernotesbackend.api.controller.AuthController;
import com.github.sddisk.usernotesbackend.api.dto.auth.AuthResponse;
import com.github.sddisk.usernotesbackend.api.dto.auth.LoginRequestDto;
import com.github.sddisk.usernotesbackend.api.dto.auth.RegisterRequestDto;
import com.github.sddisk.usernotesbackend.security.provider.JwtTokenProvider;
import com.github.sddisk.usernotesbackend.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = UserNotesBackendApplication.class)
public class AuthControllerTest {
    private static final String TEST_EMAIL = "test@email.com";
    private static final String TEST_USERNAME = "Test Username";
    private static final String TEST_PASSWORD = "pas12345!";
    private static final String ACCESS_TOKEN = "Test-Access-Token";

    private final AuthResponse authResponse = new AuthResponse(ACCESS_TOKEN);

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private UserDetailsService userDetailsService;

    // register
    @Test
    void register_shouldResponseWithAccessTokenAndOkStatusCode_whenRegisterRequestIsValid() throws Exception {
        RegisterRequestDto registerRequest = new RegisterRequestDto(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_PASSWORD
        );

        var requestBuilder = post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));

        when(authService.register(any(RegisterRequestDto.class), any(HttpServletResponse.class)))
                .thenReturn(authResponse);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(1),
                        jsonPath("$.accessToken").value(ACCESS_TOKEN)
                );

        verify(authService).register(any(RegisterRequestDto.class), any(HttpServletResponse.class));
    }

    @Nested
    class RegisterValidationTests {
        /*
            @NotBlank(message = "Username cannot be empty")
                String username,

            @NotBlank(message = "Email cannot be empty")
            @Email(message = "Incorrect email format")
                String email,

            @Size(min = 9, message = "Password length must be 9 characters or bigger")
            @Pattern(
                    regexp = "^(?=(.*\\d){5})(?=(.*[a-zA-Z]){3})(?=(.*[@#$%^&+=!]){1})[a-zA-Z\\d@#$%^&+=!]{9}$",
                    message = "Password must contains: 5 digits, 3 letters and 1 special character"
            )
                String password
         */

        // bad username
        /*
            Username cannot be empty
         */
        @Test
        void register_shouldReturnBadRequestStatusCode_whenUsernameIsEmpty() throws Exception {
            RegisterRequestDto registerRequest = new RegisterRequestDto(
                    "",
                    TEST_EMAIL,
                    TEST_PASSWORD
            );

            var requestBuilder = post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.username").value("Username cannot be empty"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).register(any(RegisterRequestDto.class), any(HttpServletResponse.class));
        }

        // email
        /*
            Email cannot be empty
            Incorrect email format
         */
        @Test
        void register_shouldReturnBadRequestStatusCode_whenEmailIsEmpty() throws Exception {
            RegisterRequestDto registerRequest = new RegisterRequestDto(
                    TEST_USERNAME,
                    "",
                    TEST_PASSWORD
            );

            var requestBuilder = post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.email").value("Email cannot be empty"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).register(any(RegisterRequestDto.class), any(HttpServletResponse.class));
        }

        @Test
        void register_shouldReturnBadRequestStatusCode_whenEmailHasIncorrectFormat() throws Exception {
            RegisterRequestDto registerRequest = new RegisterRequestDto(
                    TEST_USERNAME,
                    "incorrect-email",
                    TEST_PASSWORD
            );

            var requestBuilder = post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.email").value("Incorrect email format"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).register(any(RegisterRequestDto.class), any(HttpServletResponse.class));
        }

        // password
        /*
            Password must contains: 5 digits, 3 letters and 1 special character
         */
        @Test
        void register_shouldReturnBadRequestStatusCode_whenPasswordHasIncorrectPattern() throws Exception {
            RegisterRequestDto registerRequest = new RegisterRequestDto(
                    TEST_USERNAME,
                    TEST_EMAIL,
                    ""
            );

            var requestBuilder = post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.password").value("Password must contains: 5 digits, 3 letters and 1 special character"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).register(any(RegisterRequestDto.class), any(HttpServletResponse.class));
        }

    }

    // login
    @Test
    void login_shouldResponseWithAccessTokenAndOkStatusCode_whenLoginRequestIsValid() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto(
                TEST_EMAIL,
                TEST_PASSWORD
        );

        var requestBuilder = post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));

        when(authService.login(any(LoginRequestDto.class), any(HttpServletResponse.class)))
                .thenReturn(authResponse);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.accessToken").value(ACCESS_TOKEN));

        verify(authService).login(any(LoginRequestDto.class), any(HttpServletResponse.class));
    }

    @Nested
    class LoginValidationTests {
        /*
            @NotBlank(message = "Email cannot be empty")
            @Email(message = "Incorrect email format")
                String email,

            @NotBlank(message = "Password cannot be empty")
                String password
         */

        // email
        /*
            Email cannot be empty
            Incorrect email format
         */
        @Test
        void login_shouldReturnBadRequestStatusCode_whenEmailIsEmpty() throws Exception {
            LoginRequestDto loginRequest = new LoginRequestDto(
                    "",
                    TEST_PASSWORD
            );

            var requestBuilder = post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.email").value("Email cannot be empty"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).login(any(LoginRequestDto.class), any(HttpServletResponse.class));
        }

        @Test
        void login_shouldReturnBadRequestStatusCode_whenEmailHasIncorrectFormat() throws Exception {
            LoginRequestDto loginRequest = new LoginRequestDto(
                    "bad-email-format",
                    TEST_PASSWORD
            );

            var requestBuilder = post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.email").value("Incorrect email format"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).login(any(LoginRequestDto.class), any(HttpServletResponse.class));
        }

        // password
        /*
            Password cannot be empty
         */
        @Test
        void login_shouldReturnBadRequestStatusCode_whenPasswordIsEmpty() throws Exception {
            LoginRequestDto loginRequest = new LoginRequestDto(
                    TEST_EMAIL,
                    ""
            );

            var requestBuilder = post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest));

            mockMvc.perform(requestBuilder)
                    .andExpectAll(
                            status().isBadRequest(),
                            jsonPath("$.length()").value(3),
                            jsonPath("$.message").value("Validation failed"),
                            jsonPath("$.details.password").value("Password cannot be empty"),
                            jsonPath("$.timestamp").exists()
                    );

            verify(authService, never()).login(any(LoginRequestDto.class), any(HttpServletResponse.class));
        }
    }

    // logout
    @Test
    void logout_shouldReturnOkStatus() throws Exception {
        var requestBuilder = post("/auth/logout");

        doNothing()
                .when(authService).logout(any(HttpServletResponse.class));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());

        verify(authService).logout(any(HttpServletResponse.class));
    }

    // refresh-token
    @Test
    void refreshToken_shouldResponseWithAccessTokenAndOkStatusCode_whenRefreshTokenExtractedFromCookie() throws Exception {
        String refreshTokenValue = "refresh-token-value";
        var requestBuilders = post("/auth/refresh-token")
                .cookie(new Cookie("refreshToken", refreshTokenValue));

        when(authService.refreshToken(refreshTokenValue)).thenReturn(authResponse);

        mockMvc.perform(requestBuilders)
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.accessToken").value(ACCESS_TOKEN)
                );

        verify(authService).refreshToken(refreshTokenValue);
    }
}