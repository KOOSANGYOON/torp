package com.diary.torp.web;

import com.diary.torp.domain.User;
import com.diary.torp.domain.UserRepository;
import com.diary.torp.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Resource (name = "userRepository")
    private UserRepository userRepository;

    @GetMapping("")
    public String roomList(@LoginUser User loginUser, Model model) {
        return "/chat/roomList";
    }
}
