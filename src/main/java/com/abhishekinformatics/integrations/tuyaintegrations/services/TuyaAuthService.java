package com.abhishekinformatics.integrations.tuyaintegrations.services;

import org.springframework.stereotype.Service;

@Service
public interface TuyaAuthService {

	String getAccessTokenForTuyaClient();
}
