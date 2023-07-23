package com.zbro.main.repository;



import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerUser;

public interface ConsumerUserRepository extends JpaRepository<ConsumerUser, Long> {

	
	ConsumerUser findByConsumerId(Long consumerId);
	
	ConsumerUser save(ConsumerUser consumerUser);
	
	Optional<ConsumerUser> findByEmail(String email);

}
