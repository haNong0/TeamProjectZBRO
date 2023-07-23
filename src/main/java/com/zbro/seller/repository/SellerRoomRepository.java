package com.zbro.seller.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.SellerUser;

public interface SellerRoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findAllBySeller_SellerIdOrderByRoomId(Long sellerId, Pageable pageable);

   
}