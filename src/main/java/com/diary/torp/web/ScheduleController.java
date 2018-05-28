package com.diary.torp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    @GetMapping("/1")
    public String loginOne() {
        return "/board/boards";
    }

    @GetMapping("/2")
    public String loginTwo() { return "/board/board"; }

    @GetMapping("/3")
    public String loginThree() { return "/board/index"; }
}
