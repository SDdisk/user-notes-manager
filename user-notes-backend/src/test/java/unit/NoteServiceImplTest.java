package unit;

import com.github.sddisk.usernotesbackend.service.note.NoteService;
import com.github.sddisk.usernotesbackend.service.note.NoteServiceImpl;
import com.github.sddisk.usernotesbackend.service.user.UserServiceImpl;
import com.github.sddisk.usernotesbackend.service.user.current.CurrentUserService;
import com.github.sddisk.usernotesbackend.service.user.current.CurrentUserServiceImpl;
import com.github.sddisk.usernotesbackend.store.entity.Note;
import com.github.sddisk.usernotesbackend.store.entity.User;
import com.github.sddisk.usernotesbackend.store.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
    Note createUserNote(Note note);

    List<Note> getUserNotes();
    Note getUserNoteById(UUID noteId);

    Note updateUserNote(UUID noteId, Note newNote);
    void deleteUserNoteById(UUID noteId);
 */
@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @Mock
    private CurrentUserServiceImpl currentUserService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Captor
    private ArgumentCaptor<Note> noteCaptor;

    private UUID currentUserId;
    private User currentUser;
    private UUID noteId;
    private Note existingNote;
    private Set<Note> userNotes;

    @BeforeEach
    void setUp() {
        currentUserId = UUID.randomUUID();
        currentUser = createUser(currentUserId, "u@m.c");
        noteId = UUID.randomUUID();

        userNotes = new HashSet<>();

        existingNote = createNote(noteId, "Old title", "Old Content", false);
        existingNote.setUser(currentUser);
        userNotes.add(existingNote);

        currentUser.setNotes(userNotes);
    }

    // createUserNote
    @Test
    void createUserNote_shouldSaveNoteWithCurrentUser() {
        Note newNote = Note.builder()
                .title("New note")
                .content("Content")
                .isPinned(false)
                .build();

        Note savedNote = Note.builder()
                .id(noteId)
                .title("New note")
                .content("Content")
                .isPinned(false)
                .user(currentUser)
                .build();

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);
        Note result = noteService.createUserNote(newNote);

        assertNotNull(
                result
        );
        assertEquals(
                savedNote,
                result
        );
        assertEquals(
                savedNote.getId(),
                result.getId()
        );
        assertEquals(
                savedNote.getUser(),
                result.getUser()
        );

        verify(currentUserService).getCurrentUserId();
        verify(userService).getById(currentUserId);
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void createUserNote_shouldAddNoteToUserCollection() {
        Note newNote = Note.builder()
                .title("Test note")
                .content("Test content")
                .build();

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        int initialSize = currentUser.getNotes().size();

        noteService.createUserNote(newNote);

        verify(noteRepository).save(noteCaptor.capture());
        Note capturedNote = noteCaptor.getValue();

        assertThat(capturedNote.getUser()).isEqualTo(currentUser);
    }

    // get user notes
    @Test
    void getUserNotes_shouldReturnAllUserNotesAsList() {
        Note note2 = createNote(UUID.randomUUID(), "Note 2", "Content 2", true);
        Note note3 = createNote(UUID.randomUUID(), "Note 3", "Content 3", false);

        userNotes.add(note2);
        userNotes.add(note3);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        List<Note> result = noteService.getUserNotes();

        assertThat(result)
                .hasSize(3)
                .extracting(Note::getTitle)
                .containsExactlyInAnyOrder("Old title", "Note 2", "Note 3");

        assertThat(result).isInstanceOf(List.class);
    }

    @Test
    void getUserNotes_whenUserHasNoNotes_shouldReturnEmptyList() {
        currentUser.setNotes(new HashSet<>());

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        List<Note> result = noteService.getUserNotes();

        assertThat(result).isEmpty();
        assertThat(result).isInstanceOf(List.class);
    }

    // get user note by id
    @Test
    void getUserNoteById_withExistingNote_shouldReturnNoteFromSet() {
        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        Note result = noteService.getUserNoteById(noteId);

        assertThat(result).isEqualTo(existingNote);
        assertThat(result.getUser()).isEqualTo(currentUser);
    }

    @Test
    void getUserNoteById_shouldWorkWithSetCollections() {
        Set<Note> notes = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Note note = createNote(UUID.randomUUID(), "Note " + i, "Content " + i, false);
            notes.add(note);
        }

        notes.add(existingNote);
        currentUser.setNotes(notes);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);
        Note result = noteService.getUserNoteById(noteId);

        assertThat(result).isEqualTo(existingNote);
        assertThat(result.getUser()).isEqualTo(currentUser);
    }

    // update user note
    @Test
    void updateUserNote_shouldUpdateNoteKeepingUserRelationship() {
        Note updatedNote = Note.builder()
                .title("Updated Title")
                .content("Updated Content")
                .isPinned(true)
                .build();

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation ->
                invocation.<Note>getArgument(0)
        );

        Note result = noteService.updateUserNote(noteId, updatedNote);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getContent()).isEqualTo("Updated Content");
        assertThat(result.isPinned()).isTrue();

        assertThat(result.getUser()).isEqualTo(currentUser);
        assertThat(result.getUser().getEmail()).isEqualTo("u@m.c");

        assertThat(result.getId()).isEqualTo(noteId);
    }

    // delete user note by id
    @Test
    void deleteUserNoteById_shouldRemoveNoteFromUserSet() {
        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        assertThat(currentUser.getNotes()).hasSize(1);
        assertThat(currentUser.getNotes()).contains(existingNote);

        noteService.deleteUserNoteById(noteId);

        verify(noteRepository).deleteById(noteId);

        assertThat(currentUser.getNotes())
                .isEmpty();

        assertThat(currentUser.getNotes())
                .doesNotContain(existingNote);
    }

    @Test
    void deleteUserNoteById_shouldWorkWithSetWhenRemoving() {
        Set<Note> notes = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            Note note = createNote(UUID.randomUUID(), "Note " + i, "Content " + i, false);
            notes.add(note);
        }
        notes.add(existingNote);
        currentUser.setNotes(notes);

        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        assertThat(currentUser.getNotes()).hasSize(6);

        noteService.deleteUserNoteById(noteId);

        assertThat(currentUser.getNotes())
                .hasSize(5)
                .doesNotContain(existingNote);
    }

    @Test
    void deleteUserNoteById_transactional_shouldRemoveFromSet() {
        when(currentUserService.getCurrentUserId()).thenReturn(currentUserId);
        when(userService.getById(currentUserId)).thenReturn(currentUser);

        noteService.deleteUserNoteById(noteId);

        assertThat(currentUser.getNotes()).isEmpty();
    }


    private User createUser(UUID id, String email) {
        return User.builder()
                .id(id)
                .email(email)
                .username("username")
                .password("password")
                .roles(new HashSet<>(Set.of("USER")))
                .notes(new HashSet<>())
                .build();
    }

    private Note createNote(UUID id, String title, String content, boolean pinned) {
        return Note.builder()
                .id(id)
                .title(title)
                .content(content)
                .isPinned(pinned)
                .user(currentUser)
                .build();
    }
}
