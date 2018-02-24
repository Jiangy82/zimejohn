package com.zime.web.config;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 存储-配置
 * 
 * @author jzsong@uworks.cc
 */
@ConfigurationProperties(prefix = "oss")
@Component
public class OSSConfig {

  private static Logger logger = LoggerFactory.getLogger(OSSConfig.class);

  private String accessKeyId;
  private String accessKeySecret;
  private String uploadEndpoint;
  private String downloadEndpoint;
  private String bucketName;
  private String storagePath;
  private long downloadUrlExpiration;
  private String styleName;

  public String getAccessKeyId() {
    return accessKeyId;
  }

  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = accessKeyId;
  }

  public String getAccessKeySecret() {
    return accessKeySecret;
  }

  public void setAccessKeySecret(String accessKeySecret) {
    this.accessKeySecret = accessKeySecret;
  }

  public String getUploadEndpoint() {
    return uploadEndpoint;
  }

  public void setUploadEndpoint(String uploadEndpoint) {
    this.uploadEndpoint = uploadEndpoint;
  }

  public String getDownloadEndpoint() {
    return downloadEndpoint;
  }

  public void setDownloadEndpoint(String downloadEndpoint) {
    this.downloadEndpoint = downloadEndpoint;
  }

  public String getBucketName() {
    return bucketName;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public long getDownloadUrlExpiration() {
    return downloadUrlExpiration;
  }

  public void setDownloadUrlExpiration(long downloadUrlExpiration) {
    this.downloadUrlExpiration = downloadUrlExpiration;
  }

  public String getStoragePath() {
    return storagePath;
  }

  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }

  public String getStyleName() {
    return styleName;
  }

  public void setStyleName(String styleName) {
    this.styleName = styleName;
  }

  @PostConstruct
  public void init() {
    logger.debug(JSON.toJSONString(this));
  }

}
