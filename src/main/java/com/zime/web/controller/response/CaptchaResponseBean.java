package com.zime.web.controller.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 5分钟内有效，1分钟内不能重复发送相同类型短信验证码
 * @date 2017年1月23日
 * @version V1.0
 */
@ApiModel(description="短信验证码返回参数")
public class CaptchaResponseBean {

  @ApiModelProperty(value = "有效时间(单位秒)")
  private int validSeconds;

  @ApiModelProperty(value = "剩余发送冷却时间(单位秒)")
  private int coolSeconds;

  public int getValidSeconds() {
    return validSeconds;
  }

  public void setValidSeconds(int validSeconds) {
    this.validSeconds = validSeconds;
  }

  public int getCoolSeconds() {
    return coolSeconds;
  }

  public void setCoolSeconds(int coolSeconds) {
    this.coolSeconds = coolSeconds;
  }

}
