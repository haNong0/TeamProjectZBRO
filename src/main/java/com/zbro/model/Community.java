package com.zbro.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import com.zbro.type.PostType;

import lombok.Data;

@Data
@Entity
public class Community {
	
	@Comment("글_번호")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long postId;
	
	@Comment("커뮤니티_유형")
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private PostType type;
	
	@Comment("카테고리_유형")
	@Column(length = 20)
	private String categoryType;
	
	@Comment("글_제목")
	@Column(length = 100)
	private String title;
	
	@Comment("글_내용")
	@Lob
	private String content;
	
	@Comment("작성자")
	@ManyToOne
	@JoinColumn(name="user_id")
	private ConsumerUser user;
	
	@Comment("조회수")
	@ColumnDefault("0")
	private int viewCount;
	
	@Comment("등록일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	@Comment("수정일")
	@Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updateDate;
	
}
