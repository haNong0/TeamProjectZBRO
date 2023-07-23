package com.zbro.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Comment;

import lombok.Data;

@Data
@Entity

public class Favorite {

	
	@Comment("찜_id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long favoriteId;
	
	@Comment("회원_id")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private ConsumerUser user;
	
	@Comment("매물_번호")
	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;
	
	@Comment("찜_메모")
	private String memo;
	
	@Comment("등록일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdDate;
	
}
