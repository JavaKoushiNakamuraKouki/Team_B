package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MyController {
	
	@GetMapping("/top")
	public String topMenu(Model model, HttpSession session) {
		//ログイン状態を確認
		String loggedInUserName = (String) session.getAttribute("loggedInUserName");
		if (loggedInUserName == null) {
			//ログインしていない場合はログイン画面へ
			return "redirect:/";
		}
		
		//①ログイン情報
		model.addAttribute("loginUser", loggedInUserName + "さん");
		model.addAttribute("loginDateTime", LocalDateTime.now()
		.format(DateTimeFormatter.ofPattern("yyyy/MM/dd　HH:mm:ss")));
		
		//②リンク
		List<MenuItem> menuItems = Arrays.asList(
			new MenuItem("社員情報検索", "/user/search"),
			new MenuItem("社員情報削除", "/user/delete"),
			new MenuItem("社員情報登録", "/user/register"),
			new MenuItem("社員情報更新", "/user/update")
		);
		model.addAttribute("menuItems", menuItems);
		
		//③フッター
		model.addAttribute("copyright", "Copyright © 2025");
		model.addAttribute("teamName", "JavaStudy team-b");
		
		return "top_menu";
	}
	
	public static class MenuItem {
        private String name;
        private String url;

        public MenuItem(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }
	}
}
