package com.zime.web.service;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.zime.web.util.ResponseEntity;
import com.zime.web.vo.OrderVo;

public interface OrderService {

	ResponseEntity pay(Long orderNo, Integer userId, String path);
	
	ResponseEntity alipayCallback(Map<String, String> params);

	ResponseEntity queryOrderPayStatus(Long orderNo, Integer userId);

	ResponseEntity createOrder(Integer userId, Integer shippingId);

	ResponseEntity cancelOrder(Integer userId, Long orderNo);
	
	ResponseEntity getOrderCartProduct(Integer userId);

	ResponseEntity getOrderDetail(Integer userId, Long orderNo);

	ResponseEntity<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

	ResponseEntity manageOrderList(int pageNum, int pageSize);

	ResponseEntity<OrderVo> manageOrderDetail(Long orderNo);

	ResponseEntity<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize);

	ResponseEntity manageSendGoods(Long orderNo);
}
