package com.example.demo.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends 
JpaRepository<User, Integer>{

	Optional<User> findByIdAndPass(Integer id, String pass);
	
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
			@Param("id") Integer id,
			@Param("name") String name,
			@Param("ageFrom") Integer ageFrom,
			@Param("ageTo") Integer ageTo,
			@Param("startFrom") Date startFrom,
			@Param("startTo") Date startTo,
			@Param("endFrom") Date endFrom,
			@Param("endTo") Date endTo
    );
}
