package com.niwe.erp.common.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
	private final MailService mailService;
	public void sentEmail(String message) {
		
		String[] emailAddresses= {"niwe2026@gmail.com"};
		mailService.sendEmailsText(emailAddresses, "NIWE ERP NOFIFICATION", message);
		
	}

}
