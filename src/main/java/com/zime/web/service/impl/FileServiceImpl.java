package com.zime.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.zime.web.service.FileService;
import com.zime.web.util.FTPUtil;

@Service
public class FileServiceImpl implements FileService {
	
	private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);

	public String upload(MultipartFile file, String path) {
		String fileName= file.getOriginalFilename();
		
		String fileExtensionName= fileName.substring(fileName.lastIndexOf(".")+1);
		String uploadFileName= UUID.randomUUID().toString()+"."+fileExtensionName;
		logger.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}",fileName,path,uploadFileName);
		
		File fileDir= new File(path);
		if( !fileDir.exists() ) {
			fileDir.setWritable(true);
			fileDir.mkdirs();
		}
		File targetFile= new File(path,uploadFileName);
		
		try {
			file.transferTo(targetFile);
			//todo  将targetFile上传到FTP服务器
			FTPUtil.uploadFile(Lists.newArrayList(targetFile));
			//todo 上传完之后，删除upload下的文件
			targetFile.delete();
		} catch (IOException e) {
			logger.error("上传文件异常",e);
			return null;
		}
		
		return null;
	}
}
