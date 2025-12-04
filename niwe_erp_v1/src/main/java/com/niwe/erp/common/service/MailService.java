package com.niwe.erp.common.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

	private final JavaMailSender javaMailSender;
	@Value("${mangatek.mail.sender}")
	private String emailSender;
	public void sendEmailText(String emailAddress, String subject, String message) {
		SimpleMailMessage msg = new SimpleMailMessage();
		log.debug("========Sending email:{} to:{}  ", message, emailAddress);
		msg.setFrom(emailSender);
		msg.setTo(emailAddress);
		msg.setSubject(subject);
		msg.setText(message);
		javaMailSender.send(msg);

	}

	public void sendEmailWithAttachment(String emailAddress, String subject, String message, String url) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();

			// true = multipart message
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setFrom(emailSender);
			helper.setTo(emailAddress);

			helper.setSubject(subject);
			// true = text/html
			helper.setText("<h1>WELCOME TO KEZA LEARNING PLATFORM</h1><br> Your password:" + message
					+ " <br>For Login Click:<a href='" + url + "'>KEZA CORE UI</a>", true);

			// helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendEmailsWithAttachment(String[] emailAddresses, String subject, String message) {
		try {
			MimeMessage msg = javaMailSender.createMimeMessage();

			// true = multipart message
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			helper.setTo(emailAddresses);
			helper.setFrom(emailSender);
			helper.setSubject(subject);
			// true = text/html
			helper.setText("<h1>MANGATEK EMAIL</h1><br>" + message + " <br>", true);

			// helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendEmailsText(String[] emailAddresses, String subject, String message) {
		try {
			SimpleMailMessage msg = new SimpleMailMessage();
			log.debug("========Sending email:{} from {} to:{}  ", message, "emailSender", emailAddresses);
			msg.setFrom(emailSender);
			msg.setTo(emailAddresses);
			msg.setSubject(subject);
			msg.setText(message);
			javaMailSender.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String sendHTMLEmailWithInlineImage(String to) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject("Here's your pic");
		helper.setFrom(emailSender);
		helper.setTo(to);

		String content = "<b>Dear guru</b>,<br><i>Please look at this nice picture:.</i>"
				+ "<br><img src='cid:image001'/><br><b>Best Regards</b>";
		helper.setText(content, true);

		FileSystemResource resource = new FileSystemResource(
				new File("g:\\MyEbooks\\Freelance for Programmers\\images\\admiration.png"));
		helper.addInline("image001", resource);

		javaMailSender.send(message);

		return "result";
	}

	public String sendHTMLEmailWithAttachment(String to) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject("Here's your e-book");
		helper.setFrom(emailSender);
		helper.setTo(to);

		helper.setText("<b>Dear friend</b>,<br><i>Please find the book attached.</i>", true);

		FileSystemResource file = new FileSystemResource(
				new File("g:\\MyEbooks\\Freelance for Programmers\\SuccessFreelance-Preview.pdf"));
		helper.addAttachment("FreelanceSuccess.pdf", file);

		javaMailSender.send(message);

		return "result";
	}

	public String sendHTMLEmail(String to) throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setSubject("This is an HTML email");
		helper.setFrom(emailSender);
		helper.setTo(to);

		boolean html = true;
		helper.setText("<b>Hey guys</b>,<br><i>Welcome to my new home</i>", html);

		javaMailSender.send(message);

		return "result";
	}

	public void sendExcelFile(byte[] reportContent, String subject, String text, String fileName,
			String[] emailAddresses) {
		MimeMessage message = javaMailSender.createMimeMessage();

		try {
			log.debug("======Sending email to:{}", String.join(", ", emailAddresses));
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(emailSender);
			helper.setTo(emailAddresses);
			helper.setSubject(subject);
			helper.setText(text);

			helper.addAttachment(fileName + ".xlsx", new ByteArrayResource(reportContent));

			javaMailSender.send(message);
		} catch (MessagingException e) {
			log.debug("sendExcelFile:{}", e);
		}

	}
}
