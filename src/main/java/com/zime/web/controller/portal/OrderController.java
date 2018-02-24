package com.zime.web.controller.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.User;
import com.zime.web.service.OrderService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端订单管理接口",produces = "application/json")
@RestController
@RequestMapping("/order/")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@ApiOperation(value = "支付接口",notes = "订单支付操作")
	@RequestMapping(value="pay.do")	
	public ResponseEntity pay(HttpSession session, Long orderNo, HttpServletRequest request) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		
	   String path=	request.getSession().getServletContext().getRealPath("upload");
	   
	   return orderService.pay(orderNo, user.getId(), path);
	}
	
	
	
	
	
	
	
	

}
