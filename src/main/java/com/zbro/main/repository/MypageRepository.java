package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;



public interface MypageRepository extends JpaRepository<Favorite, Long> {
	
    List<Favorite> findByUserConsumerId(Long consumerId);
       

    
}
