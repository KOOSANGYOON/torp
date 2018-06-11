package com.diary.torp.web;

import com.diary.torp.domain.*;
import com.diary.torp.service.ToDoService;
import com.diary.torp.support.AcceptanceTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BoardsAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(BoardsAcceptanceTest.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource (name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Resource (name = "toDoDeckRepository")
    private ToDoDeckRepository toDoDeckRepository;

    @Resource (name = "toDoCardRepository")
    private ToDoCardRepository toDoCardRepository;

    @Test
    public void boardList() {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/boards", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> secondResponse = template().getForEntity("/boards", String.class);
        assertThat(secondResponse.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void showBoard() {
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/boards/add", "testBoard",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        ToDoBoard testBoard = toDoBoardRepository.findByTitle("testBoard");

        ResponseEntity<String> response2 = basicAuthTemplate().getForEntity(String.format("/boards/%d", testBoard.getId()), String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());

        ResponseEntity<String> response3 = template().getForEntity(String.format("/boards/%d", testBoard.getId()), String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}