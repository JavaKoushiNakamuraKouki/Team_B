package com.example.demo.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);
    
    User findFirstByOrderByIdDesc();
    
    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findLatestUsers(Pageable pageable);
}
