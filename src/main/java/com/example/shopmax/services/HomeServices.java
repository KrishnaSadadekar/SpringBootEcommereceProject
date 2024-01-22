package com.example.shopmax.services;

import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class HomeServices {

    @Autowired
    ProductRepository productRepository;

    public List<Product> displayProducts() {
        return productRepository.findAll().stream().toList();
    }

    public Product productsDetails(Integer pId) {
        return productRepository.findById(pId).get();
    }


    private String generateImageNameWithDateTime(String originalFileName) {
        // Format the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // Append formatted date and time to the original file name
        return now.format(formatter) + "_" + originalFileName;
    }

}
