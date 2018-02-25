package com.zime.web.pay.action;

import com.alipay.api.internal.util.AlipaySignature;
import com.zime.web.pay.service.impl.alipay.AlipayNotify;

import com.zime.web.pay.config.AliPayConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by LJH on 2017/11/6.
 */
public class PayController {
	private static final Logger log = LoggerFactory.getLogger(PayController.class);


    @Resource
    private AliPayConfig aliPayConfig;

    /**
     * 支付宝后台回调接口
     *
     * @param request
     * @return
     * @throws Exception
     */
   // @ACS(allowAnonymous = true)
    @RequestMapping(value = "/alipayNotify")
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        //logger.info("支付宝后台回调");
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();

        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验证回调的正确性，是不是支付宝发的，而且避免重复通知 
        boolean signVerfied = false;
        if (request.getParameter("sign_type").equals("MD5")) {
            signVerfied = AlipayNotify.verify(params);
        } else {
            signVerfied = AlipaySignature.rsaCheckV2(params, aliPayConfig.getAliPublicKey(), AliPayConfig.CHAESET,
                    AliPayConfig.SIGN_TYPE);
        }

        if (signVerfied) {
            //回调业务

          //  logger.info("订单支付宝支付成功");
        } else {
          throw new Exception("验证签名失败");
        }
    }


}
