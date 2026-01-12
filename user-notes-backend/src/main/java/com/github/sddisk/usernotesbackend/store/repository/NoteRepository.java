package com.github.sddisk.usernotesbackend.store.repository;

import com.github.sddisk.usernotesbackend.store.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {

}