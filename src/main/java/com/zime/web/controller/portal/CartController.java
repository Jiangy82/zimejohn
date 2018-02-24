package com.zime.web.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.User;
import com.zime.web.service.CartService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
import com.zime.web.vo.CartVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端购物车操作接口",produces = "application/json")
@RestController
@RequestMapping("/cart/")
public class CartController {
	@Autowired
	private CartService cartService;

	@ApiOperation(value = "添加至购物车接口",notes = "添加至购物车")
	@RequestMapping(value="add.do")	
	@ResponseBody
	public ResponseEntity<CartVo> add(HttpSession session, Integer count, Integer productId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.add(user.getId(), productId, count);
	}

	@ApiOperation(value = "查询购物车接口",notes = "查询购物车")
	@RequestMapping(value="list.do")	
	@ResponseBody
	public ResponseEntity<CartVo> list(HttpSession session) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.list(user.getId());
	}
	
	@ApiOperation(value = "更新购物车接口",notes = "更新购物车")
	@RequestMapping(value="update.do")	
	@ResponseBody
	public ResponseEntity<CartVo> update(HttpSession session, Integer count, Integer productId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.update(user.getId(), productId, count);
	}
	
	
	@ApiOperation(value = "删除购物车中的商品接口",notes = "删除购物车中的商品")
	@RequestMapping(value="delete_product.do")	
	@ResponseBody
	public ResponseEntity<CartVo> deleteProduct(HttpSession session, String productIds) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.deleteProduct(user.getId(), productIds);
	}
	
	
	@ApiOperation(value = "全选购物车中的商品接口",notes = "全选购物车中的商品")
	@RequestMapping(value="select_all.do")	
	@ResponseBody
	public ResponseEntity<CartVo> selectAll(HttpSession session) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.selectOrUnSelectAll(user.getId(), null, Const.Cart.CHECKED);
	}
	
	
	@ApiOperation(value = "全反选购物车中的商品接口",notes = "全反选购物车中的商品")
	@RequestMapping(value="un_select_all.do")	
	@ResponseBody
	public ResponseEntity<CartVo> unSelectAll(HttpSession session) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.selectOrUnSelectAll(user.getId(), null, Const.Cart.UN_CHECKED);
	}

	
	@ApiOperation(value = "选中某件商品接口",notes = "选中购物车中某件商品")
	@RequestMapping(value="select.do")	
	@ResponseBody
	public ResponseEntity<CartVo> select(HttpSession session, Integer productId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.selectOrUnSelectAll(user.getId(), productId, Const.Cart.CHECKED);
	}
	
	@ApiOperation(value = "反选某件商品接口",notes = "反选购物车中某件商品")
	@RequestMapping(value="un_select.do")	
	@ResponseBody
	public ResponseEntity<CartVo> unSelect(HttpSession session, Integer productId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
		return cartService.selectOrUnSelectAll(user.getId(), productId, Const.Cart.UN_CHECKED);
	}
	
	
	@ApiOperation(value = "获取购物车商品数量接口",notes = "获取购物车商品数量")
	@RequestMapping(value="get_cart_product_count.do")	
	@ResponseBody
	public ResponseEntity<Integer> getCartProductCount(HttpSession session) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.success(0);
		}
		return cartService.getCartProductCount(user.getId());
	}
}
