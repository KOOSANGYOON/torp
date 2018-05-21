package com.diary.torp.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.UnAuthorizedException;
import com.diary.torp.domain.User;
import com.diary.torp.domain.UserRepository;
import com.diary.torp.web.HomeController;

import javassist.tools.reflect.CannotCreateException;

@Service
public class UserService {
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private UserRepository userRepository;

	public User login(String userId, String password) throws UnAuthenticationException {
		User user = userRepository.findByUserId(userId).orElseThrow(UnAuthorizedException::new);
		log.debug("comming user is " + user.toString());
		if (!user.matchPassword(password)) {
			throw new UnAuthenticationException();
		}
		
		return user;
	}
	
	public User join(String userId, String password, String name) throws CannotCreateException {
		Optional<User> maybeUser = userRepository.findByUserId(userId);
		if (!maybeUser.equals(Optional.empty())) {
			throw new CannotCreateException("이미 존재하는 유저입니다.");
		}
		
		return userRepository.findByUserId(userId).orElse(userRepository.save(new User(userId, password, name)));
	}
	
	@Transactional
	public User update(User user, String password, String name) throws UnAuthenticationException {
		if (!user.matchPassword(password)) {
			throw new UnAuthenticationException();
		}
		user.update(name);
		return userRepository.save(user);
	}
}
