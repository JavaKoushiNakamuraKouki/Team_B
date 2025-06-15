package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;

@Service
public class RegisterService {

	private final UserRepository userRepository;


    @Autowired
    public RegisterService(UserRepository userRepository) { // クラス名に合わせて修正
        this.userRepository = userRepository;
    }


    // ユーザーを保存
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // すべてのユーザーを取得
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // IDからユーザーを取得
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ユーザーを削除
//    public void deleteUser(Long id) {
  //      userRepository.deleteById(id);
   // }


}