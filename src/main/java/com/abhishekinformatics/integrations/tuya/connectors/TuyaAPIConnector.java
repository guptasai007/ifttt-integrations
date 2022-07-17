package com.abhishekinformatics.integrations.tuya.connectors;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.abhishekinformatics.integrations.tuya.constants.TuyaIntegrationConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TuyaAPIConnector {

	@Value("${tuya.client.id}")
	private String tuyaClientId;

	@Value("${tuya.client.secret}")
	private String tuyaClientSecret;

	@Value("${tuya.client.url}")
	private String tuyaClientBaseURL;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	Marker marker = MarkerFactory.getMarker("Tuya-Integration-API-Request-Utils");

	public <T> T fetchTuyaAPIResponse(String apiEndpointPath, HttpMethod method, Map<String, String> uriParams,
			String body, String accessToken, Class<T> clazz, String responseType) {
		logger.info(marker, "Fetching {} from Tuya client API", responseType);
		final String currentMillis = String.valueOf(System.currentTimeMillis());
		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(tuyaClientBaseURL + apiEndpointPath).build();
		URI requestURI = MapUtils.isNotEmpty(uriParams) ? uriComponents.expand(uriParams).toUri()
				: uriComponents.toUri();
		MultiValueMap<String, String> headers = new HttpHeaders();

		RequestEntity<String> request = StringUtils.isNotBlank(body)
				? new RequestEntity<>(body, headers, method, requestURI)
				: new RequestEntity<>(headers, method, requestURI);

		headers.set(TuyaIntegrationConstants.CLIENT_ID_HEADER_KEY, tuyaClientId);
		headers.set(TuyaIntegrationConstants.SIGN_HEADER_KEY,
				calculateTuyaRequestSignature(request, accessToken, currentMillis));
		headers.set(TuyaIntegrationConstants.TIMESTAMP_HEADER_KEY, currentMillis);
		headers.set(TuyaIntegrationConstants.SIGN_METHOD_HEADER_KEY, TuyaIntegrationConstants.SIGN_METHOD_HEADER_VALUE);

		if (StringUtils.isNotBlank(accessToken)) {
			headers.set(TuyaIntegrationConstants.ACCESS_TOKEN_KEY, accessToken);
		}

		ResponseEntity<JsonNode> response = restTemplate.exchange(request, JsonNode.class);
		logger.info(marker, "Fetched response from {} API: {}", responseType, response.getBody().toPrettyString());
		
		if (response.getStatusCode() != HttpStatus.OK || !response.getBody().findValue("success").asBoolean()) {
			logger.error(marker, "Something went wrong while fetching {} from Tuya API\n{}", responseType,
					response.getBody().toPrettyString());
			return null;
		}
		return objectMapper.convertValue(response.getBody().findValue("result"), clazz);
	}

	public String calculateTuyaRequestSignature(RequestEntity<String> request, String accessToken,
			String currentMillis) {

		String signature = "";
		List<String> stringToSign = new ArrayList<>();
		String bodyHash = TuyaIntegrationConstants.EMPTY_BODY_HASH;
		
		signature+=tuyaClientId;
		
		if(StringUtils.isNotBlank(accessToken))
		{
			signature+=accessToken;
		}
		signature+=currentMillis;
		
		stringToSign.add(request.getMethod().toString().toUpperCase());

		if (request.hasBody()) {
			bodyHash = DigestUtils.sha256Hex(request.getBody());
		}

		stringToSign.add(bodyHash);
		stringToSign.add(""); // adding blank line for Signature-Headers element as its not being used in
								// signature calculation
		URI requestURI = request.getUrl();
		stringToSign
				.add(StringUtils.isNotBlank(requestURI.getQuery()) ? requestURI.getPath() + "?" + requestURI.getQuery()
				: requestURI.getPath());

		String signatureString = StringUtils.join(stringToSign, "\n");
		signature = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, tuyaClientSecret).hmacHex(signature + signatureString);
			
		return signature.toUpperCase();
	}
}
