package com.diary.torp.web;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import com.diary.torp.service.UserService;
import com.diary.torp.support.AcceptanceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

public class UserAcceptanceTest extends AcceptanceTest {
	private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

	@Resource(name = "userService")
	private UserService userService;

	@Test
	public void loginForm() {
		ResponseEntity<String> response = basicAuthTemplate().getForEntity("/user/loginForm", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));

		ResponseEntity<String> secondResponse = template().getForEntity("/user/loginForm", String.class);
		assertThat(secondResponse.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void login() {
	}

	@Test
	public void fail() {
		ResponseEntity<String> response = basicAuthTemplate().getForEntity("/user/loginFail", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));

		ResponseEntity<String> secondResponse = template().getForEntity("/user/loginFail", String.class);
		assertThat(secondResponse.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void logout() {
		ResponseEntity<String> response = basicAuthTemplate().getForEntity("/user/logout", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void joinForm() {
		ResponseEntity<String> response = template().getForEntity("/user/logout", String.class);
		assertThat(response.getStatusCode(), is(HttpStatus.OK));
		log.debug("body : {}", response.getBody());
	}

	@Test
	public void joinFail() {
	}

	@Test
	public void join() {
	}

	@Test
	public void updateForm() {
	}

	@Test
	public void update() {
	}

	@Test
	public void updateFail() {
	}
}
