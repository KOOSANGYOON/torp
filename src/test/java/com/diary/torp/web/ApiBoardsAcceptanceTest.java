package com.diary.torp.web;

import com.diary.torp.domain.ToDoBoardRepository;
import com.diary.torp.service.ToDoService;
import com.diary.torp.support.AcceptanceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ApiBoardsAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiBoardsAcceptanceTest.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource(name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Test
    public void createBoard() {
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        log.debug("body : {}", response.getBody());

        ResponseEntity<String> response2 = template().postForEntity("/api/boards/add", "testBoard",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    @Test
    public void createDeck() {
    }

    @Test
    public void createCard() {
    }

    @Test
    public void getCardInfo() {
    }

    @Test
    public void editDescription() {
    }

    @Test
    public void addComment() {
    }

    @Test
    public void editBoardTitle() {
    }

    @Test
    public void editDeckTitle() {
    }

    @Test
    public void editCardTitle() {
    }

    @Test
    public void deleteBoard() {
    }
}