package com.example.shopmax.helper;

import com.example.shopmax.dao.UserRepository;
import com.example.shopmax.models.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserRepository userRepository;


    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        User user=userRepository.getUserByUserName(email);
        if(user!=null){
        return false;
        }else
        {
            return true;
        }
    }



}
