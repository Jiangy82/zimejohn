package com.zime.web.service;


import com.zime.web.contants.SmsConstants;
import com.zime.web.controller.request.CaptchaRequestBean;
import com.zime.web.controller.response.CaptchaResponseBean;

/**
 * @Title: MobileCaptchaService.java
 * @Package cc.uworks.library.service
 * @author liyuchang
 * @Description: 短信验证码发送、验证
 * @date 2017年1月23日
 * @version V1.0
 */
public interface MobileCaptchaService {

  /**
   * 发送验证码
   * @param bean
   * @return
   */
  CaptchaResponseBean send(CaptchaRequestBean bean);

  /**
   * 验证码验证
   * @param mobile 手机号
   * @param captcha 验证码
   * @param type 类型:1注册,2修改密码,3找回密码
   */
  boolean verify(String mobile, String captcha, SmsConstants.SmsCaptchaType type);
}
