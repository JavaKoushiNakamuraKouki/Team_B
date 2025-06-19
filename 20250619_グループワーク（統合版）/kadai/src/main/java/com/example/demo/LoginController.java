package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/")
	public String showLoginForm() {
		return "login_form";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("id") String idStr,
						@RequestParam("pass") String pass,
						Model model, HttpSession session) {
		
		Long id = null;
		try {
			id = Long.parseLong(idStr);
		} catch (NumberFormatException e) {
			model.addAttribute("loginError", "IDかパスワードに誤りがあります。");
			return "login_form";
		}
		
		Optional<User> user = userRepository.findByIdAndPass(id, pass);
		
		if(user.isPresent()) {
			session.setAttribute("loggedInUserId", user.get().getId());
			session.setAttribute("loggedInUserName", user.get().getName());
			return "redirect:/top";	
		} else {
			model.addAttribute("loginError", "IDかパスワードに誤りがあります。");
			return "login_form";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
}
