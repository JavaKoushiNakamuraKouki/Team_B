package com.example.demo.Repository;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
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
    
    Optional<User> findByIdAndPass(Long id, String pass);
    

    // 検索プログラムのsearchUsersメソッド
    @Query("SELECT u FROM User u WHERE "
    	     + "(:id IS NULL OR u.id = :id) AND "
    	     + "(:name IS NULL OR u.name LIKE %:name%) AND "
    	     + "(:ageMin IS NULL OR u.age >= :ageMin) AND "
    	     + "(:ageMax IS NULL OR u.age <= :ageMax) AND "
    	     + "(:startFrom IS NULL OR u.start >= :startFrom) AND "
    	     + "(:startTo IS NULL OR u.start <= :startTo) AND "
    	     + "(:endFrom IS NULL OR u.end >= :endFrom) AND "
    	     + "(:endTo IS NULL OR u.end <= :endTo)")
    	List<User> searchUsers(
    	    @Param("id") Long id,
    	    @Param("name") String name,
    	    @Param("ageMin") Integer ageMin,
    	    @Param("ageMax") Integer ageMax,
    	    @Param("startFrom") Date startFrom,
    	    @Param("startTo") Date startTo,
    	    @Param("endFrom") Date endFrom,
    	    @Param("endTo") Date endTo
    	);
}