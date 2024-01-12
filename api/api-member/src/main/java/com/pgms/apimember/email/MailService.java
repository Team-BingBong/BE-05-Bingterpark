package com.pgms.apimember.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.common.MemberErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

	private final JavaMailSender emailSender;

	public void sendEmail(
		String toEmail,
		String title,
		String text) {
		SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
		try {
			emailSender.send(emailForm);
		} catch (RuntimeException e) {
			throw new MemberException(MemberErrorCode.EMAIL_SEND_FAIL);
		}
	}

	private SimpleMailMessage createEmailForm(
		String toEmail,
		String title,
		String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setSubject(title);
		message.setText(text);

		return message;
	}
}
