package com.diary.torp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoDeckRepository extends JpaRepository<ToDoDeck, Long> {
    Iterable<ToDoDeck> findByDeleted(boolean deleted);
    Iterable<ToDoDeck> findByToDoBoardAndDeleted(ToDoBoard toDoBoard, boolean deleted);
    ToDoDeck findByTitle(String title);
}