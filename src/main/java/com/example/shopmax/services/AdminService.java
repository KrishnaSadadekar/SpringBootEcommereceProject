package com.example.shopmax.services;

import com.example.shopmax.dao.CategoryRepository;
import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.helper.Message;
import com.example.shopmax.models.Category;
import com.example.shopmax.models.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AdminService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    private String generateImageNameWithDateTime(String originalFileName) {
        // Format the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // Append formatted date and time to the original file name
        return now.format(formatter) + "_" + originalFileName;
    }



    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    public Product addProduct(Product product, MultipartFile file) {

        try {
            if (file.isEmpty()) {
                product.setImageUrl("defaultProduct.png");
                System.out.println("File is Empty!");

            } else {
                String imageNameWithDateTime = generateImageNameWithDateTime(file.getOriginalFilename());

                product.setImageUrl(imageNameWithDateTime);

                File savefile = new ClassPathResource("static/images").getFile();
                System.out.println(savefile.getAbsolutePath());
                Path path = Paths.get(savefile.getAbsolutePath() + File.separator + imageNameWithDateTime);
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


            }
            return product;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Error Found!" + e);
            throw new RuntimeException();
        }

    }


    public Product updateProduct(MultipartFile file, Product product, Product oldproduct) {
        try {

            if(!file.isEmpty()) {
                //delete first old file-------------------

                if (oldproduct.getImageUrl() != null && (!oldproduct.getImageUrl().equals("defaultProduct.png"))) {
                    File savefile = new ClassPathResource("static/images").getFile();
                    System.out.println();
                    Path path = Paths.get(savefile.getAbsolutePath() + File.separator + oldproduct.getImageUrl());
                    Files.delete(path);
                    System.out.println("deleted!" + savefile.getName() + "----");
                }
                //upload file----------------------------------------
                String imageNameWithDateTime = generateImageNameWithDateTime(file.getOriginalFilename());
                product.setImageUrl(imageNameWithDateTime);
                System.out.println("File Name: " + file.getOriginalFilename());

                File savefile1 = new ClassPathResource("static/images").getFile();
                Path path1 = Paths.get(savefile1.getAbsolutePath() + File.separator + imageNameWithDateTime);
                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                return this.productRepository.save(product);

            } else {
                product.setImageUrl(oldproduct.getImageUrl());
                System.out.printf("Added to product!");
                return this.productRepository.save(product);


            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
