package com.diary.torp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<User, Long> {
    Iterable<ToDo> findByDeleted(boolean deleted);
}
