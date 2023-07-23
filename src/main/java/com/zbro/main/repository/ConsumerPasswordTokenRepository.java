package com.zbro.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerPasswordToken;

public interface ConsumerPasswordTokenRepository extends JpaRepository<ConsumerPasswordToken, Long> {
	
}
