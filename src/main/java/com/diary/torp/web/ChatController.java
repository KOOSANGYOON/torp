package com.diary.torp.web;

import com.diary.torp.domain.*;
import com.diary.torp.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/chat")
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    @Resource (name = "userRepository")
    private UserRepository userRepository;

    @Resource
    private ChatRoomRepository roomRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    // --- 추 후 채팅방 추가 기능 ---
    @GetMapping("/test")
    public String roomList(@LoginUser User loginUser, Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        return "/chat/roomList";
    }

    @GetMapping("/{id}")
    public String room(@LoginUser User loginUser, @PathVariable long id, Model model) {
        log.debug("room in!");
        ChatRoom targetRoom = roomRepository.findOne(id);
        model.addAttribute("roomInfo", id);
        model.addAttribute("title", targetRoom.getTitle());
        return "/chat/chat";
    }
    // --------------------------
}
