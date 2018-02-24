package com.zime.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.zime.web.config.FtpConfig;

@EnableConfigurationProperties
public class FTPUtil {
	private static Logger logger= LoggerFactory.getLogger(FTPUtil.class);
	
	@Autowired
	private static FtpConfig ftpConfig;
	
	private String ip;
	private int port;
	private String user;
	private String pwd;
	private FTPClient ftpClient;
	
	public FTPUtil(String ip, int port, String user, String pwd) {
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.pwd = pwd;
	}

	public static boolean uploadFile(List<File> fileList) throws IOException {
		
		FTPUtil ftpUtil=new FTPUtil(ftpConfig.getServerIp(), 21, ftpConfig.getUser(), ftpConfig.getPass());
	    logger.info("开始连接FTP服务器");
	    boolean result= ftpUtil.uploadFile("img", fileList);
	    logger.info("开始连接FTP服务器，结束上传，上传结果：{}");
		return result;
	}
	
	public void testFTPConfig() {
		logger.info(ftpConfig.getServerIp());
	}
	
	private boolean uploadFile(String remotepath, List<File> fileList) throws IOException {
		boolean uploaded=true;
		FileInputStream fis=null;
		if(connectServer(this.ip, this.port, this.user, this.pwd)) {
			
			try {
				ftpClient.changeWorkingDirectory(remotepath);
				ftpClient.setBufferSize(1024);
				ftpClient.setControlEncoding("UTF-8");
				ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
				ftpClient.enterLocalPassiveMode();
				for(File fileItem : fileList) {
					fis= new FileInputStream(fileItem);
					ftpClient.storeFile(fileItem.getName(), fis);
				}
				
			} catch (IOException e) {
				logger.error("上传文件失败",e);
				uploaded=false;
				e.printStackTrace();
			}finally {
				fis.close();
				ftpClient.disconnect();
			}
			
		}
		return uploaded;
	}
	
	private boolean connectServer(String ip, int port, String user, String pwd) {
		boolean isSuccess=false;
		ftpClient= new FTPClient();
		try {
			ftpClient.connect(ip);
			isSuccess= ftpClient.login(user, pwd);
		}  catch (IOException e) {
			logger.error("连接FTP服务器异常",e);
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public FTPClient getFtpClient() {
		return ftpClient;
	}
	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}
	
	

}
