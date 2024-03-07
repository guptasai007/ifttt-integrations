package com.abhishekinformatics.iftttintegrations.tuya.services.impl;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.abhishekinformatics.iftttintegrations.tuya.connectors.TuyaAPIConnector;
import com.abhishekinformatics.iftttintegrations.tuya.constants.TuyaIntegrationConstants;
import com.abhishekinformatics.iftttintegrations.tuya.services.TuyaAuthService;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class TuyaAuthServiceImpl implements TuyaAuthService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	Marker marker = MarkerFactory.getMarker("Tuya-Integration-Auth-Service");

	@Value("${tuya.access.token.endpoint}")
	private String accessTokenEndpointPath;

	@Autowired
	private TuyaAPIConnector apiConnector;

	private LocalTime accessTokenExpireTime;
	private String cachedAccessToken;

	@Override
	public String getAccessTokenForTuyaClient() {
		LocalTime currentTime = LocalTime.now();

		logger.info(marker, "Getting Tuya client access token");
		if (StringUtils.isNotBlank(this.cachedAccessToken) && currentTime.isBefore(accessTokenExpireTime)) {
			logger.info(marker, "Fetched access token from cache: {} with expire time {}", this.cachedAccessToken,
					this.accessTokenExpireTime);
			return this.cachedAccessToken;
		}
		logger.info(marker, "Access token not present in cache, fetching through API");
		return getAccessTokenForTuyaClientFromAPI();
	}

	private String getAccessTokenForTuyaClientFromAPI() {
		
		JsonNode accessTokenNode = apiConnector.fetchTuyaAPIResponse(accessTokenEndpointPath, HttpMethod.GET, null,
				null, null, JsonNode.class, "access token");
		String accessToken = accessTokenNode.findValue(TuyaIntegrationConstants.ACCESS_TOKEN_KEY).asText();
		Long expiresIn = accessTokenNode.findValue(TuyaIntegrationConstants.EXPIRES_IN_KEY).asLong();
		
		if (StringUtils.isNotBlank(accessToken)) {
			this.cachedAccessToken = accessToken;
			this.accessTokenExpireTime = LocalTime.now().plus(expiresIn, ChronoUnit.SECONDS);

			logger.info(marker, "Access token {} fetched through API and saved in cache with expire time {}", accessToken, expiresIn);
		}
		else {
			logger.error(marker, "Something went wrong while fetching access token from Tuya API\n{}",
					accessTokenNode.toPrettyString());
			return null;
		}
		
		return accessToken;
	}


}
