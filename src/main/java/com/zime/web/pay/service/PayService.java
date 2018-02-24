package com.zime.web.pay.service;

import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zime.web.pay.config.PayStatus;
import com.zime.web.pay.config.request.AliPayRefundRequestBean;
import com.zime.web.pay.config.request.AlipayRequestBean;

/**
 * Created by LJH on 2017/11/6.
 */
public interface PayService {



    /////////////////////////////////////支付宝支付////////////////////////////////////////////

    /**
     * 获得阿里支付实例
     *
     * @return AlipayClient
     */
    AlipayClient getAlipayClient ();


    /**
     * 支付宝条码支付
     * @param paybean
     * @return  1成功  小于等于0  失败
     */
    PayStatus aliPayBarCode(AlipayRequestBean paybean);


    /**
     * 支付宝交易查询
     * @param outTradeNo 商户唯一交易编号
     * @param tradeNo 支付宝唯一交易编号
     * @return 交易实体类
     */
    AlipayTradeQueryResponse aliPayQueryTrade(String outTradeNo,String tradeNo);


    /**
     * 支付宝交易退款
     * @param bean
     * @return  */
    PayStatus aliPayTradeRefund(AliPayRefundRequestBean bean);


    /**
     * 支付宝交易关闭
     * @param out_trade_no
     * @param trade_no
     * @return
     */
    PayStatus aliPaytradeCancel(String out_trade_no, String trade_no);

    /**
     * 支付宝交易关闭查询
     * @param outTradeNo
     * @param tradeNo
     * @param outRequestNo 商户唯一取消编号
     * @return
     */
    AlipayTradeFastpayRefundQueryResponse aliPayTradeRefundQuery(String outTradeNo,String tradeNo,String outRequestNo);





}
