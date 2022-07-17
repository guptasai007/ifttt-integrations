package com.abhishekinformatics.integrations.controllers;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.abhishekinformatics.integrations.tuyaintegrations.dtos.TuyaDeviceCommand;
import com.abhishekinformatics.integrations.tuyaintegrations.dtos.TuyaDeviceDataPoint;
import com.abhishekinformatics.integrations.tuyaintegrations.services.TuyaAuthService;
import com.abhishekinformatics.integrations.tuyaintegrations.services.impl.TuyaLightDeviceControlServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class IFTTTWebhookController {

	@Autowired
	private TuyaAuthService tuyaAuthService;

	@Autowired
	private TuyaLightDeviceControlServiceImpl tuyaLightControlService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/webhook")
	@Async
	public void postEvent(@RequestBody String body, @RequestHeader MultiValueMap<String, String> headers)
			throws JsonProcessingException {
		logger.info("body: {}", body);
		logger.info("header: {}", headers);
		tuyaAuthService.getAccessTokenForTuyaClient();
		tuyaAuthService.getAccessTokenForTuyaClient();
		tuyaAuthService.getAccessTokenForTuyaClient();
		tuyaAuthService.getAccessTokenForTuyaClient();
		tuyaLightControlService.getDeviceInfoByDeviceId("70087286f4cfa2e73bf8");

		TuyaDeviceCommand deviceCommand = new TuyaDeviceCommand();
		deviceCommand.setCommands(Collections.singletonList(new TuyaDeviceDataPoint("switch_led", false)));
		tuyaLightControlService.sendDeviceCommandWithDeviceId("70087286f4cfa2e73bf8", deviceCommand);
	}
}
