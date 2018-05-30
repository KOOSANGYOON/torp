package com.diary.torp.web;

import com.diary.torp.domain.ToDoBoard;
import com.diary.torp.domain.ToDoBoardRepository;
import com.diary.torp.domain.User;
import com.diary.torp.security.LoginUser;
import com.diary.torp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/boards")
public class ApiBoardsController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource (name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @PostMapping ("/add")
    public ToDoBoard createBoard(@LoginUser User loginUser, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("title is " + title);

        ToDoBoard newToDoBoard = toDoService.create(loginUser, title);

        return newToDoBoard;
    }
}
