package com.diary.torp.web;

import com.diary.torp.domain.ToDoBoard;
import com.diary.torp.domain.ToDoBoardRepository;
import com.diary.torp.domain.User;
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

    @GetMapping("")
    public String boardList(@LoginUser User loginUser, Model model) {
//        model.addAttribute("toDoBoards", toDoBoardRepository.findByDeletedAndWriter(loginUser.getId(), false));
        model.addAttribute("boards", toDoBoardRepository.findByWriter(loginUser));
        return "/board/boards";
    }

    @GetMapping("/{id}")
    public String showBoard(@PathVariable Long id, Model model) {
        System.out.println("In the showboard");
        ToDoBoard board = toDoBoardRepository.findById(id);
        model.addAttribute("board", board);
        return "/board/board";
    }
}
