package com.zime.web.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;


public interface OssService {

  /**
   * oss上传文件，返回文件访问路径
   * @param file：文件
   * @return
   */
  String upload(MultipartFile file);

  /**
   * base64code方式上传
   * @param imageBytes 文件流
   * @return
   */
  String uploadImageBase64(byte[] imageBytes);

  /**
   *File方式上传
   * @param file 文件
   * @return
   */
  String uploadFile(File file);

  /**
   * 下载文件
   * @param url
   * @return
   */
	byte[] download(String url);

}
