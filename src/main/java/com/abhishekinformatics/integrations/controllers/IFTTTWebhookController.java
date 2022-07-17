package com.abhishekinformatics.integrations.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.abhishekinformatics.integrations.tuya.dtos.TuyaDeviceInfo;
import com.abhishekinformatics.integrations.tuya.services.TuyaAuthService;
import com.abhishekinformatics.integrations.tuya.services.impl.TuyaLightDeviceControlServiceImpl;
import com.abhishekinformatics.integrations.tuya.utils.TuyaDeviceCommandUtils;
import com.abhishekinformatics.integrations.tuya.vo.TuyaLightDeviceCommandVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class IFTTTWebhookController {

	@Autowired
	private TuyaAuthService tuyaAuthService;

	@Autowired
	private TuyaLightDeviceControlServiceImpl tuyaLightControlService;

	@Autowired
	ObjectMapper objectMapper;

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
		TuyaDeviceInfo deviceInfo = tuyaLightControlService.getDeviceInfoByDeviceId("70087286f4cfa2e73bf8");
		TuyaLightDeviceCommandVO lightDeviceCommand = TuyaDeviceCommandUtils
				.getLightCommandFromDeviceDataPoints(deviceInfo.getStatus(), objectMapper);
		logger.info("Testing deviceDataPoint conversion to Light command {}", lightDeviceCommand);
		lightDeviceCommand.setSwitchLed(true);
		tuyaLightControlService.sendDeviceCommandWithDeviceId("70087286f4cfa2e73bf8",
				TuyaDeviceCommandUtils.getDeviceCommandFromLightCommandVo(lightDeviceCommand, objectMapper));
	}
}
