package com.example.shopmax.controllers;


import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.models.Product;
import com.example.shopmax.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    UserService userService;

    @GetMapping("/search/{query}")
    public ResponseEntity<?> search(@PathVariable("query") String query) {
        System.out.println("Enter!");
        System.out.println(query);
        List<Product> products = this.productRepository.findByProductNameContaining(query);
        return ResponseEntity.ok(products);
    }


}
