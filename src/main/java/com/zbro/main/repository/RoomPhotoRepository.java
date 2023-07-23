package com.zbro.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;

public interface RoomPhotoRepository extends JpaRepository<RoomPhoto, Long> {
    
    Optional<RoomPhoto> findByRoomAndImageSeq(Room room, int seq);

	List<RoomPhoto> findByRoomRoomId(Long roomId);
}
