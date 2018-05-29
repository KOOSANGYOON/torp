package com.diary.torp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoCardRepository extends JpaRepository<ToDoCard, Long> {
}
