package com.example.demo.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Model.User; 

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // IDの型をLongに統一

    Optional<User> findById(Long id); // IDの型をLongに統一

    User findFirstByOrderByIdDesc();

    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findLatestUsers(Pageable pageable);

    // 検索プログラムのsearchUsersメソッド
    @Query(value = "SELECT * FROM users WHERE " +
            "(:id IS NULL OR id = :id) AND " +
            "(:name IS NULL OR name LIKE %:name%) AND " +
            "(:ageFrom IS NULL OR age >= :ageFrom) AND " +
            "(:ageTo IS NULL OR age <= :ageTo) AND " +
            "(:startFrom IS NULL OR start >= :startFrom) AND " +
            "(:startTo IS NULL OR start <= :startTo) AND " +
            "(:endFrom IS NULL OR end >= :endFrom) AND " +
            "(:endTo IS NULL OR end <= :endTo)", nativeQuery = true)
    List<User> searchUsers(
            @Param("id") Long id, 
            @Param("name") String name,
            @Param("ageFrom") Integer ageFrom, 
            @Param("ageTo") Integer ageTo,    
            @Param("startFrom") Date startFrom,
            @Param("startTo") Date startTo,
            @Param("endFrom") Date endFrom,
            @Param("endTo") Date endTo
    );
    // findByIdAndPass は必要に応じて追加
    Optional<User> findByIdAndPass(Long id, String pass); 
}