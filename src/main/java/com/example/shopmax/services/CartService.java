package com.example.shopmax.services;

import com.example.shopmax.dao.CartItemRepository;
import com.example.shopmax.dao.OrderRepository;
import com.example.shopmax.dao.ProductRepository;
import com.example.shopmax.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    public CartItem addToCart(Product product, CartItem cartItem, User user) {
        int quantity = cartItem.getQuantity();
        String size = cartItem.getSize();
        System.out.println("In Add to Cart");
        CartItem presentCartItem = cartItemRepository.findByProductAndUser(product, user, size);

        System.out.println("Aft present" + cartItem.getSize());
        if (presentCartItem != null) {
            System.out.println("If Loop->" + presentCartItem.getSize());


            System.out.println("CartItem Id: " + presentCartItem.getId());
            quantity += presentCartItem.getQuantity();
            presentCartItem.setQuantity(quantity);

            BigDecimal qty = BigDecimal.valueOf(quantity);
            BigDecimal amt = presentCartItem.getProduct().getPrice().multiply(qty);
            presentCartItem.setAmount(amt);

            return cartItemRepository.save(presentCartItem);

        } else {
            System.out.println("else Loop");
            cartItem.setUser(user);
            cartItem.setPrice(product.getPrice());
            cartItem.setImageUrl(product.getImageUrl());
            cartItem.setProduct(product);

            BigDecimal qty = BigDecimal.valueOf(cartItem.getQuantity());
            BigDecimal amt = product.getPrice().multiply(qty);
            cartItem.setAmount(amt);
            System.out.println(cartItem.getSize() + "-----step3");
            return cartItemRepository.save(cartItem);
        }


    }

    public Orders placeOrder(List<CartItem> ls, BillingAddress billingAddress, User user, Orders order) {
        BigDecimal total = ls.stream().map(CartItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setDateTime(LocalDateTime.now());
        List<Product> product = new ArrayList<>();
        for (CartItem c : ls) {
            product.add(c.getProduct());
            Product pro=productRepository.findById(c.getProduct().getId()).get();
            pro.setStockQuantity(pro.getStockQuantity()-c.getQuantity());
            productRepository.save(pro);
            cartItemRepository.delete(c);

        }
        order.setProducts(product);
        order.setPrice(total);
        order.setUser(user);
        order.setBillingAddress(billingAddress);
        return orderRepository.save(order);
    }
}
