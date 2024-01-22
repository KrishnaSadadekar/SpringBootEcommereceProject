package com.example.shopmax.dao;

import com.example.shopmax.models.Orders;
import com.example.shopmax.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders,Integer> {

    @Query("select u from Orders u where u.user=:user")
    List<Orders> findByUser(@Param("user") User user);

}
