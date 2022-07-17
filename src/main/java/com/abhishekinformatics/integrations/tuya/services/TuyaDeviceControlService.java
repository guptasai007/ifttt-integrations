package com.abhishekinformatics.integrations.tuya.services;

import org.springframework.stereotype.Service;

import com.abhishekinformatics.integrations.tuya.dtos.TuyaDeviceCommand;
import com.abhishekinformatics.integrations.tuya.dtos.TuyaDeviceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public interface TuyaDeviceControlService {

	TuyaDeviceInfo getDeviceInfoByDeviceId(String deviceId);

	boolean sendDeviceCommandWithDeviceId(String deviceId, TuyaDeviceCommand command) throws JsonProcessingException;
}
