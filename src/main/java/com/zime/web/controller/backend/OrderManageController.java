package com.zime.web.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.User;
import com.zime.web.service.OrderService;
import com.zime.web.service.UserService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="订单后台管理操作接口",produces = "application/json")
@RestController
@RequestMapping("/manage/order/")
public class OrderManageController {

	@Autowired
	private UserService userService;
	@Autowired
	private OrderService orderService;
	
	@ApiOperation(value = "获取订单列表操作接口",notes = "获取订单列表")
	@PostMapping(value="list.do")	
	public ResponseEntity getList(HttpSession session,@RequestParam(value="pageNum", defaultValue="1") int pageNum,  @RequestParam(value="pageSize", defaultValue="10") int pageSize) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return orderService.manageOrderList(pageNum, pageSize);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	
	@ApiOperation(value = "获取订单详情操作接口",notes = "获取订单详情")
	@PostMapping(value="detail.do")	
	public ResponseEntity orderDetail(HttpSession session, Long orderNo) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return orderService.manageOrderDetail(orderNo);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	
	@ApiOperation(value = "查询订单操作接口",notes = "查询订单操作")
	@PostMapping(value="search.do")	
	public ResponseEntity search(HttpSession session, Long orderNo,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,  
			@RequestParam(value="pageSize", defaultValue="10") int pageSize) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return orderService.manageSearch(orderNo,pageNum,pageSize);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}	
	
	
	@ApiOperation(value = "管理发货操作接口",notes = "发货管理")
	@PostMapping(value="send_goods.do")	
	public ResponseEntity orderSendGoods(HttpSession session, Long orderNo) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return orderService.manageSendGoods(orderNo);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
}
