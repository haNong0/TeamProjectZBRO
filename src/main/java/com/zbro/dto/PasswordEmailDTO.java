package com.zbro.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zbro.model.ConsumerPasswordToken;
import com.zbro.model.SellerPasswordToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordEmailDTO {
	private String name;
	private String email;
	private String token;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime expiredDateString;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime createDateString;

	public PasswordEmailDTO(ConsumerPasswordToken passwordToken) {
		this.name = passwordToken.getUser().getName();
		this.email = passwordToken.getUser().getEmail();
		this.token = passwordToken.getToken();
		this.expiredDateString = passwordToken.getExpiredDate();
		this.createDateString = passwordToken.getCreateDate();
	}
	
	public PasswordEmailDTO(SellerPasswordToken passwordToken) {
		this.name = passwordToken.getUser().getName();
		this.email = passwordToken.getUser().getEmail();
		this.token = passwordToken.getToken();
		this.expiredDateString = passwordToken.getExpiredDate();
		this.createDateString = passwordToken.getCreateDate();
	}
}
