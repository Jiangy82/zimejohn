package com.zime.web.pay.config.request;

import java.math.BigDecimal;

/**
 * 支付宝退款请求
 * Created by LJH on 2017/10/9.
 */
public class AliPayRefundRequestBean {

    //商户订单号   二选一
    private String outTradeNo;
    //支付宝订单号
    private String  tradeNo;

    //退款金额  不能大于订单金额   单位元  支持两位小数
    private BigDecimal refundAmount;


    /**
     * 以下选填
     */


    //退款原因
    private String refundReason;
    //标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
    private String outRequestNo;
    //商户的操作员编号
    private String operatorId;
    //商户的门店编号
    private String storeId;
    //商户的终端编号
    private String terminalId;




    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
