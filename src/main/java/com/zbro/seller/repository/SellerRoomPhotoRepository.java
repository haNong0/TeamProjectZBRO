package com.zbro.seller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;

public interface SellerRoomPhotoRepository extends JpaRepository<RoomPhoto, Long>{
	
	 void deleteByRoom(Room room);
}
