package com.abhishekinformatics.iftttintegrations.tuya.services;

import org.springframework.stereotype.Service;

@Service
public interface TuyaAuthService {

	String getAccessTokenForTuyaClient();
}
