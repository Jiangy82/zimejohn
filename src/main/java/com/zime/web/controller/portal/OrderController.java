package com.zime.web.controller.portal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.internal.util.AlipaySignature;
import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.User;
import com.zime.web.pay.action.PayController;
import com.zime.web.pay.config.AliPayConfig;
import com.zime.web.pay.service.impl.alipay.AlipayNotify;
import com.zime.web.service.OrderService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端订单管理接口",produces = "application/json")
@RestController
@RequestMapping("/order/")
public class OrderController {
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrderService orderService;
	
	@Resource
	private AliPayConfig aliPayConfig;
	

	@ApiOperation(value = "创建订单接口",notes = "创建订单操作")
	@RequestMapping(value="create.do")	
	public ResponseEntity createOrder(HttpSession session, Integer shippingId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}

	   return orderService.createOrder(user.getId(), shippingId);
	}	

	@ApiOperation(value = "取消订单接口",notes = "取消订单操作")
	@RequestMapping(value="cancel.do")	
	public ResponseEntity cancelOrder(HttpSession session,Long orderNo) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}

	   return orderService.cancelOrder(user.getId(), orderNo);
	}	


	@ApiOperation(value = "查看订单详情接口",notes = "查看订单详情")
	@RequestMapping(value="detail.do")	
	public ResponseEntity detail(HttpSession session,Long orderNo) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}

	   return orderService.getOrderDetail(user.getId(), orderNo);
	}
	
	@ApiOperation(value = "获取购物车中商品信息接口",notes = "获取购物车中商品信息操作")
	@RequestMapping(value="get_order_cart_product.do")	
	public ResponseEntity getOrderCartProduct(HttpSession session) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}

	   return orderService.getOrderCartProduct(user.getId());
	}
	
	@ApiOperation(value = "获取订单列表操作接口",notes = "获取订单列表操作")
	@RequestMapping(value="list.do")	
	public ResponseEntity list(HttpSession session,
			@RequestParam(value="pageNum",defaultValue="1") int pageNum, 
			@RequestParam(value="pageSize",defaultValue="10")int pageSize
			) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}

	   return orderService.getOrderList(user.getId(),pageNum,pageSize);
	}	
	
	
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
	
	@ApiOperation(value = "支付宝后台回调接口",notes = "支付宝后台回调操作")
    @RequestMapping(value = "/alipayNotify")
    public Object alipayNotify(HttpServletRequest request) throws Exception {

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
        	ResponseEntity responseEntity= orderService.alipayCallback(params);
        	if(responseEntity.isSuccess()) {
        		return Const.AlipayCallback.RESPONSE_SUCCESS;
        	}
        	else {
        		return Const.AlipayCallback.RESPONSE_FAILED;
        	}

        } else {
          throw new Exception("验证签名失败");
        }
    }
	
	@ApiOperation(value = "查询支付状态接口",notes = "查询订单支付状态操作")
	@RequestMapping(value="query_order_pay_status.do")	
	public ResponseEntity<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo, HttpServletRequest request) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), Errors.SYSTEM_NOT_LOGIN.label);
		}
	   
	   ResponseEntity responseEntity= orderService.queryOrderPayStatus(orderNo, user.getId());
	   if(responseEntity.isSuccess()) {
		   return ResponseEntityUtil.success(true);
	   }else {
		   return ResponseEntityUtil.success(false);
	   }
	   
	}
	

}
