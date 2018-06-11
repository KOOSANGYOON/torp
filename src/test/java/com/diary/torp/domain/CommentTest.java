package com.diary.torp.domain;

import com.diary.torp.web.HomeController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class CommentTest {
    private static final Logger log = LoggerFactory.getLogger(CommentTest.class);

    User testUser = new User("testUser", "password", "testName");
    ToDoCard testCard = new ToDoCard(testUser, "testCard");
    Comment testComment = new Comment(testUser, "testComment");

    @Test
    public void registerIntoCard() {
        assertEquals(testComment.getToDoCard(), null);
        testComment.registerIntoCard(testCard);
        assertNotNull(testComment.getToDoCard());
        assertEquals(testComment.getToDoCard().getTitle(), "testCard");
    }
}