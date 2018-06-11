package com.diary.torp.domain;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.web.HomeController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class ToDoCardTest {
    private static final Logger log = LoggerFactory.getLogger(ToDoCard.class);

    User testUser = new User("testUser", "password", "testName");
    User wrongUser = new User("wrongUser", "password", "wrongName");
    ToDoDeck testDeck = new ToDoDeck(testUser, "testDeck");
    ToDoCard testCard = new ToDoCard(testUser, "testCard");
    Comment testComment = new Comment(testUser, "testComment");

    @Test
    public void registerIntoDeck() {
        assertEquals(testCard.getToDoDeck(), null);
        testCard.registerIntoDeck(testDeck);
        assertEquals(testCard.getToDoDeck(), testDeck);
    }

    @Test
    public void editDescription_rightUser() {
        try {
            testCard.editDescription(testUser, "description.");
            assertEquals(testCard.getDescription(), "description.");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = UnAuthenticationException.class)
    public void editDescription_wrongUser() throws UnAuthenticationException {
        try {
            testCard.editDescription(wrongUser, "description.");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertNotEquals(testCard.getDescription(), "description.");
            assertEquals(testCard.getDescription(), null);
            throw new UnAuthenticationException();
        }
    }

    @Test
    public void addComment_rightUser() {
        try {
            assertEquals(testCard.getComments().size(), 0);
            testCard.addComment(testUser, testComment);
            assertEquals(testCard.getComments().size(), 1);
            assertEquals(testCard.getComments().get(0).getComment(), "testComment");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = UnAuthenticationException.class)
    public void addComment_wrongUser() throws UnAuthenticationException {
        try {
            assertEquals(testCard.getComments().size(), 0);
            testCard.addComment(wrongUser, testComment);
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertEquals(testCard.getComments().size(), 0);
            throw new UnAuthenticationException();
        }
    }

    @Test
    public void editTitle_rightUser() {
        assertEquals(testCard.getTitle(), "testCard");
        try {
            testCard.editTitle(testUser, "newTitle");
            assertEquals(testCard.getTitle(), "newTitle");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = UnAuthenticationException.class)
    public void editTitle_wrongUser() throws UnAuthenticationException {
        assertEquals(testCard.getTitle(), "testCard");
        try {
            testCard.editTitle(wrongUser, "newTitle");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertEquals(testCard.getTitle(), "testCard");
            throw new UnAuthenticationException();
        }
    }

    @Test
    public void isOwner() {
        assertEquals(testCard.isOwner(testUser), true);
        assertEquals(testCard.isOwner(wrongUser), false);
    }
}