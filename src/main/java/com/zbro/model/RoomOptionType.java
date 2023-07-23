package com.zbro.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Comment;

import lombok.Data;

@Data
@Entity
public class RoomOptionType {
	
	@Comment("옵션_타입")
	@Id
	@Column(length = 20)
	private String optionType;
	
	@Comment("유형_이름")
	@Column(nullable = false, length = 20)
	private String optionName;
	
	@Comment("공용_여부")
	private boolean isShare;
	
	@Comment("정렬_순서")
	private int sortOrder;
}

