package com.abhishekinformatics.iftttintegrations.controllers;

import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.abhishekinformatics.iftttintegrations.tuya.constants.TuyaIntegrationConstants;
import com.abhishekinformatics.iftttintegrations.tuya.dtos.TuyaDeviceInfo;
import com.abhishekinformatics.iftttintegrations.tuya.services.TuyaAuthService;
import com.abhishekinformatics.iftttintegrations.tuya.services.impl.TuyaLightDeviceControlServiceImpl;
import com.abhishekinformatics.iftttintegrations.tuya.utils.TuyaDeviceCommandUtils;
import com.abhishekinformatics.iftttintegrations.tuya.vo.TuyaLightDeviceCommandVO;
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
	public void postEvent(@RequestBody Map<String, String> requestMap,
			@RequestHeader MultiValueMap<String, String> headers) throws JsonProcessingException {
		tuyaAuthService.getAccessTokenForTuyaClient();
		// Device Id "70087286f4cfa2e73bf8"
		TuyaDeviceInfo deviceInfo = tuyaLightControlService
				.getDeviceInfoByDeviceId(headers.getFirst(TuyaIntegrationConstants.DEVICE_ID_URI_PARAM));
		TuyaLightDeviceCommandVO lightDeviceCommand = TuyaDeviceCommandUtils
				.getLightCommandFromDeviceDataPoints(deviceInfo.getStatus(), objectMapper);
		logger.info("Testing deviceDataPoint conversion to Light command {}", lightDeviceCommand);
		lightDeviceCommand
				.setSwitchLed(BooleanUtils.toBoolean(requestMap.get(TuyaIntegrationConstants.SWITCH_LED_COMMAND)));
		lightDeviceCommand.setWorkMode(requestMap.get(TuyaIntegrationConstants.CHANGE_WORK_MODE_COMMAND));
		tuyaLightControlService.sendDeviceCommandWithDeviceId(
				headers.getFirst(TuyaIntegrationConstants.DEVICE_ID_URI_PARAM),
				TuyaDeviceCommandUtils.getDeviceCommandFromLightCommandVo(lightDeviceCommand, objectMapper));
	}
}
