package com.zime.web.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.zime.web.dao.entity.Order;
import com.zime.web.dao.mapper.OrderMapper;
import com.zime.web.pay.config.AliPayConfig;
import com.zime.web.pay.config.PayStatus;
import com.zime.web.pay.config.request.AlipayRequestBean;
import com.zime.web.pay.service.PayService;
import com.zime.web.service.OrderService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private PayService payService;
	
	public ResponseEntity pay(Long orderNo, Integer userId, String path) {
		Map<String, String> resultMap= Maps.newHashMap();
		Order order= orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
		if(order==null) {
			return ResponseEntityUtil.fail("用户没有该订单");
		}
		resultMap.put("orderNo", String.valueOf(order.getOrderNo()));
		
		AlipayRequestBean payBean=new AlipayRequestBean();
		payBean.setOrderId(order.getOrderNo().toString());
		payBean.setBody(new StringBuilder().append("zimemall扫码支付，订单号:").append(order.getOrderNo().toString()).append(",购买商品共").append(order.getPayment().toString()).append("元").toString());
		payBean.setTotalAmount(order.getPayment().toString());
		payBean.setAuthcode("280752538213075206");
		//payBean.setShopid(alipayConfig.getSellerId());
		
		
		
		PayStatus payStatus= payService.aliPayBarCode(payBean);
		if(payStatus.getCode()==1) {
			return ResponseEntityUtil.success("支付成功");
		}
		else {
			return ResponseEntityUtil.fail("支付失败");
		}

	}
}
