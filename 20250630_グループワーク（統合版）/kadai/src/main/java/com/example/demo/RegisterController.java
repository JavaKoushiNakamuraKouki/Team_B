package com.example.demo;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.form.RegisterForm; // RegisterFormは別途定義が必要

import jakarta.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepository;
  	
  	//登録画面表示
  	@GetMapping("/user/register")
  	public String showRegisterForm(Model model) {	
  	    model.addAttribute("registerForm", new RegisterForm());
  	    return "add"; // add.html を表示
  	}
  	
  	//登録内容確認
  	@PostMapping("/user/register_confirm") // << パスを /user/register_confirm に変更
    public String confirmRegister(@Valid @ModelAttribute RegisterForm registerForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add";
        }
        model.addAttribute("registerForm", registerForm); // 入力されたRegisterFormオブジェクトをそのまま渡す
        return "confirm"; // confirm.html へ遷移
    }
  	
  	//最終的な登録処理 (POST /user/register)
    @PostMapping("/user/register") // << パスは /user/register のまま、処理内容を変更
    public String completeRegister(@ModelAttribute RegisterForm registerForm, RedirectAttributes redirectAttributes) {
        User user = new User();
        user.setName(registerForm.getName());
        user.setAge(registerForm.getAge());
        user.setPass(registerForm.getPass());
        
        // 開始日
        Date currentDate = new Date();  
        user.setStart(new Date());
        
        // 終了日を開始日の1年後に設定
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(currentDate); 
        calendar.add(Calendar.YEAR, 1); // 1年加算
        user.setEnd(calendar.getTime()); 

        userRepository.save(user); // データ登録を実行

        redirectAttributes.addFlashAttribute("message", "ユーザー登録が完了しました。");
        return "redirect:/user/complete"; // 完了画面へリダイレクト
    }
  	
  	//登録完了画面表示
  	@GetMapping("/user/complete")
  	public String completeRegister() {
  	    return "complete"; // complete.html を表示
  	}

}