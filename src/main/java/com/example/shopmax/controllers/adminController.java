package com.example.shopmax.controllers;

import com.example.shopmax.dao.CategoryRepository;
import com.example.shopmax.dao.OrderRepository;
import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.helper.Message;
import com.example.shopmax.models.Category;
import com.example.shopmax.models.Orders;
import com.example.shopmax.models.Product;
import com.example.shopmax.models.User;
import com.example.shopmax.services.AdminService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class adminController {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    AdminService adminService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    OrderRepository orderRepository;


    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String dashboard(Model model) {

        model.addAttribute("title", "AdminDashboard");
        return "admin/adminDashboard";
    }


    @GetMapping("/addProduct")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String addProduct(Model model) {
        model.addAttribute("title", "AddProduct");
        List<Category> list = categoryRepository.findAll().stream().toList();
        System.out.println("_------");
        System.out.println(list);
        model.addAttribute("categoryList", list);
        return "admin/addProduct";
    }

    @PostMapping("/process-addProduct")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String addProductProcess(@ModelAttribute Product product,
                                    @RequestParam("catId") Integer id,
                                    @RequestParam("productImage") MultipartFile file,
                                    HttpSession session, Model model) {

        List<Category> list = categoryRepository.findAll().stream().toList();
        model.addAttribute("categoryList", list);
        Category category = categoryRepository.findById(id).get();
        try {
            product.setCategory(category);

            Product result = adminService.addProduct(product, file);
            productRepository.save(result);
            session.setAttribute("message", new Message("One more product is added!", "Success"));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("Product add error:-> " + e);
            session.setAttribute("message", new Message("Failed to add!", "failure"));
        }


        return "redirect:/admin/addProduct";
    }


    @GetMapping("/addCategory")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String addCategory(Model model) {
        model.addAttribute("title", "AddCategory");
        model.addAttribute("contact", new Category());
        return "admin/addCategory";
    }

    @PostMapping("/process-category")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String processCategory(@ModelAttribute Category category, HttpSession session) {
        try {
            adminService.addCategory(category);
            session.setAttribute("message", new Message("One more category is added!", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Failed to add!", "failure"));
        }


        return "admin/addCategory";
    }


    @GetMapping("/product-list")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String getProductList(Model model) {
        model.addAttribute("title", "ProductList");
        List<Product> list = productRepository.findAll().stream().toList();
        model.addAttribute("products", list);
        return "admin/ProductList";
    }


//    Update Product Details

    @GetMapping("/update-product/{id}")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String updateProductDetails(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("title", "UpdateProductDetails");
        Product product = productRepository.findById(id).get();
        model.addAttribute("product", product);
        List<Category> list = categoryRepository.findAll().stream().toList();
        model.addAttribute("categoryList", list);
        return "admin/updateProductDetails";
    }

//    Handler for update product


    @PostMapping("/process-updateProduct/{id}")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String updateHandler(@PathVariable("id") Integer pId, @RequestParam("catId") Integer cId,
                                @RequestParam("productImage") MultipartFile file,
                                Product product, HttpSession session) {

        try {
            Category category = categoryRepository.findById(cId).get();
            Product old = productRepository.findById(pId).get();
            product.setCategory(category);
            product.setId(old.getId());
            Product result = adminService.updateProduct(file, product, old);
            session.setAttribute("message", new Message("Products details updated!", "Success"));


        } catch (Exception ignored) {
            System.out.println("Error:->" + ignored);
            session.setAttribute("message", new Message("Failed to update!", "Failure"));
        }
        return "redirect:/admin/update-product/" + pId;
    }


    //Delete
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String deleteContact(@PathVariable("id") Integer cId, Model model, HttpSession session) throws IOException {

        Product product = productRepository.findById(cId).get();

        this.productRepository.delete(product);

        try {

            File savefile = new ClassPathResource("static/images").getFile();
            Path path = Paths.get(savefile.getAbsolutePath() + File.separator + product.getImageUrl());
            if (!product.getImageUrl().equals("defaultProduct.png")) {
                Files.delete(path);
            }
            session.setAttribute("message", new Message("product deleted successfully", "success"));

        } catch (Exception e) {
            System.out.println("Error:" + e);
            e.printStackTrace();
        }

        return "redirect:/admin/product-list";
    }

    //Admin Update

    @GetMapping("/update-admin")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String updateProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUserName(username);
        model.addAttribute("user", user);
        model.addAttribute("title", "UpdateProfile!");
        return "admin/updateAdmin";
    }


    //---------------[Update Profile]------------------------------------

    @PostMapping("/process-admin")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bResult,
                             @RequestParam("profileImage") MultipartFile file,
                             Model model, HttpSession session, Principal principal) {


        String name = principal.getName();
        User olduser = userRepository.getUserByUserName(name);
        System.out.println("Name: -" + olduser + "----" + user.getName());
//----------------------------------------------------
        try {

            user.setId(olduser.getId());
            user.setPassword(olduser.getPassword());
            user.setEmail(olduser.getEmail());
            user.setRole("Role_ADMIN");
            if (bResult.hasErrors()) {

                FieldError fieldError = bResult.getFieldError("name");
                if (fieldError != null) {
                    String errorMessage = fieldError.getDefaultMessage();
                    System.out.println(errorMessage);
                    return "admin/updateAdmin";
                }


            }
            if (!file.isEmpty()) {
//delete first old file-------------------
                if (olduser.getImageUrl() != null && (!olduser.getImageUrl().equals("defaultProfile.jpg"))) {
                    File savefile = new ClassPathResource("static/images").getFile();
                    Path path = Paths.get(savefile.getAbsolutePath() + File.separator + olduser.getImageUrl());
                    Files.delete(path);
                    System.out.println("deleted!");
                }
//upload file----------------------------------------

                user.setImageUrl(file.getOriginalFilename());
                System.out.println("File Name: " + file.getOriginalFilename());

                File savefile1 = new ClassPathResource("static/images").getFile();
                Path path1 = Paths.get(savefile1.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
            } else {

                user.setImageUrl(olduser.getImageUrl());


            }


            userRepository.save(user);
            session.setAttribute("message", new Message("Updated Successfully", "Failure"));
        } catch (Exception e) {
            session.setAttribute("message", new Message("Failed to Update", "Failure"));
        }
        return "admin/updateAdmin";
    }


    //-------------------------------------------------
    @PostMapping("/process-password")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String changepassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal) {
        User user = userRepository.getUserByUserName(principal.getName());
        System.out.println("Encoded Password: " + user.getPassword());
        System.out.println("Current Password" + currentPassword + " New Password: " + newPassword);

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return "redirect:/admin/update-admin";
        } else {
            return "admin/updateAdmin";
        }

    }


    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('Role_ADMIN')")
    public String allOrders(Model model) {
        List<Orders> orders = orderRepository.findAll().stream().toList();

        model.addAttribute("OrderList", orders);
        model.addAttribute("title", "Orders");
        return "admin/all-orders";
    }
}
