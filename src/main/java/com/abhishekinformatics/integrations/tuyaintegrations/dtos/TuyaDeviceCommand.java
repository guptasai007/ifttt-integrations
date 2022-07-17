package com.abhishekinformatics.integrations.tuyaintegrations.dtos;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TuyaDeviceCommand implements Serializable {

	private List<TuyaDeviceDataPoint> commands;

	public List<TuyaDeviceDataPoint> getCommands() {
		return commands;
	}

	public void setCommands(List<TuyaDeviceDataPoint> commands) {
		this.commands = commands;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}
