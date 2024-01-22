package com.example.shopmax.dao;

import com.example.shopmax.models.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Integer> {

}
