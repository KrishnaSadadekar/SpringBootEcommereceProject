package com.example.shopmax.dao;

import com.example.shopmax.models.CartItem;
import com.example.shopmax.models.Product;
import com.example.shopmax.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    @Query("SELECT u FROM CartItem u WHERE u.product=:product AND u.user=:user AND u.size=:size")
    CartItem findByProductAndUser(@Param("product") Product product, @Param("user") User user,@Param("size") String size);

    @Query("SELECT u FROM CartItem u WHERE u.user=:user")
    List<CartItem> findByUser(@Param("user") User user);

}
