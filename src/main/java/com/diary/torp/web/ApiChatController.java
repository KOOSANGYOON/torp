package com.diary.torp.web;

import com.diary.torp.domain.ChatRoom;
import com.diary.torp.domain.ChatRoomRepository;
import com.diary.torp.domain.User;
import com.diary.torp.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat")
public class ApiChatController {
    private static final Logger log = LoggerFactory.getLogger(ApiChatController.class);

    @Resource(name = "chatRoomRepository")
    private ChatRoomRepository chatRoomRepository;

    @PostMapping("/addRoom")
    public ChatRoom createRoom(@LoginUser User loginUser, @Valid @RequestBody String title) {
        log.debug("LoginUser is : " + loginUser);
        log.debug("Title is : " + title);

        ChatRoom newRoom = new ChatRoom(title, loginUser);
        chatRoomRepository.save(newRoom);
        return newRoom;
    }
}
