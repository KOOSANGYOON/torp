package com.diary.torp.web;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.domain.*;
import com.diary.torp.security.LoginUser;
import com.diary.torp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/boards")
public class BoardsController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Resource (name = "toDoService")
    private ToDoService toDoService;

    @Resource (name = "toDoBoardRepository")
    private ToDoBoardRepository toDoBoardRepository;

    @Resource (name = "toDoDeckRepository")
    private ToDoDeckRepository toDoDeckRepository;

    @Resource (name = "toDoCardRepository")
    private ToDoCardRepository toDoCardRepository;

    @GetMapping("")
    public String boardList(@LoginUser User loginUser, Model model) {
//        model.addAttribute("boards", toDoBoardRepository.findByWriter(loginUser));
        model.addAttribute("boards", toDoBoardRepository.findByDeletedAndWriter(false, loginUser));

        return "/board/boards";
    }

    @GetMapping("/{id}")
    public String showBoard(@LoginUser User loginUser, @PathVariable Long id, Model model) {
        System.out.println("In the showboard");

        ToDoBoard board = toDoBoardRepository.findOne(id);
        if (!board.isOwner(loginUser)) {
            return "/error/error";
        }

        model.addAttribute("board", board);
        model.addAttribute("decks", toDoDeckRepository.findByToDoBoardAndDeleted(board, false));
        return "/board/board";
    }
}
