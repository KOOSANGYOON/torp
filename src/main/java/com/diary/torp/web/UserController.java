package com.diary.torp.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.diary.torp.UnAuthenticationException;
import com.diary.torp.UnAuthorizedException;
import com.diary.torp.domain.User;
import com.diary.torp.security.HttpSessionUtils;
import com.diary.torp.service.UserService;

import javassist.tools.reflect.CannotCreateException;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Resource(name = "userService")
	private UserService userService;
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "/user/loginForm";
	}
	
	@PostMapping("/login")
	public String login(HttpSession session, String userId, String password) {
		log.debug("user id : " + userId + " | password : " + password);
		
		try {
			User loginUser = userService.login(userId, password);
			session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, loginUser);
		} catch (UnAuthenticationException e) {
			e.printStackTrace();
			return "redirect:/user/loginFail";
		} catch (UnAuthorizedException e) {
			e.printStackTrace();
			return "redirect:/user/loginFail";
		}
		return "redirect:/";
	}
	
	@GetMapping("/loginFail")
	public String fail(Model model) {
		model.addAttribute("errorMessage", "로그인 실패. 아이디와 비밀번호를 확인해주세요.");
		return "/user/loginForm";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		log.debug("======== Success to LOGOUT!! ========");

		return "redirect:/";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "/user/joinForm";
	}
	
	@GetMapping("/joinFail")
	public String joinFail(Model model) {
		model.addAttribute("errorMessage", "생성 실패. 이미 존재하는 아이디입니다.");
		return "/user/joinForm";
	}
	
	@PostMapping("/join")
	public String join(HttpSession session, String userId, String password, String name) {
		try {
			User newUser = userService.join(userId, password, name);
			log.debug("생성 완료.");
			session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, newUser);
		} catch (CannotCreateException e) {
			e.printStackTrace();
			return "redirect:/user/joinFail";
		}
		return "redirect:/";
	}
	
	@GetMapping("/updateForm")
	public String updateForm(HttpSession session , Model model) {
		User user = HttpSessionUtils.getUserFromSession(session);
		log.debug("login user is : " + user.toString());
		model.addAttribute(user);
		return "/user/updateForm";
	}
	
	@PostMapping("/update")
	public String update(HttpSession session, String name, String password) {
		User user = HttpSessionUtils.getUserFromSession(session);
		log.debug("login user is : " + user.toString());
		
		try {
			User updatedUser = userService.update(user, password, name);
			log.debug("update info : " + updatedUser.toString());
		} catch (UnAuthenticationException e) {
			e.printStackTrace();
			return "redirect:/user/updateFail";
		}
		return "redirect:/";
	}
	
	@GetMapping("/updateFail")
	public String updateFail(HttpSession session, Model model) {
		User user = HttpSessionUtils.getUserFromSession(session);
		model.addAttribute("errorMessage", "업데이트 실패. 비밀번호를 확인해주세요.");
		model.addAttribute(user);
		return "/user/updateForm";
	}
}
