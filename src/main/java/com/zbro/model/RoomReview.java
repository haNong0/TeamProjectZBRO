package com.zbro.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import lombok.Data;

@Data
@Entity
public class RoomReview {
	
	@Comment("리뷰_id")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	
	@Comment("리뷰_내용(한줄평)")
	@Lob
	private String content;
	
	@Comment("작성자_id")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private ConsumerUser user;
	
	@Comment("매물_번호")
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@Comment("평점")
	private int score;
	
	@Comment("등록일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
}
