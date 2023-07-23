package com.zbro.seller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Favorite;
import com.zbro.model.Room;

public interface SellerRoomFavoriteRepository extends JpaRepository<Favorite, Long>  {
	
	void deleteByRoom(Room room);
}
