package com.abhishekinformatics.integrations.tuyaintegrations.services.impl;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.abhishekinformatics.integrations.tuyaintegrations.connectors.TuyaAPIConnector;
import com.abhishekinformatics.integrations.tuyaintegrations.dtos.TuyaDeviceCommand;
import com.abhishekinformatics.integrations.tuyaintegrations.dtos.TuyaDeviceInfo;
import com.abhishekinformatics.integrations.tuyaintegrations.dtos.TuyaLightDeviceInfo;
import com.abhishekinformatics.integrations.tuyaintegrations.services.TuyaAuthService;
import com.abhishekinformatics.integrations.tuyaintegrations.services.TuyaDeviceControlService;
import com.abhishekinformatics.integrations.tuyaintegrations.services.constants.TuyaIntegrationConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TuyaLightDeviceControlServiceImpl implements TuyaDeviceControlService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	Marker marker = MarkerFactory.getMarker("Tuya-Integration-Light-Control-Service");

	@Autowired
	private TuyaAPIConnector apiConnector;

	@Autowired
	private TuyaAuthService tuyaAuthService;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${tuya.device.info.endpoint}")
	private String deviceInfoEndpointPath;

	@Value("${tuya.device.command.endpoint}")
	private String deviceCommandEndpointPath;

	@Override
	public TuyaDeviceInfo getDeviceInfoByDeviceId(String deviceId) {
		String accessToken = tuyaAuthService.getAccessTokenForTuyaClient();
		logger.info(marker, "Getting smart light device info for deviceId {}", deviceId);
		Map<String, String> requestURIParam = Collections.singletonMap(TuyaIntegrationConstants.DEVICE_ID_URI_PARAM, deviceId);
		TuyaDeviceInfo deviceInfo = apiConnector.fetchTuyaAPIResponse(deviceInfoEndpointPath, HttpMethod.GET,
				requestURIParam, null, accessToken, TuyaLightDeviceInfo.class, "device info");
		logger.info(marker, "Received device info for deviceId {} successfully\n{}", deviceInfo);
		return deviceInfo;
	}

	@Override
	public boolean sendDeviceCommandWithDeviceId(String deviceId, TuyaDeviceCommand command)
			throws JsonProcessingException {
		String accessToken = tuyaAuthService.getAccessTokenForTuyaClient();
		String commandsAsJson = objectMapper.writeValueAsString(command);
		logger.info(marker, "Sending device commands for smart light device for deviceId {}, commands: {}", deviceId,
				commandsAsJson);
		Map<String, String> requestURIParam = Collections.singletonMap(TuyaIntegrationConstants.DEVICE_ID_URI_PARAM,
				deviceId);
		Boolean commandSuccess = apiConnector.fetchTuyaAPIResponse(deviceCommandEndpointPath, HttpMethod.POST,
				requestURIParam, commandsAsJson, accessToken, Boolean.class, "send command");
		if (commandSuccess)
			logger.info("Device commands successfully sent to smart light device with deviceId {}, commands: {}",
					deviceId, commandsAsJson);
		else
			logger.error(
					"Something went wrong while sending device commands to smart light device with deviceId {}, commands: {}",
					deviceId, commandsAsJson);
		return commandSuccess;
	}

}
