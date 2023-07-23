package com.zbro.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerPasswordToken {

	@Comment("회원ID")
	@Id
	@Column(name = "user_id")
	private Long userId;
	
	@OneToOne
	@MapsId
	@JoinColumn(name="user_id")
	private SellerUser user;
	
	@Comment("비밀번호_변경_토큰")
	private String token;
	
	@Comment("만료일")
	@Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT date_add(current_timestamp(), interval 10 minute)")
	private LocalDateTime expiredDate;
	
	@Comment("생성일")
	@Column(insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
}
