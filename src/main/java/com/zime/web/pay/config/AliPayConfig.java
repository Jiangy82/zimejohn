package com.zime.web.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by LJH on 2017/11/6.
 */

@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
	/**
	 * 支付宝网关
	 */
	private String openApiDomain;
    /**
     * 合作身份者ID，以2088开头由16位纯数字组成的字符串
     */
    private String partner;

    /**
     * 收款支付宝账号，以2088开头由16位纯数字组成的字符串
     */
    private String sellerId;

    /**
     * 商户的私钥
     */
    private String privateKey;

    /**
     * 支付宝的公钥，无需修改该值
     */
    private String aliPublicKey;

    /**
     * 支付宝支付key
     */
    private String aliPayKey;

    /**
     *
     */
    private String aliPayPid;

    /**
     * 支付宝消息验证地址
     */
    private String aliPayHttpsVerifyUrl;

    /**
     * 支付宝支付service 固定值
     */
    private String aliPayService;

    /**
     * 支付回调地址
     */
    private String notifyUrl;

    /**
     * 支付跳转地址
     */
    private String returnUrl;

    /**
     * APPID
     */
    private String appId;

    /**
     * MD5Key
     */
    public static String md5Key;

    public static final String CHAESET = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";
    //public static final String SIGN_TYPE = "RSA";



    public String getOpenApiDomain() {
		return openApiDomain;
	}

	public void setOpenApiDomain(String openApiDomain) {
		this.openApiDomain = openApiDomain;
	}

    public String getPartner() {
        return partner;
    }


	public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        this.aliPublicKey = aliPublicKey;
    }

    public String getAliPayKey() {
        return aliPayKey;
    }

    public void setAliPayKey(String aliPayKey) {
        this.aliPayKey = aliPayKey;
    }

    public String getAliPayPid() {
        return aliPayPid;
    }

    public void setAliPayPid(String aliPayPid) {
        this.aliPayPid = aliPayPid;
    }

    public String getAliPayHttpsVerifyUrl() {
        return aliPayHttpsVerifyUrl;
    }

    public void setAliPayHttpsVerifyUrl(String aliPayHttpsVerifyUrl) {
        this.aliPayHttpsVerifyUrl = aliPayHttpsVerifyUrl;
    }

    public String getAliPayService() {
        return aliPayService;
    }

    public void setAliPayService(String aliPayService) {
        this.aliPayService = aliPayService;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public static String getMd5Key() {
        return md5Key;
    }

    public static void setMd5Key(String md5Key) {
        AliPayConfig.md5Key = md5Key;
    }
}
