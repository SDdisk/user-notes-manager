package com.github.sddisk.usernotesbackend.store.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;


@Entity
@Table(name = "user_table")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(of = "email")
@ToString(exclude = "password")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // cred
    private String username;
    @NaturalId
    private String email;
    private String password;
}