package com.zbro.seller.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.seller.repository.SellerRoomFavoriteRepository;
import com.zbro.seller.repository.SellerRoomOptionRepository;
import com.zbro.seller.repository.SellerRoomOptionTypeRepository;
import com.zbro.seller.repository.SellerRoomPhotoRepository;
import com.zbro.seller.repository.SellerRoomRepository;

@Service
public class SellerRoomService {
	
	@Autowired
	private SellerRoomRepository roomRepo;
	@Autowired
	private SellerRoomOptionRepository roomOptionRepo;
	@Autowired
	private SellerRoomOptionTypeRepository roomOptionTypeRepo;
	@Autowired
	private SellerRoomPhotoRepository roomPhotoRepo;
	@Autowired
	private SellerRoomFavoriteRepository roomFavoriteRepo;

	public void insertRoom(Room room) {
		roomRepo.save(room);
	}

	public void insertRoomOption(RoomOption roomOption) {
		roomOptionRepo.save(roomOption);
	}

	public List<RoomOptionType> getRoomOptionType() {
		List<RoomOptionType> findOptionTypes = roomOptionTypeRepo.findAll();
		
		Collections.sort(findOptionTypes, Comparator.comparingInt(RoomOptionType::getSortOrder));
		
		return findOptionTypes;
	}

	public void insertRoomPhoto(RoomPhoto roomPhoto) {
		roomPhotoRepo.save(roomPhoto);
	}

	
	
	
	
	
	
    public Page<Room> getAllRoomsOrderedByRoomId(Long sellerId, Pageable pageable) {
        return roomRepo.findAllBySeller_SellerIdOrderByRoomId(sellerId, pageable);
    }

    public Page<Room> searchRoomsByTypeOrBuildingNameOrAddress(Long sellerId, List<String> types, String searchType, String searchKeyword, Pageable pageable) {
        List<Room> filteredRooms = new ArrayList<>();
        Page<Room> rooms = getAllRoomsOrderedByRoomId(sellerId, Pageable.unpaged());

        for (Room room : rooms.getContent()) {
            if (isMatched(room, types, searchType, searchKeyword)) {
                filteredRooms.add(room);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredRooms.size());
        return new PageImpl<>(filteredRooms.subList(start, end), pageable, filteredRooms.size());
    }

    private boolean isMatched(Room room, List<String> types, String searchType, String searchKeyword) {
        if (types != null && !types.isEmpty() && !types.contains(room.getType().toString())) {
            return false;
        }

        if (searchType != null && searchKeyword != null) {
            if (searchType.equals("type")) {
                return room.getType().toString().contains(searchKeyword);
            } else if (searchType.equals("buildingName")) {
                return room.getBuildingName().contains(searchKeyword);
            } else if (searchType.equals("address")) {
                return room.getAddress().contains(searchKeyword);
            }
        }

        return true;
    }
    
    @Transactional
    public void deleteRooms(List<Long> roomIds) {
        for (Long roomId : roomIds) {
            Room room = roomRepo.findById(roomId).orElse(null);
            if (room != null) {
                roomPhotoRepo.deleteByRoom(room); 
                roomOptionRepo.deleteByRoom(room); 
                roomFavoriteRepo.deleteByRoom(room); 
                roomRepo.deleteById(roomId);
            }
        }
    }
    


}