package com.abhishekinformatics.integrations.tuya.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.abhishekinformatics.integrations.tuya.dtos.TuyaDeviceCommand;
import com.abhishekinformatics.integrations.tuya.dtos.TuyaDeviceDataPoint;
import com.abhishekinformatics.integrations.tuya.vo.TuyaLightDeviceCommandVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TuyaDeviceCommandUtils {

	public static TuyaDeviceCommand getDeviceCommandFromLightCommandVo(TuyaLightDeviceCommandVO lightCommandVo,
			ObjectMapper objectMapper) {
		List<TuyaDeviceDataPoint> deviceStateToWrite = new ArrayList<>();
		Map<String, Object> lightCommandMap = objectMapper.convertValue(lightCommandVo,
				new TypeReference<Map<String, Object>>() {
				});
		lightCommandMap.entrySet().forEach(
				dataPoint -> deviceStateToWrite.add(new TuyaDeviceDataPoint(dataPoint.getKey(), dataPoint.getValue())));
		TuyaDeviceCommand deviceCommand = new TuyaDeviceCommand();
		deviceCommand.setCommands(deviceStateToWrite);
		return deviceCommand;
	}

	public static TuyaLightDeviceCommandVO getLightCommandFromDeviceDataPoints(
			List<TuyaDeviceDataPoint> deviceDataPoints, ObjectMapper objectMapper) {
		Map<String, Object> deviceStateMap = deviceDataPoints.stream()
				.collect(Collectors.toMap(TuyaDeviceDataPoint::getCode, TuyaDeviceDataPoint::getValue));
		return objectMapper.convertValue(deviceStateMap, TuyaLightDeviceCommandVO.class);
	}
}
