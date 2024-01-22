package com.example.shopmax.services;

import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.models.Product;
import com.example.shopmax.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    ProductRepository productRepository;


    private String generateImageNameWithDateTime(String originalFileName) {
        // Format the current date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

        // Append formatted date and time to the original file name
        return now.format(formatter) + "_" + originalFileName;
    }

    public User updateUser(MultipartFile file, User user, User oldUser) {

        try {

            if(!file.isEmpty()) {
                String imageNameWithDateTime = generateImageNameWithDateTime(file.getOriginalFilename());
                //delete first old file-------------------

                if (oldUser.getImageUrl() != null && (!oldUser.getImageUrl().equals("defaultProduct.png"))) {
                    File savefile = new ClassPathResource("static/images").getFile();
                    System.out.println();
                    Path path = Paths.get(savefile.getAbsolutePath() + File.separator + oldUser.getImageUrl());
                    Files.delete(path);
                    System.out.println("deleted!" + savefile.getName() + "----");
                }
                //upload file----------------------------------------

                user.setImageUrl(imageNameWithDateTime);
                System.out.println("File Name: " + file.getOriginalFilename());

                File savefile1 = new ClassPathResource("static/images").getFile();
                Path path1 = Paths.get(savefile1.getAbsolutePath() + File.separator + imageNameWithDateTime);
                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
                return this.userRepository.save(user);

            } else {
                user.setImageUrl(oldUser.getImageUrl());
                System.out.println("Updated!");
                return this.userRepository.save(user);


            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }



}
