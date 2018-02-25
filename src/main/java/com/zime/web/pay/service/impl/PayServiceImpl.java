package com.zime.web.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.zime.web.pay.config.AliPayConfig;
import com.zime.web.pay.config.PayStatus;

import com.zime.web.pay.config.request.AliPayRefundRequestBean;
import com.zime.web.pay.config.request.AlipayRequestBean;
import com.zime.web.pay.service.PayService;




import javax.annotation.Resource;

import org.springframework.stereotype.Service;


/**
 * Created by LJH on 2017/11/6.
 */
@Service
public class PayServiceImpl implements PayService {

    @Resource
    private AliPayConfig alipayConfig;



    @Override
    public AlipayClient getAlipayClient() {
        // 阿里支付公共请求参数
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getOpenApiDomain(),
                alipayConfig.getAppId(), alipayConfig.getPrivateKey(), "json", AliPayConfig.CHAESET,
                alipayConfig.getAliPublicKey(), AliPayConfig.SIGN_TYPE);

        return alipayClient;
    }
    //预下单，生成二维码
    @Override
    public PayStatus aliPayBarCode(AlipayRequestBean payBean) {

        // 实例化具体API对应的request类
        AlipayTradePayRequest appPayRequest = new AlipayTradePayRequest();// 创建API对应的request类
        
        JSONObject model = new JSONObject();
        model.put("out_trade_no", payBean.getOrderId());
        model.put("scene", "bar_code");
        model.put("auth_code", payBean.getAuthcode());
        model.put("subject", payBean.getBody());
        model.put("store_id", alipayConfig.getSellerId());
        model.put("total_amount", payBean.getTotalAmount());
        //超时时间2分钟
        model.put("timeout_express", "2m");

        appPayRequest.setBizContent(model.toString());

        AlipayTradePayResponse response = null;
        try {

            AlipayClient alipayClient = getAlipayClient();
            response = alipayClient.execute(appPayRequest);

            // 查询交易 确认结果
            String tradeNo = response.getTradeNo();

            //查询支付单接口
            AlipayTradeQueryResponse responsebean = aliPayQueryTrade(null, tradeNo);
            String tradeStatus = responsebean.getTradeStatus();
            if (tradeStatus.equals("TRADE_SUCCESS")) {
                //交易成功
                return PayStatus.success();
            } else {
                return PayStatus.fail(responsebean.getBody());
            }


        } catch (AlipayApiException e) {
            e.printStackTrace();
            return PayStatus.fail(e.getErrCode()+"---"+e.getErrMsg());
        }
    }
    //订单查询
    @Override
    public AlipayTradeQueryResponse aliPayQueryTrade(String outTradeNo, String tradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject querybean = new JSONObject();
        querybean.put("out_trade_no", outTradeNo);
        querybean.put("trade_no", tradeNo);

        request.setBizContent(querybean.toString());
        AlipayTradeQueryResponse response = null;

        try {

            AlipayClient alipayClient = getAlipayClient();
            response = alipayClient.execute(request);


        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }

        return response;
    }
    //消费退款
    @Override
    public PayStatus aliPayTradeRefund(AliPayRefundRequestBean bean) {
        
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        JSONObject querybean = new JSONObject();
        querybean.put("out_trade_no", bean.getOutRequestNo());
        querybean.put("trade_no", bean.getTradeNo());

        querybean.put("refund_amount", bean.getRefundAmount());//或者是订单金额
        querybean.put("refund_reason", bean.getRefundReason());
        querybean.put("out_request_no", bean.getOutRequestNo());//config.getOutRequestNo()
        querybean.put("operator_id", bean.getOperatorId());
        querybean.put("store_id", bean.getStoreId());
        querybean.put("terminal_id", bean.getTerminalId());

        request.setBizContent(querybean.toString());
        AlipayTradeRefundResponse response = null;
        try {
            AlipayClient alipayClient = getAlipayClient();
            response = alipayClient.execute(request);

            //退款查询
            AlipayTradeFastpayRefundQueryResponse responseBean =
                    aliPayTradeRefundQuery(bean.getOutRequestNo(), bean.getTradeNo(), bean.getOutRequestNo());

            if (responseBean != null && responseBean.getTradeNo() != null) {
                return PayStatus.success();
            } else {
                return PayStatus.fail("没有查到退款信息！");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return PayStatus.fail(e.getErrCode()+"---"+e.getErrMsg());
        }

    }
    //消费退款查询
    @Override
    public AlipayTradeFastpayRefundQueryResponse aliPayTradeRefundQuery(String outTradeNo, String tradeNo, String outRequestNo) {

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

        JSONObject querybean = new JSONObject();
        querybean.put("out_trade_no", outTradeNo);
        querybean.put("trade_no", tradeNo);
        querybean.put("out_request_no", outRequestNo);

        request.setBizContent(querybean.toString());
        AlipayTradeFastpayRefundQueryResponse response = null;
        try {
            AlipayClient alipayClient = getAlipayClient();
            response = alipayClient.execute(request);
            return response;

        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }

    }
    //交易取消
    @Override
    public PayStatus aliPaytradeCancel(String out_trade_no, String trade_no) {


        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();

        JSONObject Cancelrequest = new JSONObject();
        Cancelrequest.put("out_trade_no", out_trade_no);
        Cancelrequest.put("trade_no", trade_no);

        request.setBizContent(Cancelrequest.toString());
        try {
            AlipayClient alipayClient = getAlipayClient();
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                return PayStatus.success();
            }
            return PayStatus.fail("订单关闭失败！");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return PayStatus.fail(e.getErrCode()+"---"+e.getErrMsg());
        }


    }




}

