package com.example.shopmax.Configuration;

import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
public class UserInfoService implements UserDetailsService {

@Autowired
private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user= Optional.ofNullable(userRepository.getUserByUserName(username));
        return user.map(UserInfoConfig::new).orElseThrow(()->new UsernameNotFoundException("User Does Not Exist"));
    }
}
