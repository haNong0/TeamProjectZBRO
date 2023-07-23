package com.zbro.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;

public interface FavoritRepository extends JpaRepository<Favorite, Long>{
	
	Optional<Favorite> findByUserAndRoom(ConsumerUser user, Room room);
}
