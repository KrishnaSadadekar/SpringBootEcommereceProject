package com.example.shopmax.controllers;


import com.example.shopmax.dao.CartItemRepository;
import com.example.shopmax.dao.CategoryRepository;
import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.helper.Message;
import com.example.shopmax.models.CartItem;
import com.example.shopmax.models.Category;
import com.example.shopmax.models.Product;
import com.example.shopmax.models.User;
import com.example.shopmax.services.HomeServices;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Controller
public class Home {


    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HomeServices homeServices;

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("title", "LoginPage");
        return "signin";
    }

    @GetMapping("/home")
    public String home(Model model) {
//        model.addAttribute("title", "HomePage");
        return "index";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }



    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "AboutPage");
        return "about";
    }

    @GetMapping("/contact")
    public String ContactUs(Model model) {
        model.addAttribute("title", "ContactPage");

        return "contact";
    }

    //Sign Up
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "SignUp");
        model.addAttribute("user", new User());

        return "signup";
    }

// handler for form------------------------------

    @PostMapping("/doregister")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               HttpSession session,
                               Model model) {

        try {
            if (!agreement) {
                System.out.println("you have not agreed terms and conditions!");
                throw new Exception("you have not agreed terms and conditions!");

            }
            if(userRepository.existsByEmail(user.getEmail()))
            {

             throw new Exception(" Email id already existed!");

            }


            if (bindingResult.hasErrors()) {
                System.out.println("Eroror:->" + bindingResult.toString());
                model.addAttribute("user", user);
                return "signup";
            }
            user.setRole("Role_USER");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setImageUrl("defaultProduct.png");
            User result = this.userRepository.save(user);

            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Registration successfully", "alert-success"));
            return "signup";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }

    }

//    Product Display

    @GetMapping("/shop")
    public String displayAllProducts(Model model) {
        model.addAttribute("title", "Shop Now");
        List<Product> list=homeServices.displayProducts();
        List<Category> categoryList = categoryRepository.findAll().stream().toList();
        Map<Category,Integer>map=new HashMap<>();
        for (int i = 0; i <categoryList.size() ; i++) {
           List<Product>products= productRepository.findByCategory(categoryList.get(i));
            map.put(categoryList.get(i),products.size());
        }

        model.addAttribute("categories",map);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("products",list);
        return "shop";
    }

    //shop by category

    @GetMapping("/shop/{id}")
    public String displayProductByCategory(@PathVariable("id")Integer id,Model model)
    {
        Category category=categoryRepository.findById(id).get();
        List<Product>products=productRepository.findByCategory(category);


        List<Category> categoryList = categoryRepository.findAll().stream().toList();

        Map<Category,Integer>map=new HashMap<>();
        for (int i = 0; i <categoryList.size() ; i++) {
            List<Product>productsList= productRepository.findByCategory(categoryList.get(i));
            map.put(categoryList.get(i),productsList.size());
        }

        model.addAttribute("categories",map);
        model.addAttribute("products",products);
        model.addAttribute("title", "shop-"+category.getCategoryName());
        return "shop";

    }
    //shop-single
    @GetMapping("/shop-single/{id}")
    public String shopSingle(@PathVariable("id")Integer id, Model model) {

        Product product=homeServices.productsDetails(id);
        model.addAttribute("title", "Shop Now");
        model.addAttribute("product", product);
        model.addAttribute("cartItem",new CartItem());
        return "shop-single";
    }

    //Add to cart handler


}
