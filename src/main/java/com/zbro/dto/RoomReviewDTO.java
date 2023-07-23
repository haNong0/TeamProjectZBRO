package com.zbro.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zbro.model.RoomReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomReviewDTO {
	
	/* private ConsumerUser user; */
	private int score;
	private LocalDateTime createDate;
	private String content;
	private Long roomId;
	// 여기까지 RoomReview에서 불러온 field
	
	 private String profilePhoto; 
	 private String username;
	//여기까지 ConsumerUser에서 불러온 field
	
	public RoomReviewDTO (RoomReview roomReview) {    
		  this.score= roomReview.getScore();
		  this.createDate = roomReview.getCreateDate(); 
		  this.content = roomReview.getContent();
		  this.roomId = roomReview.getRoom().getRoomId();
		  this.profilePhoto = roomReview.getUser().getProfilePhoto();
		  this.username = roomReview.getUser().getName();
		  }
	
	
	//stream사용
	
	public List<RoomReviewDTO> convertToDTOList(List<RoomReview> roomReviewList) {
        return roomReviewList.stream()
                .map(RoomReviewDTO::new)
                .collect(Collectors.toList());
    }
	
	
			
	
	
}
