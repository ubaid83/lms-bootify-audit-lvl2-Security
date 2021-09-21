package com.spts.lms.helpers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("mailService")
public class ApplicationMailer {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage preConfiguredMessage;

	/**
	 * This method will compose and send the message
	 * */
	@Async
	public void sendMail(final String to, final String subject, final String body) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}

	/**
	 * This method will send a pre-configured message
	 * */
	public void sendPreConfiguredMail(final String message) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
		mailMessage.setText(message);
		mailSender.send(mailMessage);
	}
	
	/**
	 * This method will compose and send a dynamic message using {@link java.lang.String.format}
	 * */
	public void sendMail(final String to, final String subject, final String body, final Object[] arguments) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(String.format(body,arguments));
		mailSender.send(mailMessage);
	}
	
	/**
	 * This method will compose and send a dynamic message using {@link java.lang.String.format}
	 * */
	public void sendMailWithAttachments(final String to, final String subject, final String body, final Object[] arguments, final String[] attachmentPaths) {
		final MimeMessage message = mailSender.createMimeMessage();
		try {
			final MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(String.format(body,arguments));
			for(String filePath : attachmentPaths){
				final FileSystemResource config = new FileSystemResource(filePath);
				helper.addAttachment(config.getFilename(), config);
			}
		} catch (MessagingException e) {
			throw new MailParseException(e);
		}
		

		mailSender.send(message);
	}
}
