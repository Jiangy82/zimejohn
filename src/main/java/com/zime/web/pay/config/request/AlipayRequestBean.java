package com.zime.web.pay.config.request;

import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by LJH on 2017/11/6.
 */
public class AlipayRequestBean {

    @ApiModelProperty(value = "订单ID")
    @NotBlank(message = "订单ID不能为空")
    private String orderId;

    @ApiModelProperty(value = "门店编号")
    @NotBlank(message = "门店编号不能为空")
    private String shopid;

    @ApiModelProperty(value = "商品描述：APP——需传入应用市场上的APP名字-实际商品名称（例如：天天爱消除-游戏充值）")
    @NotBlank(message = "商品描述不能为空")
    private String body;

    @ApiModelProperty(value = "在公共参数中设置页面回跳地址，wap支付需要填写")
    private String returnUrl;

    @ApiModelProperty(value = "authcode,二维码内容主动扫码支付时必填")
    private  String authcode;

    // 订单总金额，整形，此处单位为元，精确到小数点后2位，不能超过1亿元
    // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
    @ApiModelProperty(value = "totalAmount,总金额主动扫码支付时必填")
    private  String totalAmount;
     
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    
}
