package com.diary.torp.service;

import com.diary.torp.domain.*;
import com.diary.torp.web.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ToDoService {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource(name="toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Resource(name="toDoDeckRepository")
    private ToDoDeckRepository toDoDeckRepository;

    public ToDoBoard createBoard(User user, String title) {
        ToDoBoard newBoard = new ToDoBoard(user, title);
        return toDoBoardRepository.save(newBoard);
    }

    public ToDoDeck createDeck(User user, String title) {
        ToDoDeck newDeck = new ToDoDeck(user, title);
        return toDoDeckRepository.save(newDeck);
    }
}
