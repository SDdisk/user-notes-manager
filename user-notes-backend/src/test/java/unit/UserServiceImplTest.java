package unit;

import com.github.sddisk.usernotesbackend.exception.UserAlreadyExistException;
import com.github.sddisk.usernotesbackend.exception.UserNotFoundException;
import com.github.sddisk.usernotesbackend.service.user.UserServiceImpl;
import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    // get all
    /*
        empty list
        filled list
     */
    @Test
    void getAll_whenZeroUsersFound_shouldReturnEmptyList() {
        // AAA (Arrange, Act, Assert)
        List<User> emptyList = Collections.emptyList();


        when(userRepository.findAll()).thenReturn(emptyList);
        List<User> result = userService.getAll();

        assertNotNull(
                result
        );
        assertArrayEquals(
                result.toArray(),
                emptyList.toArray()
        );

        verify(userRepository).findAll();
    }

    @Test
    void getAll_whenUsersFound_shouldReturnListWithUsers() {
        Set<String> userRole = new HashSet<>();
        userRole.add("USER");

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        User u1 = new User(id1, "u1", "u1@m.c", "encoded", userRole, Collections.emptySet());
        User u2 = new User(id2, "u2", "u2@m.c", "encoded", userRole, Collections.emptySet());
        User u3 = new User(id3, "u3", "u3@m.c", "encoded", userRole, Collections.emptySet());

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);


        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.getAll();

        assertNotNull(
                result
        );
        assertEquals(
                result.size(),
                userList.size()
        );
        assertArrayEquals(
                result.toArray(),
                userList.toArray()
        );

        verify(userRepository).findAll();
    }

    // get by id
    /*
        user exists
        user not exists (not found cuz bad id request)
     */
    @Test
    void getById_whenUserFound_shouldReturnUser() {
        Set<String> userRole = new HashSet<>();
        userRole.add("USER");

        UUID id = UUID.randomUUID();
        User u1 = new User(id, "u1", "u1@m.c", "encoded", userRole, Collections.emptySet());

        when(userRepository.findById(id)).thenReturn(Optional.of(u1));
        User result = userService.getById(id);

        assertNotNull(
                result
        );
        assertEquals(
                u1,
                result
        );

        verify(userRepository).findById(any());
    }

    @Test
    void getById_whenUserNotFound_shouldThrowException() {
        UUID badId = UUID.randomUUID();

        when(userRepository.findById(badId)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getById(badId)
        );

        verify(userRepository).findById(badId);
    }

    // get by email
    /*
        user exists
        user not exists (not found cuz bad email request)
     */
    @Test
    void getByEmail_whenUserFound_shouldReturnUser() {
        Set<String> userRole = new HashSet<>();
        userRole.add("USER");

        UUID id = UUID.randomUUID();
        String email = "u1@m.c";
        User u1 = new User(id, "u1", email, "encoded", userRole, Collections.emptySet());

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(u1));
        User result = userService.getByEmail(email);

        assertNotNull(
                result
        );
        assertEquals(
                u1,
                result
        );

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void getByEmail_whenUserNotFound_shouldThrowException() {
        String email = "e@m.c";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getByEmail(email)
        );

        verify(userRepository).findByEmail(anyString());
    }


    // save user
    /*
        user saved (with hash password & roles USER)
        user not saved (already exists)
     */
    @Test
    void save_whenUserNotExists_shouldCreateNewUserAndSave() {
        User newUser = new User(null, "newUser", "e@m.c", "password", null, null);

        Set<String> userRole = new HashSet<>();
        userRole.add("USER");

        UUID id = UUID.randomUUID();
        User savedUser = new User(id, "newUser", "e@m.c", "encoded", userRole, Collections.emptySet());

        when(userRepository.existsUserByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(newUser);

        assertNotNull(
                result
        );
        assertEquals(
                savedUser,
                result
        );
        assertEquals(
                "encoded",
                result.getPassword()
        );
        assertArrayEquals(
                userRole.toArray(),
                result.getRoles().toArray()
        );
        assertEquals(
                Collections.emptySet(),
                result.getNotes()
        );

        verify(userRepository).existsUserByEmail(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void save_whenUserExists_shouldThrowException() {
        User newUser = new User(null, "newUser", "e@m.c", "password", null, null);

        when(userRepository.existsUserByEmail(anyString())).thenReturn(true);

        assertThrows(
                UserAlreadyExistException.class,
                () -> userService.save(newUser)
        );

        verify(userRepository).existsUserByEmail(anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // delete user
    /*
        user deleted successfully
     */
    @Test
    void deleteById_withValidId_shouldCallRepository() {
        UUID id = UUID.randomUUID();

        userService.deleteById(id);

        verify(userRepository).deleteById(id);
        verify(userRepository).deleteById(eq(id));
    }
}