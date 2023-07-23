package com.zbro.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Comment;

import com.zbro.type.UserStatusType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ConsumerUser {

	@Comment("구매자_아이디")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long consumerId;
	
	@Comment("이메일")
	@Column(unique = true, nullable = false, length = 100)
	private String email;
	
	@Comment("이름")
	@Column(nullable = false, length = 50)
	private String name;
	
	@Comment("비밀번호")
	@Column(nullable = false)
	private String password;
	
	@Comment("프로필사진 파일명")
	private String profilePhoto;
	
	@Comment("상태")
	@Enumerated(EnumType.STRING)
	@Column(insertable = false, columnDefinition = "VARCHAR(10) DEFAULT 'ACTIVE'")
	private UserStatusType status;
	
	@Comment("등록일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;
	
}
