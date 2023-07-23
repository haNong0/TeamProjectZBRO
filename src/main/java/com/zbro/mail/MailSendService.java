package com.zbro.mail;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zbro.dto.EmailDTO;
import com.zbro.dto.PasswordEmailDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendService{
	
	private final JavaMailSender javaMailSender;
	private final TemplateEngine templateEngine;
	
	@Value("${mail.sender.address}")
	private String fromEmailAddress;
	
	/**
	 * Thymeleaf Template을 사용하여 이메일을 전송
	 * 
	 * @param emailDTO : content필드 제외 모두 필수 입력(메일 주소, 제목)
	 * @param template : 사용할 Thymeleaf Template 위치(ex:mail/password)
	 * @param dto : Thymeleaf Template에서 사용할 DTO
	 * @throws MessagingException 
	 */
	@Async
	public void sendTemplateMail(EmailDTO emailDTO, String template, Object dto) throws MessagingException {
		
		Context context = dtoToContext(dto);
		String htmlTemplate = templateEngine.process(template, context);
		
		MimeMessage mime = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeHelper = new MimeMessageHelper(mime, true, "UTF-8");
		mimeHelper.setSubject(emailDTO.getSubject());
		mimeHelper.setTo(emailDTO.getTo());
		mimeHelper.setFrom(fromEmailAddress);
		mimeHelper.setText(htmlTemplate, true);
		mimeHelper.addInline("logo-image", new ClassPathResource("static/images/navi_logo.png"));
		javaMailSender.send(mime);
	}
	
	/**
	 * 일반 텍스트 메일 발송
	 * 
	 * EmailDTO에 모든 필드를 사용
	 * @param emailDTO
	 */
	@Async
	public void sendMail(EmailDTO emailDTO) {
		javaMailSender.send(emailDTO.toSimpleMailMessage());
	}
	
	
	/**
	 * DTO를 Context로 변환
	 * @param dto
	 * @return
	 */
	private Context dtoToContext(Object dto) {
		
		//dtoObject -> Map
		Map objectMap = new ObjectMapper().convertValue(dto, Map.class);
		log.info("Map : {}", objectMap);
		
		Context result = new Context();
		result.setVariables(objectMap);
		
		return result;
	}
	
	
	public void sendPasswordChangeMail(PasswordEmailDTO passwordEmailDTO) throws MessagingException {
		EmailDTO emailDTO = new EmailDTO();
		
		emailDTO.setTo(passwordEmailDTO.getEmail());
		emailDTO.setSubject("비밀번호 안내 메일");
		
		
		log.info("######## sendPasswordChangeMail : {}", passwordEmailDTO);
		sendTemplateMail(emailDTO, "mail/password", passwordEmailDTO);
	}
	
	
}
