package com.zbro.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerUser;
import com.zbro.model.SellerUser;

public interface SellerUserRepository extends JpaRepository<SellerUser, Long> {

	Optional<SellerUser> findByEmail(String email);
}
