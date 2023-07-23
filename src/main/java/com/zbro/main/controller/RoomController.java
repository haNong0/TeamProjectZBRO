package com.zbro.main.controller;

import java.io.FileNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zbro.dto.RoomReviewDTO;
import com.zbro.dto.RoomSearchDTO;
import com.zbro.main.service.FavoriteService;
import com.zbro.main.service.RoomService;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomPhoto;
import com.zbro.model.RoomReview;

import lombok.extern.slf4j.Slf4j;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.service.RoomService;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.SellerUser;

@Slf4j
@Controller
@RequestMapping("/room")
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private FavoriteService favService;
	
	
	
	@GetMapping("/search")
	public String searchView(RoomSearchDTO roomDTO, Model model) {
		Long userId = 1L;
		List<RoomSearchDTO> roomDTOList = roomService.searchRoomAndFavorite(roomDTO, userId);
		
		model.addAttribute("rooms", roomDTOList);
		model.addAttribute("roomSearchDTO", roomDTO);
		
		return "main/room/search";
	}
	
	@RequestMapping("/detail/{roomId}")
	public String detailView(@PathVariable("roomId") Long roomId,RoomReviewDTO ReviewDTO,Model model) {
	    Room room = roomService.findById(roomId);
	    SellerUser selleruser = new SellerUser();
	    selleruser.setSellerId(room.getSeller().getSellerId());
	    ConsumerUser csuser = new ConsumerUser();
	    csuser.setConsumerId(1L);
	    List<Room> roomsame = roomService.findBySellerId(selleruser);
	    List<RoomOption> roomOption = roomService.getroomOption(room);
	    List<RoomPhoto> roomPhotoList = roomService.getRoomPhtotList(roomId);
	    List<RoomReview> roomReviewList = roomService.getRoomReview(ReviewDTO,roomId);
		Optional<Favorite> roomDetailFavorite = favService.getFavoriteDetail(csuser , room);
		/*
		 * List<RoomReviewDTO> roomReviewDTOList =
		 * RoomReviewDTO.convertToDTOList(roomReviewList);
		 */
	    model.addAttribute("room", room);
	    model.addAttribute("roomsame", roomsame);
	    model.addAttribute("roomOptions", roomOption);
	    model.addAttribute("roomPhotoList",roomPhotoList);
	    model.addAttribute("roomReviews",roomReviewList);
	    model.addAttribute("isFavorite",roomDetailFavorite.isPresent());
	    if(roomDetailFavorite.isPresent()) {
	    	model.addAttribute("roomDetailFavorite",roomDetailFavorite.get());
	    }
	    return "main/room/detail";
	}
	
	
	@PostMapping("/favorite")
	@ResponseBody
	public ResponseEntity<?> doFavorite(Room Room) {
		
		log.info("doFavorite = {}", Room);
		Favorite fav = new Favorite();
		
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(1L);
		fav.setUser(user);
		fav.setRoom(Room);
		
		Favorite savedFav = favService.favorite(fav);
		
		return ResponseEntity.ok().body(savedFav.getFavoriteId());
	}
	
	
	@PostMapping("/unfavorite")
	@ResponseBody
	public ResponseEntity<?> doUnfavorite(Favorite fav) {
		
		log.info("doUnfavorite = {}", fav);
		
		favService.unfavorite(fav);
		return ResponseEntity.ok().body("success");
	}
	
	
	
	@GetMapping("/photo")
	public ResponseEntity<Resource> getRoomPhotoImage(Room room) throws FileNotFoundException {
		
		RoomPhoto findedRoomPhoto = roomService.getRoomPhotoOne(room);
		
		Resource imageResource = roomService.getImageResource(findedRoomPhoto);
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
	}
	

//	// 상세페이지 사진 업로드 구현
	@GetMapping("/photodetail")
	public ResponseEntity<Resource> getDetailRoomPhotoImg(RoomPhoto roomphoto) throws FileNotFoundException {
		
		RoomPhoto findedRoomPhoto = roomService.getRoomPhoto(roomphoto);
		
		Resource imageResource = roomService.getImageResource(findedRoomPhoto);
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
	}
	
	@PostMapping("/review")
	public String postRoomReview(@RequestParam("roomId") Long roomId,RoomReview roomReview) {
		
		Room room = new Room();
		room.setRoomId(roomId);
		roomReview.setRoom(room);
		roomService.saveRoomReview(roomReview); 
		
	
		return "redirect:/room/detail/"+roomId;
	
		
	}	
	
	
}
