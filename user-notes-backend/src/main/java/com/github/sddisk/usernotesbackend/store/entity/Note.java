package com.github.sddisk.usernotesbackend.store.entity;

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

    private boolean isPinned;
}
