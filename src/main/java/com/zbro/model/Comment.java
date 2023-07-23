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

import org.hibernate.annotations.ColumnDefault;
import lombok.Data;

@Data
@Entity
public class Comment {

	@org.hibernate.annotations.Comment("댓글_번호")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long commentId;
	
	@org.hibernate.annotations.Comment("댓글_내용")
	@Lob
	private String content;
	
	@org.hibernate.annotations.Comment("작성자_id")
	@ManyToOne
	@JoinColumn(name="user_id")
	private ConsumerUser user;
	
	@org.hibernate.annotations.Comment("게시글_id")
	@ManyToOne
	@JoinColumn(name="post_id")
	private Community post;
	
	@org.hibernate.annotations.Comment("부모_댓글_id")
	@ManyToOne
	@JoinColumn(name="parent_id")
	private Comment parent;
	
	@org.hibernate.annotations.Comment("댓글_유형")
	@ColumnDefault("0")
	private int commentType;
	
	@org.hibernate.annotations.Comment("작성일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
	@org.hibernate.annotations.Comment("수정일")
	@Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updateDate;
	
}
