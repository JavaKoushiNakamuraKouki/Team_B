package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.form.RegisterForm; // RegisterFormは別途定義が必要

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

  //登録画面
  	@PostMapping("/user/register")
  	public String registerUser(@Valid @ModelAttribute RegisterForm registerForm, BindingResult result, Model model) {
  	    if (result.hasErrors()) {
  	        model.addAttribute("registerForm", registerForm); // 入力値を保持
  	        return "add"; // エラーがある場合は入力画面に戻る
  	    }

  	    User user = new User();
  	    user.setName(registerForm.getName());
  	    user.setAge(registerForm.getAge());
  	    user.setPass(registerForm.getPass());

  	    userRepository.save(user); // データ登録

  	    model.addAttribute("registeredUser", user); // 確認画面へ渡す
  	    return "confirm"; // confirm.html へ遷移
  	}
  	
  	@GetMapping("/user/register")
  	public String showRegisterForm(Model model) {
  	    model.addAttribute("registerForm", new RegisterForm());
  	    return "add"; // add.html のテンプレートを表示
  	}
  	
  	@GetMapping("/user/complete")
  	public String completeRegister() {
  	    return "complete"; // complete.html を表示
  	}

  	@GetMapping("/add")
  	public String showAddForm(Model model) {
  	    model.addAttribute("registerForm", new RegisterForm());
  	    return "add"; // ここがファイル名と一致しているか確認
  	}
}