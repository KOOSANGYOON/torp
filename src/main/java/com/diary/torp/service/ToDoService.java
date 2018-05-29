package com.diary.torp.service;

import com.diary.torp.domain.ToDoBoard;
import com.diary.torp.domain.ToDoBoardRepository;
import com.diary.torp.domain.User;
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

    public ToDoBoard create(User user, String title) {
        ToDoBoard newBoard = new ToDoBoard(user, title);
        return toDoBoardRepository.save(newBoard);
    }
}
