package com.diary.torp.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUserId(String userId);
	Iterable<User> findByDeleted(boolean deleted);
}
