package com.github.sddisk.usernotesbackend.store.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "note_table")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Note {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "note_id")
    private UUID id;

    private String title;
    private String content;

    @Column(name = "pinned")
    private boolean isPinned;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private User user;
}
