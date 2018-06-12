package com.diary.torp.domain;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.web.HomeController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class ToDoBoardTest {
    private static final Logger log = LoggerFactory.getLogger(ToDoBoardTest.class);
    User testUser = new User("testUser", "password", "testName");
    User wrongUser = new User("wrongUser", "password", "wrongName");
    ToDoBoard testBoard = new ToDoBoard(testUser, "testBoard");
    ToDoDeck testDeck = new ToDoDeck(testUser, "testDeck");

    @Test
    public void addDeck() {
        assertEquals(testBoard.getToDoDecks().size(), 0);

        testBoard.addDeck(testDeck);

        assertEquals(testBoard.getToDoDecks().size(), 1);
        assertEquals(testBoard.getToDoDecks().get(0).getTitle(), "testDeck");
    }

    @Test
    public void isOwner() {
        assertEquals(testBoard.isOwner(testUser), true);
        assertEquals(testBoard.isOwner(wrongUser), false);
    }

    @Test
    public void editTitle_rightUser() {
        try {
            testBoard.editTitle(testUser, "newTitle");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
        }

        assertEquals(testBoard.getTitle(), "newTitle");
    }

    @Test(expected = UnAuthenticationException.class)
    public void editTitle_wrongUser() throws UnAuthenticationException {
        try {
            testBoard.editTitle(wrongUser, "newTitle");
            log.debug("this message will not show command line.");
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertEquals(testBoard.getTitle(), "testBoard");
            throw new UnAuthenticationException();
        }
    }

    @Test
    public void delete_rightUser() throws UnAuthenticationException {
        //add deck
        ToDoDeck newDeck1 = new ToDoDeck(testUser, "newDeck1");
        ToDoDeck newDeck2 = new ToDoDeck(testUser, "newDeck2");
        newDeck1.registerIntoBoard(testBoard);
        newDeck2.registerIntoBoard(testBoard);
        testBoard.addDeck(newDeck1);
        testBoard.addDeck(newDeck2);

        try {
            testBoard.delete(testUser);
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            throw new UnAuthenticationException();
        }

        assertEquals(testBoard.isDeleted(), true);
        assertEquals(testBoard.getToDoDecks().get(0).isDeleted(), true);
        assertEquals(testBoard.getToDoDecks().get(1).isDeleted(), true);
    }

    @Test (expected = UnAuthenticationException.class)
    public void delete_wrongUser() throws UnAuthenticationException {
        try {
            testBoard.delete(wrongUser);
        } catch (UnAuthenticationException e) {
            e.printStackTrace();
            assertEquals(testBoard.isDeleted(), false);
            throw new UnAuthenticationException();
        }
    }
}