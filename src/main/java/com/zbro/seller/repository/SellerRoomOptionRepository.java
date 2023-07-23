package com.zbro.seller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;

public interface SellerRoomOptionRepository extends JpaRepository<RoomOption, Long>{

	 void deleteByRoom(Room room);
}
