package com.diary.torp.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

	@Test
	public void matchPasswordTest() {
		User testUser = new User("test", "password", "name");
		assertEquals(testUser.matchPassword("password"), true);
		assertEquals(testUser.matchPassword("wrongpassword"), false);
	}

	@Test
	public void updateTest() {
		User testUser = new User("test", "password", "name");
		testUser.update("newName");
		assertEquals(testUser.getName(), "newName");
	}

}
