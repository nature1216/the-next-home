package com.ssafy.email.service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PasswordResetEmailService implements EmailService {
	
	private final JavaMailSender mailSender;
	private final TemplateEngine templateEngine;

	private final static String SUBJECT = "비밀번호 초기화 메일입니다.";
	private final static String URL_HEAD = "http://localhost:8080/auth/password-reset-verification?";
	
	@Value("${spring.mail.username}")
	private static String sender;

	@Override
	public String send(String email) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		Context context = new Context();
		
		String uuid = generateUUID();
		String url = URL_HEAD + "email=" + email + "&uuid=" + uuid;
		context.setVariable("baseUrl", URL_HEAD);
		context.setVariable("email", email);
		context.setVariable("uuid", uuid);
		
		message.addRecipients(MimeMessage.RecipientType.TO, email);
		message.setSubject(SUBJECT);
		message.setFrom(sender);
		message.setText(templateEngine.process("passwordReset", context), "utf-8", "html");
		
		mailSender.send(message);
		
		return uuid;
		
	}
	
	private String generateUUID() {
		return UUID.randomUUID().toString();
	}

}
