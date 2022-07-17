package com.abhishekinformatics.integrations.tuyaintegrations.dtos;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TuyaDeviceInfo implements Serializable {

	@JsonProperty("id")
	private String deviceId;

	@JsonProperty("ip")
	private String deviceIP;

	@JsonProperty("lat")
	private String deviceLatitude;

	@JsonProperty("lon")
	private String deviceLongitude;

	@JsonProperty("name")
	private String deviceName;

	@JsonProperty("online")
	private boolean deviceOnline;

	@JsonProperty("owner_id")
	private String homeOwnerId;

	@JsonProperty("product_name")
	private String productName;

	@JsonProperty("status")
	private List<TuyaDeviceDataPoint> status;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}

	public String getDeviceLatitude() {
		return deviceLatitude;
	}

	public void setDeviceLatitude(String deviceLatitude) {
		this.deviceLatitude = deviceLatitude;
	}

	public String getDeviceLongitude() {
		return deviceLongitude;
	}

	public void setDeviceLongitude(String deviceLongitude) {
		this.deviceLongitude = deviceLongitude;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public boolean isDeviceOnline() {
		return deviceOnline;
	}

	public void setDeviceOnline(boolean deviceOnline) {
		this.deviceOnline = deviceOnline;
	}

	public String getHomeOwnerId() {
		return homeOwnerId;
	}

	public void setHomeOwnerId(String homeOwnerId) {
		this.homeOwnerId = homeOwnerId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<TuyaDeviceDataPoint> getStatus() {
		return status;
	}

	public void setStatus(List<TuyaDeviceDataPoint> status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
