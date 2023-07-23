package com.zbro.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
@Entity
public class RoomPhoto {
	@Comment("매물_사진_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long photoId;

	@Comment("파일_이름")
	private String fileName;
	
	@Comment("매물_id")
	@ManyToOne
	@JoinColumn(name="room_id", nullable = false)
	private Room room;
	
	@Comment("이미지_순서")
	@ColumnDefault("1")
	private int imageSeq;
	
	@Transient
	private MultipartFile uploadFile;
	
}
