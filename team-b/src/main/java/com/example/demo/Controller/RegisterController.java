package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.form.RegisterForm;

import jakarta.validation.Valid;


@Controller
public class RegisterController {

    /**
     * 登録情報 Service
     */
	@Autowired
	private UserRepository userRepository; // 今後使う予定ならそのまま

    
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
	
	//削除画面表示


	@GetMapping("/user/list")
	public String showUserList(Model model) {
	    List<User> users = userRepository.findAll(); // 全ユーザー取得
	    model.addAttribute("users", users);
	    return "user_list"; // user_list.html へ遷移
	}
	@PostMapping("/user/delete")
	public String deleteUser(@RequestParam("id") Long userId) { 
	    userRepository.deleteById(userId); // ユーザー削除
	    return "redirect:/user/delete_complete"; // 削除完了画面へ遷移
	}
	@GetMapping("/user/delete_complete")
	public String deleteComplete(Model model) {
	    model.addAttribute("message", "ユーザーの削除が完了しました。");
	    return "delete_complete"; // 削除完了画面を表示
	}
	
}
