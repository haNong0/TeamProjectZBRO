package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.SellerUser;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

public interface RoomRepository extends JpaRepository<Room, Long>{
	
	List<Room> findRoomsByAddressContaining(String searchWord);
	List<Room> findRoomsByTypeAndAddressContaining(RoomType type, String searchWord);
	List<Room> findRoomsByCostTypeAndAddressContaining(CostType costType, String searchWord);
	List<Room> findRoomsByTypeAndCostTypeAndAddressContaining(RoomType type, CostType costType, String searchWord);
	List<Room> findBySeller(SellerUser selleruser);

}
