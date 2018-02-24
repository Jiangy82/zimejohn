package com.zime.web.pay.config.request;

/**
 * Created by LJH on 2017/10/9.
 */
public class AliPayQueryRequestBean {
    private  String  merchantCode;//商户订单号   二选一
    private String  alipayCode;//支付宝订单号


    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getAlipayCode() {
        return alipayCode;
    }

    public void setAlipayCode(String alipayCode) {
        this.alipayCode = alipayCode;
    }
}
