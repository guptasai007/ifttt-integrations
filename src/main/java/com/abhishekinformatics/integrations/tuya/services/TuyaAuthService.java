package com.abhishekinformatics.integrations.tuya.services;

import org.springframework.stereotype.Service;

@Service
public interface TuyaAuthService {

	String getAccessTokenForTuyaClient();
}
