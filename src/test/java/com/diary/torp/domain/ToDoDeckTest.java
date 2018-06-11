package com.diary.torp.domain;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.web.HomeController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class ToDoDeckTest {
    private static final Logger log = LoggerFactory.getLogger(ToDoDeckTest.class);

    User testUser = new User("testUser", "password", "testName");
    User wrongUser = new User("wrongUser", "password", "wrongName");
    ToDoBoard testBoard = new ToDoBoard(testUser, "testBoard");
    ToDoDeck testDeck = new ToDoDeck(testUser, "testDeck");
    ToDoCard testCard = new ToDoCard(testUser, "testCard");

    @Test
    public void registerIntoBoard() {
        assertEquals(testDeck.getToDoBoard(), null);
        testDeck.registerIntoBoard(testBoard);
        assertEquals(testDeck.getToDoBoard(), testBoard);
    }

    @Test
    public void addCard() {
        assertEquals(testDeck.getToDoCards().size(), 0);
        testDeck.addCard(testCard);
        assertEquals(testDeck.getToDoCards().size(), 1);
        assertEquals(testDeck.getToDoCards().get(0).getTitle(), "testCard");
    }

    @Test
    public void isOwner() {
        assertEquals(testDeck.isOwner(testUser), true);
        assertEquals(testDeck.isOwner(wrongUser), false);
    }

    @Test
    public void editTitle_rightUser() {
        try {
            testDeck.editTitle(testUser, "newTitle");
            assertEquals(testDeck.getTitle(), "newTitle");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = UnAuthenticationException.class)
    public void editTitle_wrongUser() throws UnAuthenticationException {
        try {
            testDeck.editTitle(wrongUser, "newTitle");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertEquals(testDeck.getTitle(), "testDeck");
            throw new UnAuthenticationException();
        }
    }
}