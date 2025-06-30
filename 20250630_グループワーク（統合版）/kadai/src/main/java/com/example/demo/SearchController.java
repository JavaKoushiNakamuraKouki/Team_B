package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class SearchController {
	
	@Autowired
	private UserRepository userRepository;
	
	//検索画面
	@GetMapping("/user/search")
	public String searchForm(Model model, HttpSession session) {
		return "search_form";
	}
	
	// 検索画面表示（/user/listへのGETリクエスト）
    @GetMapping("/user/list")
    public String searchFormAndList(
            @RequestParam(value = "id", required = false) String idStr,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "ageFrom", required = false) String ageFromStr,
            @RequestParam(value = "ageTo", required = false) String ageToStr,
            @RequestParam(value = "startFrom", required = false) String startFromStr,
            @RequestParam(value = "startTo", required = false) String startToStr,
            @RequestParam(value = "endFrom", required = false) String endFromStr,
            @RequestParam(value = "endTo", required = false) String endToStr,
            Model model) {
		
    	Long id = null; 
        Integer ageFrom = null, ageTo = null; 
        Date startFrom = null, startTo = null, endFrom = null, endTo = null;
        boolean hasError = false;
        List<String> errors = new ArrayList<>();

		
		//IDチェック
		if(idStr != null && !idStr.isEmpty()) {
			try {
				id = Long.parseLong(idStr);
			} catch(NumberFormatException e) {
				errors.add("社員IDは数値で入力してください。");
				hasError = true;
			}
		} 
		
		//年齢チェック
		if(ageFromStr != null && !ageFromStr.isEmpty()) {
			try {
				ageFrom = Integer.parseInt(ageFromStr);
			} catch(NumberFormatException e) {
				errors.add("年齢(From)は数値で入力してください。");
				hasError = true;
			}
		} 
		
		if(ageToStr != null && !ageToStr.isEmpty()) {
			try {
				Integer ageToTemp = Integer.parseInt(ageToStr);
				if(ageFrom != null && ageToTemp < ageFrom) {
					errors.add("年齢の範囲指定が正しくありません。年齢(From)は年齢(To)以下で入力してください。");
					hasError = true;
				} ageTo = ageToTemp;
			} catch(NumberFormatException e) {
				errors.add("年齢(To)は数値で入力してください。");
				hasError = true;
			}
		} 
		
		//日付変換
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    sdf.setLenient(false);
		
		//開始日チェック
	    try {
	    	if (startFromStr != null && !startFromStr.isEmpty()) {
	    		startFrom = sdf.parse(startFromStr);
	        }
	    	if (startToStr != null && !startToStr.isEmpty()) {
	            startTo = sdf.parse(startToStr);
	        }
	    	if (startFrom != null && startTo != null && startFrom.after(startTo)) {
	            errors.add("開始日の範囲指定が正しくありません。開始日(From)は開始日(To)以前で入力してください。");
	            hasError = true;
	        }
	    } catch (ParseException e) {
	        errors.add("開始日の日付形式が不正です。yyyy-MM-dd形式で入力してください。");
	        hasError = true;
	    }
		
		//終了日チェック
	    try {
	    	if (endFromStr != null && !endFromStr.isEmpty()) {
	    		endFrom = sdf.parse(endFromStr);
	    	}
	    	if (endToStr != null && !endToStr.isEmpty()) {
	            endTo = sdf.parse(endToStr);
	        }
	    	if (endFrom != null && endTo != null && endFrom.after(endTo)) {
	    		errors.add("終了日の範囲指定が正しくありません。終了日(From)は終了日(To)以前で入力してください。");
	            hasError = true;
	        }
	    } catch (ParseException e) {
	        errors.add("終了日の日付形式が不正です。yyyy-MM-dd形式で入力してください。");
	        hasError = true;
	    }
		
		model.addAttribute("errors", errors);
		List<User> users = new ArrayList<>();
		
		if (!hasError) {
			users = userRepository.searchUsers(
				id,
				(name != null && !name.isEmpty()) ? name : null,
				ageFrom,
				ageTo,
				startFrom, 
				startTo, 
				endFrom,
				endTo
            );
            model.addAttribute("searchResultCount", users.size());
        } else {
            model.addAttribute("searchResultCount", 0); // エラー時は件数を0と表示
        }
		
        model.addAttribute("users", users);
        
        // 検索条件を保持
        model.addAttribute("id", idStr);
        model.addAttribute("name", name);
        model.addAttribute("ageFrom", ageFromStr);
        model.addAttribute("ageTo", ageToStr);
        model.addAttribute("startFrom", startFromStr);
        model.addAttribute("startTo", startToStr);
        model.addAttribute("endFrom", endFromStr);
        model.addAttribute("endTo", endToStr);
        
        return "search_form";
	}

	
	//社員更新画面への遷移
	//@GetMapping("/user/update")
	//public String updateUser(@RequestParam("id") Long userId, Model model, HttpSession session) {
        //Optional<User> userOptional = userRepository.findById(userId);
        //if (userOptional.isPresent()) {
           // model.addAttribute("user", userOptional.get());
        //} else {
            // エラー処理（ユーザーが見つからない場合）
          //  return "redirect:/user/list"; //リスト画面に戻す
        //}
        //return "update_form"; // 更新画面のHTML名
    //}
	
	//社員削除確認画面への遷移
    @PostMapping("/user/delete_confirm")
    public String confirmDeleteUsers(@RequestParam("selectID") List<Long> selectIDs, Model model) { // IDの型をLongに
        List<User> usersToDelete = new ArrayList<>();
        for (Long id : selectIDs) {
            userRepository.findById(id).ifPresent(usersToDelete::add);
        }
        model.addAttribute("usersToDelete", usersToDelete);
        return "user_delete"; // 削除確認画面のhtml名
    }
    
    // 社員削除処理 
    @GetMapping("/list")
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


