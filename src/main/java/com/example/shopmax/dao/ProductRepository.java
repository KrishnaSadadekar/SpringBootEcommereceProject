package com.example.shopmax.dao;

import com.example.shopmax.models.Category;
import com.example.shopmax.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository  extends JpaRepository<Product, Integer> {



    public  List<Product>findByProductNameContaining(String name);

    @Query("select u from Product u where u.category=:category")
    List<Product> findByCategory(@Param("category")Category category);

}
