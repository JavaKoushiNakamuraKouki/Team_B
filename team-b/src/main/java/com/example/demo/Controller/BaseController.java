package com.example.demo.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice //ヘッダー・フッターの共通化
public class BaseController {

	@ModelAttribute
	public void commonAttribute(Model model, HttpSession session) {
		String loggedInUserName = (String) session.getAttribute("loggedInUserName");
		 if (loggedInUserName != null) {
			model.addAttribute("loginUser", loggedInUserName + "さん");
			model.addAttribute("loginDateTime", LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy/MM/dd　HH:mm:ss")));
		 }
	}
	
	@ModelAttribute("copyright")
	public String copyright() {
		return "Copyright © 2025";
	}
	
	 @ModelAttribute("teamName")
	 public String teamName() {
		 return "JavaStudy team-b";
	}

}