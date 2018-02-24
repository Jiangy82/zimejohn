package com.zime.web.controller.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;


@ApiModel(description="base64code方式上传图片")
public class UploadImageBase64RequestBean {
  @ApiModelProperty(value = "image", required = true)
  @NotBlank(message = "image不能为空")
  String image;

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
