package com.abhishekinformatics.iftttintegrations.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IFTTTWebhookController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/hello")
	public void hello() {
		logger.info("hello endpoint hit");
	}
}
