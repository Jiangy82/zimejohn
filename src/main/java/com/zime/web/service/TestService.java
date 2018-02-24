package com.zime.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zime.web.config.FtpConfig;

@Service
public class TestService {

	@Autowired
	private FtpConfig ftpConfig;
	
	public String connectTest() {
		return ftpConfig.getServerHttpPrefix()+ftpConfig.getServerIp();
	}
}
