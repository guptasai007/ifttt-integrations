package com.abhishekinformatics.iftttintegrations.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IFTTTWebhookController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/webhook")
	public void postEvent(@RequestBody String body, @RequestHeader MultiValueMap<String, String> headers) {
		logger.info("body: {}", body);
		logger.info("header: {}", headers);
	}
}
