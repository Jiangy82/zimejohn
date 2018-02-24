package com.zime.web.controller;

import com.zime.web.contants.Errors;
import com.zime.web.controller.request.CaptchaRequestBean;
import com.zime.web.controller.response.CaptchaResponseBean;
import com.zime.web.exception.BusinessException;
import com.zime.web.service.MobileCaptchaService;
import com.zime.web.util.ExceptionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description: 工具controler
 * @date 2016年11月19日
 * @version V1.0
 */
@Api(description = "公共模块", produces = "application/json")
@RestController
@RequestMapping("/common")
public class CommonController {

  @Resource
  private MobileCaptchaService mobileCaptchaService;



  /**
   * 发送验证码
   * 
   * @param bean
   * @return
   */
  @ApiOperation(
      value = "发送短信验证码",
      notes = "type类型：1注册,2修改密码,3重置密码,4注册+登陆,5绑定卡<br/>有效时间5分钟，相同类型发送冷却时间1分钟<br/>返回：code=0成功；code=1手机号有误;code=2未超过发送冷却时间，exception=剩余发送冷却时间(单位秒)；code=3送失败请稍后再试")
  @RequestMapping(value = "/sms/sendCaptcha", method = RequestMethod.POST)
  public ResponseEntity<CaptchaResponseBean> sendCaptcha(@Valid @RequestBody CaptchaRequestBean bean) {
      CaptchaResponseBean result=null;
      try {
       result = mobileCaptchaService.send(bean);
        } catch (BusinessException e) {
        ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code,e.toString());
      }
      return ResponseEntity.ok(result);
  }

}
