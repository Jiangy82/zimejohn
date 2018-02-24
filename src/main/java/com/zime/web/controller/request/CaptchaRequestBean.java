package com.zime.web.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @Description: 短信验证码参数
 * @date 2017年1月23日
 * @version V1.0
 */
@ApiModel(description="短信验证码发送参数")
public class CaptchaRequestBean {

  @ApiModelProperty(value = "手机号", required = true)
  @NotBlank(message = "手机号不能为空")
  private String mobile;

  @ApiModelProperty(value = "类型：1注册,2修改密码,3重置密码，4注册+登陆", required = true)
  @NotNull(message = "验证码类型不能为空")
  private Byte type;

  public String getMobile() {
    return mobile.replaceAll(" ", "");
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public Byte getType() {
    return type;
  }

  public void setType(Byte type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "CaptchaRequestBean [mobile=" + mobile + ", type=" + type + "]";
  }
  
}
