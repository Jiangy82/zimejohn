package com.zime.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {
	/**
	 * 服务器IP
	 */
	private String serverIp;
	/**
	 * HTTP访问前缀
	 */
	private String serverHttpPrefix;
	/**
	 * FTP登录名
	 */
	private String user;
	/**
	 * FTP密码
	 */
	private String pass;
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getServerHttpPrefix() {
		return serverHttpPrefix;
	}
	public void setServerHttpPrefix(String serverHttpPrefix) {
		this.serverHttpPrefix = serverHttpPrefix;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	@Override
	public String toString() {
		return "FtpConfig [serverIp=" + serverIp + ", serverHttpPrefix=" + serverHttpPrefix + ", user=" + user
				+ ", pass=" + pass + "]";
	}
	
	

}
