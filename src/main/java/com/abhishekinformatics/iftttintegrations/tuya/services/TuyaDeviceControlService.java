package com.abhishekinformatics.iftttintegrations.tuya.services;

import org.springframework.stereotype.Service;

import com.abhishekinformatics.iftttintegrations.tuya.dtos.TuyaDeviceCommand;
import com.abhishekinformatics.iftttintegrations.tuya.dtos.TuyaDeviceInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public interface TuyaDeviceControlService {

	TuyaDeviceInfo getDeviceInfoByDeviceId(String deviceId);

	boolean sendDeviceCommandWithDeviceId(String deviceId, TuyaDeviceCommand command) throws JsonProcessingException;
}
