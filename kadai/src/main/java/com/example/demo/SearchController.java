package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.User;
import com.example.demo.dao.UserRepository;

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
	
	//検索処理
	@GetMapping({"/user/list"})
	public String searchUsers(
    		@RequestParam(value = "id", required = false) String idStr,
    		@RequestParam(value = "name", required = false) String name,
    		@RequestParam(value = "ageFrom", required = false) String ageFromStr,
    		@RequestParam(value = "ageTo", required = false) String ageToStr,
    		@RequestParam(value = "startFrom", required = false) String startFromStr,
    		@RequestParam(value = "startTo", required = false) String startToStr,
    		@RequestParam(value = "endFrom", required = false) String endFromStr,
    		@RequestParam(value = "endTo", required = false) String endToStr,
    		Model model, HttpSession session) {
		
		Integer id = null, ageFrom = null, ageTo = null;
		Date startFrom = null, startTo = null, endFrom = null, endTo = null;
		boolean hasError = false;
		List<String> errors = new ArrayList<>();
		
		//IDチェック
		if(idStr != null && !idStr.isEmpty()) {
			try {
				id = Integer.parseInt(idStr);
			} catch(NumberFormatException e) {
				errors.add("社員IDは数値で入力してください。");
				hasError = true;
			}
		} //else if (idStr != null) {
			//id = null;
		//}
		
		//年齢チェック
		if(ageFromStr != null && !ageFromStr.isEmpty()) {
			try {
				ageFrom = Integer.parseInt(ageFromStr);
			} catch(NumberFormatException e) {
				errors.add("年齢(From)は数値で入力してください。");
				hasError = true;
			}
		} //else if (ageFromStr != null) {
			//ageFrom = null;
		//}
		
		if(ageToStr != null && !ageToStr.isEmpty()) {
			try {
				Integer ageToTemp = Integer.parseInt(ageToStr);
				if(ageFrom != null && ageToTemp < ageFrom) {
					errors.add("年齢の範囲指定が正しくありません。年齢(From)は年齢(To)以下で入力してください。");
					hasError = true;
				}
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
        return "search_form";
	}
	
	//社員情報登録画面へ遷移
	@GetMapping("/user/register")
	public String registerForm(Model model, HttpSession session) {
		String loggedInUserName = (String) session.getAttribute("loggedInUserName");
		if (loggedInUserName == null) {
			return "redirect:/";
		}
		model.addAttribute("loginUser", loggedInUserName + "さん");
		model.addAttribute("loginDateTime", LocalDateTime.now()
				.format(DateTimeFormatter.ofPattern("yyyy/MM/dd　HH:mm:ss")));
		return "register_form"; //登録画面のhtml名
	}
	
	//メインメニューへ遷移
	//@GetMapping("/menu")
	//public String menuRedirect(HttpSession session) {
		//if (session.getAttribute("loggedInUserName") == null) {
			//return "redirect:/";
		//}
		//return "top_menu"; 
	//}
	
	//社員更新画面への遷移
	@GetMapping("/user/update")
	public String updateUser(@RequestParam("id") Integer userId, Model model, HttpSession session) {
		model.addAttribute("userId", userId);
		return "update_form"; //更新画面のhtml名
    }
	
	//社員削除確認画面への遷移
    @PostMapping("/user/delete")
    public String deleteUsers(@RequestParam("selectID") List<Integer> selectID, Model model, HttpSession session) {
    	model.addAttribute("selectID", selectID);
    	return "delete_confirm"; // 削除確認画面のhtml名
    }
		
}
