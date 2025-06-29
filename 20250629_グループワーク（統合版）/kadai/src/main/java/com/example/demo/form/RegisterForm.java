package com.example.demo.form;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class RegisterForm {
	
    @NotBlank(message = "必須項目です。")
    private String name;

    @Positive//負の値やゼロの入力を防ぐ
    @NotNull(message = "年齢を入力してください。")
    @Digits(integer = 3, fraction = 0, message = "年齢は数値のみ入力してください。")//整数のみ許可
    private Integer age;

    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 6, message = "パスワードは8文字以上にしてください。")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z0-9]+$", message = "パスワードは英字と数字の組み合わせで入力してください。（半角英数字のみ)")
    private String pass;

    
    @NotBlank(message = "確認用パスワードを入力してください。")
    private String confirmPass;

    @AssertTrue(message = "パスワードと確認用パスワードが一致しません。")
    public boolean isPasswordMatched() {
        return pass != null && confirmPass != null && pass.equals(confirmPass);
    }
}