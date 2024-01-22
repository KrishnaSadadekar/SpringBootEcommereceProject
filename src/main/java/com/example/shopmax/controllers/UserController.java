package com.example.shopmax.controllers;

import com.example.shopmax.dao.*;
import com.example.shopmax.helper.Message;
import com.example.shopmax.models.*;
import com.example.shopmax.services.CartService;
import com.example.shopmax.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartService cartService;

    @Autowired
    BillingAddressRepository billingAddressRepository;

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserService userService;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    static  List<CartItem> cartItemList = new ArrayList<>();


     int cartSize;

    @GetMapping("/userpage")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String userPage(Model model,Principal principal) {
        model.addAttribute("title", "UserHome!");
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        cartItemList=cartItemRepository.findByUser(user).stream().toList();
        model.addAttribute("cartSize", cartItemList.size());
        model.addAttribute("user",user);
        return "user/index";
    }


    @PostMapping("/addCart/{id}")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String addtoCart(@PathVariable("id") Integer id, @ModelAttribute CartItem cartItem,
                            Principal principal) {

        System.out.println(cartItem.getSize() + "---");
        Product product = productRepository.findById(id).get();
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        cartService.addToCart(product, cartItem, user);

        System.out.println(cartItemRepository.findAll().stream().toList());
        return "redirect:/shop";
    }

    //Delete
    @GetMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String removeCartItem(@PathVariable("id") Integer cId, Model model, Principal principal, HttpSession session) throws IOException {

        CartItem cartItem = cartItemRepository.findById(cId).get();

        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        cartItemList=cartItemRepository.findByUser(user).stream().toList();
        model.addAttribute("cartSize",cartItemList.size());

        try {
            cartItemRepository.delete(cartItem);
            session.setAttribute("message", new Message("Product removed form Cart", "success"));

        } catch (Exception e) {
            System.out.println("Error:" + e);
            session.setAttribute("message", new Message("Failed to remove", "Failure"));
            e.printStackTrace();
        }


        return "redirect:/user/cart";
    }


    @GetMapping("/cart")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String displayAllProducts(Model model, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("title", "Cart Page!");

        List<CartItem> ls = cartItemRepository.findByUser(user);
        cartItemList = ls.stream().toList();
        BigDecimal total = ls.stream().map(CartItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("total", total);

        model.addAttribute("cartSize", cartItemList.size());
        model.addAttribute("cartList", ls);
        return "user/cart";
    }

//Handler

    @PostMapping("/process-cart")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String processCart(@RequestParam("totalPrice") BigDecimal amt, Principal principal, Model model) {

        System.out.println("total Amount is " + amt);
        return "redirect:/user/checkout";
    }


    //Address Form

    @GetMapping("/checkout")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String checkOut(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUserName(username);
        cartItemList = cartItemRepository.findByUser(user);
        model.addAttribute("cartSize", cartItemList.size());
        model.addAttribute("title", "CheckOut Page!");
        model.addAttribute("billingAddress", new BillingAddress());
        model.addAttribute("cartSize", cartSize);
        return "user/checkout";
    }

    @PostMapping("/process-checkout")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String processCheckOut(@ModelAttribute BillingAddress billingAddress, Principal principal) {
        String userName = principal.getName();
        User user = userRepository.getUserByUserName(userName);
        billingAddress.setUser(user);
        billingAddressRepository.save(billingAddress);
        Orders orders = new Orders();
        Orders result = cartService.placeOrder(cartItemList, billingAddress, user, orders);
        return "redirect:/user/place-order";
    }


    @GetMapping("/place-order")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String placeOrder(Model model, Principal principal) {
        model.addAttribute("title", "CheckOut Page!");
        model.addAttribute("billingAddress", new BillingAddress());
        String username=principal.getName();
        User user=userRepository.getUserByUserName(username);
        model.addAttribute("cartSize", cartItemRepository.findByUser(user).stream().toList().size());
        return "user/thankyou";
    }


//    To Show User Orders


    @GetMapping("/my-order")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String myOrder(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepository.getUserByUserName(username);
        List<Orders> myorder = orderRepository.findByUser(user).stream().toList();
        model.addAttribute("title", "OrderList!");
        model.addAttribute("OrderList", myorder);
        model.addAttribute("cartSize", cartItemRepository.findByUser(user).size());
        return "user/order-list";
    }

    //    Update Profile and Chenage Password
    @GetMapping("/update-profile")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String updateProfile(Model model,Principal principal) {
        String username=principal.getName();
        User user=userRepository.getUserByUserName(username);
        model.addAttribute("user", user);
        model.addAttribute("cartSize", cartSize);
        model.addAttribute("title", "UpdateProfile!");
        model.addAttribute("cartSize", cartItemRepository.findByUser(user).size());
        return "user/updateprofile";
    }

//---------------[Update Profile]------------------------------------

    @PostMapping("/process-user")
    @PreAuthorize("hasAuthority('Role_USER')")
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
            user.setRole("Role_USER");
            if (bResult.hasErrors()) {

                FieldError fieldError = bResult.getFieldError("name");
                if (fieldError != null) {
                    String errorMessage = fieldError.getDefaultMessage();
                    System.out.println(errorMessage);
                    return "user/updateprofile";
                }


            }
            User result=userService.updateUser(file,user,olduser);
            session.setAttribute("message", new Message("Updated Successfully", "Failure"));
        } catch (Exception e) {
            session.setAttribute("message", new Message("Failed to Update", "Failure"));
        }
        return "redirect:/user/update-profile";
    }


    //-------------------------------------------------
    @PostMapping("/process-password")
    @PreAuthorize("hasAuthority('Role_USER')")
    public String changepassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal) {
        User user = userRepository.getUserByUserName(principal.getName());
        System.out.println("Encoded Password: " + user.getPassword());
        System.out.println("Current Password" + currentPassword + " New Password: " + newPassword);

        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            System.out.println("Password updated ! "+newPassword);
            return "redirect:/user/update-profile";
        } else {
            return "user/updateprofile";
        }

    }



}
