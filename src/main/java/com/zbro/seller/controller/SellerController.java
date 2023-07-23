package com.zbro.seller.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.zbro.main.service.UserService;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.model.RoomPhoto;
import com.zbro.model.SellerUser;
import com.zbro.seller.service.SellerRoomService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SellerController {
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	private UserService sellerUserService;
	
	@Autowired
	private SellerRoomService roomService;
	
	
	@Value("${file.images.room-photo}")
	public String uploadFolder;
	
	@Value("${file.biz}")
	private String fileBizPath;
	
	
	
	
	@GetMapping("/seller")
	public String sellerMainPage() {
		
		return "seller/index";
	}
	
	/**
	 * 판매자 로그인 성공했을때 오는 매핑
	 * 사이드바에 유저 정보를 항시 표출하기 위해 세션에 정보 추가
	 * */
	@GetMapping("/seller/login/success")
	public String sellerLoginSuccess(HttpSession session, Principal principal) {
		SellerUser seller = sellerUserService.getSellerUserByEmail(principal.getName());
		
		session.setAttribute("sellerId", seller.getSellerId());
		session.setAttribute("sellerName", seller.getName());
		session.setAttribute("sellerIsAdmission", seller.isAdmission());
		
		return "redirect:/seller";
	}
	
	
	// 매물등록 페이지 들어가기.
	@GetMapping("/room_add")
	public String roomAddView(Model model, RoomOptionType roomOptionType) {
		List<RoomOptionType> optionTypes = roomService.getRoomOptionType();
		model.addAttribute("optionTypes", optionTypes);
		
		return "seller/room_add";
	}

	// 매물 등록하기
	@PostMapping("/room_add")
	public String roomAdd(@RequestParam("isRoomIn") boolean isRoomIn, 
						  @RequestParam("isElevator") boolean isElevator,
						  @RequestParam(value = "optionType", required = false) List<String> optionTypes,
						  @RequestParam(value = "uploadFile", required = false) List<MultipartFile> files,
						  Room room) throws Exception, IOException {
		
		// room insert
		room.setRoomIn(isRoomIn);
		room.setElevator(isElevator);
		roomService.insertRoom(room);
		
		// room insert 결과 확인
//		System.out.println("등록된 room id : " + room.getRoomId());
		
		// 선택한 옵션 확인
//		if (optionTypes != null) {
//	        for (String optionType : optionTypes) {
//	            System.out.println("선택한 옵션: " + optionType.toString());
//	        }
//	    }
		
		// RoomOption insert
		if(optionTypes != null) {
			for (String optionType : optionTypes) {
				RoomOption roomOption = new RoomOption();
				RoomOptionType roomOptionType = new RoomOptionType();
				roomOptionType.setOptionType(optionType);
				roomOption.setRoom(room);
				roomOption.setOptionType(roomOptionType);
				roomService.insertRoomOption(roomOption);
			}
		}
		
		// 업로드한 파일 확인
		for (MultipartFile file : files) {
		    System.out.println("업로드한 파일 확인 : " + file.getOriginalFilename());
		}
		
		
		if(!files.isEmpty()) {	//파일을 업로드했다면
			int imgCnt = 1;
			
			for(MultipartFile file:files) {
				System.out.println(imgCnt);
				// 지정폴더에 파일을 실제 업로드 // ex)room1_파일명.파일형식
				String fileName = "room"+room.getRoomId()+"_"+file.getOriginalFilename();
				file.transferTo(new File(uploadFolder+fileName));
				
				// 테이블 데이터 set
				RoomPhoto roomPhoto = new RoomPhoto();
				roomPhoto.setFileName(fileName);
				roomPhoto.setRoom(room);
				roomPhoto.setImageSeq(imgCnt);
				
				// img insert
				roomService.insertRoomPhoto(roomPhoto);
				imgCnt++;
			}
//			imgCnt = 1;
		}
		
		
		
		return "redirect:add_test";
	}
	
	
	

	@GetMapping("/add_test")
	public String test() {
		return "seller/add_test";
	}
	
	
	@GetMapping("/seller/user")
	public String sellerUserView(Principal principal, Model model){
		
		
		SellerUser sellerUser = sellerUserService.getSellerUserByEmail(principal.getName());
		model.addAttribute("sellerUser", sellerUser);
		
		return "seller/user/detail";
	}
	
	@PostMapping("/seller/user/update")
	public String sellerUserUpdate(Principal principal
									, @RequestParam("profilePhotoFile") MultipartFile profilePhotoFile
									, @RequestParam("bizScanFile") MultipartFile bizScanFile
									, @RequestParam("isBiz") String isBizString
									, SellerUser sellerUser
									, HttpSession session
									) throws IllegalStateException, IOException {
		
		
		SellerUser findedSellerUser = sellerUserService.getSellerUserByEmail(principal.getName());
		
		//isBiz String -> boolean
		sellerUser.setBiz("true".equalsIgnoreCase(isBizString));
		
		log.info("#### sellerUserUpdate : {}", sellerUser);
		
		if(profilePhotoFile.isEmpty() == false) {
			sellerUserService.profileImageDelete(findedSellerUser);
			String profileImageFilename = sellerUserService.profileImageSave(profilePhotoFile);
			sellerUser.setProfilePhoto(profileImageFilename);
		}
		
		if(bizScanFile.isEmpty() == false && sellerUser.isBiz()) {
			sellerUserService.bizFileDelete(findedSellerUser);
			String bizFilename = sellerUserService.bizFileSave(bizScanFile);
			sellerUser.setBizFile(bizFilename);
		} else if(sellerUser.isBiz() == false) {
			sellerUserService.bizFileDelete(findedSellerUser);
		}
		
		SellerUser savedSeller = sellerUserService.updateSellerUser(findedSellerUser.getSellerId(), sellerUser);
		
		//사이드바 업데이트
		session.setAttribute("sellerName", savedSeller.getName());
		
		return "redirect:/seller/user";
	}
	
	
	
	@GetMapping("/download/seller/bizfile")
	public ResponseEntity<Resource> downloadBizFile(SellerUser user) {
		SellerUser sellerUser = sellerUserService.getSellerUser(user.getSellerId());
		
		try {
			Resource resource =  resourceLoader.getResource("file:/"+fileBizPath + sellerUser.getBizFile());
			File file = resource.getFile();
			
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName())
					.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
					.body(resource);
			
		} catch (IOException e) {
			 return ResponseEntity.badRequest().body(null);
		}
		
	}


		
	
	
	

	@GetMapping("/seller/room/list")
	public String roomList(Model model,
	                       @RequestParam(value = "searchType", required = false) String searchType,
	                       @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
	                       @RequestParam(value = "type", required = false) List<String> types,
	                       @RequestParam(defaultValue = "0") int page,
	                       @RequestParam(defaultValue = "10") int size) {

	    Long sellerId = 1L;
	    Pageable pageable = PageRequest.of(page, size);

	    Page<Room> rooms;
	    if ((searchType != null && searchKeyword != null) || (types != null && !types.isEmpty())) {
	        rooms = roomService.searchRoomsByTypeOrBuildingNameOrAddress(sellerId, types, searchType, searchKeyword, pageable);
	    } else {
	        rooms = roomService.getAllRoomsOrderedByRoomId(sellerId, pageable);
	    }

	    model.addAttribute("rooms", rooms);
	    model.addAttribute("searchKeyword", searchKeyword);
	    model.addAttribute("searchType", searchType);
	    model.addAttribute("types", types);

	    return "seller/roomList";
	}
	
	
	@PostMapping("/seller/room/delete")
	public ResponseEntity<Void> deleteRooms(@RequestBody List<Long> roomIds) {
	    System.out.println(" roomIds: " + roomIds);

	    roomService.deleteRooms(roomIds);

	    return ResponseEntity.ok().build();
	}
	
	
	
}