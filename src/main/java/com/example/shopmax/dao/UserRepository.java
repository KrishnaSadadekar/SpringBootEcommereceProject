package com.example.shopmax.dao;

import com.example.shopmax.models.Category;
import com.example.shopmax.models.Product;
import com.example.shopmax.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email=:email")
    User getUserByUserName(@Param("email") String email);

    boolean existsByEmail(String email);

    @Query("select u from User u where u.email=:email")
    User findByEmail(@Param("email") String email);
}
