package com.zime.web.cache;

import java.util.Date;

/**
 * 验证码存储结构
 */
public class MobileCaptchaCache {

  /**
   * 验证码值
   */
  private String captcha;

  /**
   * 发送时间(用于验证是否发送频率太快)
   */
  private Date sentTime;

  public String getCaptcha() {
    return captcha;
  }

  public void setCaptcha(String captcha) {
    this.captcha = captcha;
  }

  public Date getSentTime() {
    return sentTime;
  }

  public void setSentTime(Date sentTime) {
    this.sentTime = sentTime;
  }

}
