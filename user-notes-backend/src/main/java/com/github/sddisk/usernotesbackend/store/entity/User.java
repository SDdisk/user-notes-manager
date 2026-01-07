package com.github.sddisk.usernotesbackend.store.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.util.UUID;


@Entity
@Table(name = "user_table")
@Getter @Setter
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