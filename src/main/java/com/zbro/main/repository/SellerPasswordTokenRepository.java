package com.zbro.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerPasswordToken;
import com.zbro.model.SellerPasswordToken;

public interface SellerPasswordTokenRepository extends JpaRepository<SellerPasswordToken, Long> {
	
}
