package com.diary.torp.web;

import com.diary.torp.domain.ToDoBoard;
import com.diary.torp.domain.ToDoBoardRepository;
import com.diary.torp.domain.ToDoDeck;
import com.diary.torp.domain.User;
import com.diary.torp.security.LoginUser;
import com.diary.torp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/boards")
public class ApiBoardsController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @Resource(name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @PostMapping("/add")
    public ToDoBoard createBoard(@LoginUser User loginUser, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("title is " + title);

        ToDoBoard newToDoBoard = toDoService.createBoard(loginUser, title);

        return newToDoBoard;
    }

    @PostMapping("/{id}/add")
    public ToDoDeck createDeck(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody String title) {
        log.debug("user is " + loginUser);
        log.debug("title is " + title);

        ToDoDeck newToDoDeck = toDoService.createDeck(loginUser, title);
        toDoService.addDeck(id, newToDoDeck);

        return newToDoDeck;
    }
}
