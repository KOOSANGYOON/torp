package com.diary.torp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoBoardRepository extends JpaRepository<ToDoBoard, Long> {
    Iterable<ToDoBoard> findByDeleted(boolean deleted);
    Iterable<ToDoBoard> findByWriter(User writer);
    ToDoBoard findById(Long id);
}
