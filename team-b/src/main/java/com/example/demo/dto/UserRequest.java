package com.example.demo.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * ユーザー情報 リクエストデータ
 */
@Data
public class UserRequest implements Serializable {
	
	/**
     * 名前
     */
	private String name;
	/**
     * 年齢
     */
	private Integer age;
	/**
     * パスワード
     */
	private String pass;
}
