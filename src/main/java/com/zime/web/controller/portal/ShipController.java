package com.zime.web.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.Shipping;
import com.zime.web.dao.entity.User;
import com.zime.web.service.ShippingService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
import com.zime.web.vo.CartVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端收货地址管理接口",produces = "application/json")
@RestController
@RequestMapping("/shipping/")
public class ShipController {
	@Autowired
	private ShippingService shippingService;

	@ApiOperation(value = "添加收货地址接口",notes = "添加收货地址")
	@RequestMapping(value="add.do")	
	@ResponseBody
	public ResponseEntity add(HttpSession session, Shipping shipping) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return shippingService.add(user.getId(), shipping);
	}
	
	@ApiOperation(value = "删除收货地址接口",notes = "删除收货地址")
	@RequestMapping(value="del.do")	
	@ResponseBody
	public ResponseEntity del(HttpSession session, Integer shippingId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return shippingService.del(user.getId(), shippingId);
	}
	

	@ApiOperation(value = "更新收货地址接口",notes = "更新收货地址")
	@RequestMapping(value="update.do")	
	@ResponseBody
	public ResponseEntity update(HttpSession session, Shipping shipping) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return shippingService.update(user.getId(), shipping);
	}
	
	@ApiOperation(value = "查询收货地址接口",notes = "查询收货地址")
	@RequestMapping(value="select.do")	
	@ResponseBody
	public ResponseEntity select(HttpSession session, Integer shippingId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return shippingService.select(user.getId(), shippingId);
	}
	
	
	@ApiOperation(value = "查询收货地址列表接口",notes = "查询收货地址列表")
	@RequestMapping(value="list.do")	
	@ResponseBody
	public ResponseEntity list(HttpSession session, Integer pageNum, Integer pageSize) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return shippingService.list(user.getId(), pageNum, pageSize);
	}
}
