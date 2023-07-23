package com.zbro.dto;

import org.springframework.mail.SimpleMailMessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO {
	private String to;
	private String from;
	private String subject;
	private String content;
	
	public void setSubject(String subject) {
		this.subject = "[지브로] "+subject;
	}
	
	public SimpleMailMessage toSimpleMailMessage() {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(this.to);
		mailMessage.setFrom(this.from);
		mailMessage.setSubject(this.subject);
		mailMessage.setText(this.content);
		
		return mailMessage;
	}
	
}
