package com.zime.web.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zime.web.config.FtpConfig;
import com.zime.web.contants.Const;
import com.zime.web.dao.entity.Cart;
import com.zime.web.dao.entity.Order;
import com.zime.web.dao.entity.OrderItem;
import com.zime.web.dao.entity.PayInfo;
import com.zime.web.dao.entity.Product;
import com.zime.web.dao.entity.Shipping;
import com.zime.web.dao.mapper.CartMapper;
import com.zime.web.dao.mapper.OrderItemMapper;
import com.zime.web.dao.mapper.OrderMapper;
import com.zime.web.dao.mapper.PayInfoMapper;
import com.zime.web.dao.mapper.ProductMapper;
import com.zime.web.dao.mapper.ShippingMapper;
import com.zime.web.pay.config.PayStatus;
import com.zime.web.pay.config.request.AlipayRequestBean;
import com.zime.web.pay.service.PayService;
import com.zime.web.service.OrderService;
import com.zime.web.util.BigDecimalUtil;
import com.zime.web.util.DateUtil;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
import com.zime.web.vo.OrderItemVo;
import com.zime.web.vo.OrderProductVo;
import com.zime.web.vo.OrderVo;
import com.zime.web.vo.ShippingVo;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private PayService payService;
	@Autowired
	private PayInfoMapper payInfoMapper;
    @Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private ShippingMapper shippingMapper;
	@Autowired
	private FtpConfig ftpConfig;
	
	
	public ResponseEntity pay(Long orderNo, Integer userId, String path) {
		Map<String, String> resultMap = Maps.newHashMap();
		Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
		if (order == null) {
			return ResponseEntityUtil.fail("用户没有该订单");
		}
		resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

		AlipayRequestBean payBean = new AlipayRequestBean();
		payBean.setOrderId(order.getOrderNo().toString());
		payBean.setBody(new StringBuilder().append("zimemall扫码支付，订单号:").append(order.getOrderNo().toString())
				.append(",购买商品共").append(order.getPayment().toString()).append("元").toString());
		payBean.setTotalAmount(order.getPayment().toString());
		payBean.setAuthcode("280752538213075206");
		// payBean.setShopid(alipayConfig.getSellerId());

		PayStatus payStatus = payService.aliPayBarCode(payBean);
		if (payStatus.getCode() == 1) {
			return ResponseEntityUtil.success("支付成功");
		} else {
			return ResponseEntityUtil.fail("支付失败");
		}

	}

	public ResponseEntity alipayCallback(Map<String, String> params) {
		Long orderNo = Long.parseLong(params.get("out_trade_no"));
		String tradeNo = params.get("trade_no");
		String tradeStatus = params.get("trade_status");
		Order order = orderMapper.selectByOrderNo(orderNo);

		if (order == null) {
			return ResponseEntityUtil.fail("非本商城的订单，回调忽略");
		}
		if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
			return ResponseEntityUtil.success("支付宝重复调用");
		}
		if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
			order.setPaymentTime(DateUtil.stringToDate(params.get("gmt_payment"), DateUtil.DEFAULT_PATTERN));
			order.setStatus(Const.OrderStatusEnum.PAID.getCode());
			orderMapper.updateByPrimaryKeySelective(order);
		}

		PayInfo payInfo = new PayInfo();
		payInfo.setUserId(order.getUserId());
		payInfo.setOrderNo(order.getOrderNo());
		payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
		payInfo.setPlatformNumber(tradeNo);
		payInfo.setPlatformStatus(tradeStatus);

		payInfoMapper.insert(payInfo);

		return ResponseEntityUtil.success();

	}

	@Override
	public ResponseEntity queryOrderPayStatus(Long orderNo, Integer userId) {
		Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
		if (order == null) {
			return ResponseEntityUtil.fail("用户没有该订单");
		}
		if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()) {
			return ResponseEntityUtil.success();
		}
		return ResponseEntityUtil.fail("支付失败");
	}

	@Override
	public ResponseEntity createOrder(Integer userId, Integer shippingId) {
		List<Cart> carts = cartMapper.selectCheckedCartByUserId(userId);
		//计算订单总价
		ResponseEntity responseEntity= this.getCartOrderItem(userId, carts);
		if(!responseEntity.isSuccess()) {
			return responseEntity;
		}
		List<OrderItem> orderItems = (List<OrderItem>)responseEntity.getData();
		BigDecimal payment= this.getOrderTotalPrice(orderItems);
		

        //生成订单
        Order order = this.assembleOrder(userId,shippingId,payment);
        if(order == null){
            return ResponseEntityUtil.fail("生成订单错误");
        }
        if(CollectionUtils.isEmpty(orderItems)){
            return ResponseEntityUtil.fail("购物车为空");
        }
        for(OrderItem orderItem : orderItems){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //mybatis批量插入Orderitem
        orderItemMapper.batchInsert(orderItems);
        
        //订单生成成功，减少产品库存
        this.reduceProductStock(orderItems);
        //清空购物车
        this.cleanCart(carts);
        
        OrderVo orderVo = assembleOrderVo(order, orderItems);
        return ResponseEntityUtil.success(orderVo);
	}

    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc(Const.PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());

        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getValue());

        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if(shipping != null){
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(assembleShippingVo(shipping));
        }

        orderVo.setPaymentTime(DateUtil.format(order.getPaymentTime()));
        orderVo.setSendTime(DateUtil.format(order.getSendTime()));
        orderVo.setEndTime(DateUtil.format(order.getEndTime()));
        orderVo.setCreateTime(DateUtil.format(order.getCreateTime()));
        orderVo.setCloseTime(DateUtil.format(order.getCloseTime()));


        orderVo.setImageHost(ftpConfig.getServerHttpPrefix());


        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        for(OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());

        orderItemVo.setCreateTime(DateUtil.format(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

	private void cleanCart(List<Cart> cartList) {
		for(Cart cart :cartList) {
			cartMapper.deleteByPrimaryKey(cart.getId());
		}
	}
	
	private void reduceProductStock(List<OrderItem> orderItems) {
		for(OrderItem orderItem :orderItems) {
			Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
			product.setStock(product.getStock()- orderItem.getQuantity());
			productMapper.updateByPrimaryKeySelective(product);
		}
	}
	
    private Order assembleOrder(Integer userId,Integer shippingId,BigDecimal payment){
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);

        order.setUserId(userId);
        order.setShippingId(shippingId);
        //发货时间等等
        //付款时间等等
        int rowCount = orderMapper.insert(order);
        if(rowCount > 0){
            return order;
        }
        return null;
    }
	
    private long generateOrderNo(){
        long currentTime =System.currentTimeMillis();
        return currentTime+new Random().nextInt(100);
    }
	
	private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems) {
		BigDecimal payment= new BigDecimal("0");
		for(OrderItem orderItem :orderItems) {
			BigDecimalUtil.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
		}
		return payment;
	}
	
	private ResponseEntity getCartOrderItem(Integer userId, List<Cart> cartList){
		List<OrderItem> orderItems= Lists.newArrayList();
		
		if(CollectionUtils.isEmpty(cartList)) {
			return ResponseEntityUtil.fail("购物车为空");
		}
		
		for(Cart cart: cartList) {
			OrderItem orderItem =new OrderItem();
			Product product= productMapper.selectByPrimaryKey(cart.getProductId());
			if(Const.ProductStatusEnum.ON_SALE.getCode()!= product.getStatus()) {
				return ResponseEntityUtil.fail("产品："+product.getName()+"不在销售状态");
			}
			
			if(cart.getQuantity()> product.getStock()) {
				return ResponseEntityUtil.fail("产品库存不足");
			}
			orderItem.setUserId(userId);
			orderItem.setProductId(product.getId());
			orderItem.setProductName(product.getName());
			orderItem.setProductImage(product.getMainImage());
			orderItem.setQuantity(cart.getQuantity());
			orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cart.getQuantity()));
			
			orderItems.add(orderItem);
		}
		return ResponseEntityUtil.success(orderItems);
	}

	public ResponseEntity<String> cancelOrder(Integer userId, Long orderNo) {
		Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
		if (order == null) {
			return ResponseEntityUtil.fail("用户没有该订单");
		}
		if (order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()) {
			return ResponseEntityUtil.fail("已付款的订单不可以取消");
		}
		
		Order updateOrder=new Order();
		updateOrder.setId(order.getId());
		updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
	    
		int resultCount= orderMapper.updateByPrimaryKeySelective(updateOrder);
		if(resultCount>0) {
			return ResponseEntityUtil.success("订单已取消");
		}else {
			return ResponseEntityUtil.fail("订单取消失败");
		}
	}
	

    public ResponseEntity getOrderCartProduct(Integer userId){
        OrderProductVo orderProductVo = new OrderProductVo();
        //从购物车中获取数据

        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ResponseEntity serverResponse =  this.getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList =( List<OrderItem> ) serverResponse.getData();

        List<OrderItemVo> orderItemVoList = Lists.newArrayList();

        BigDecimal payment = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            payment = BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        orderProductVo.setImageHost(ftpConfig.getServerHttpPrefix());
        return ResponseEntityUtil.success(orderProductVo);
    }

	@Override
	public ResponseEntity getOrderDetail(Integer userId, Long orderNo) {
		Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
		if (order == null) {
			return ResponseEntityUtil.fail("用户没有该订单");
		}
		List<OrderItem> orderItemList= orderItemMapper.getByOrderNoUserId(orderNo, userId);
		OrderVo orderVo= assembleOrderVo(order, orderItemList);
		return ResponseEntityUtil.success(orderVo);
	}

	@Override
	public ResponseEntity<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orderList= orderMapper.selectByUserId(userId);
		List<OrderVo> orderVoList= assembleOrderVoList(orderList, userId);
		PageInfo pageInfo=new PageInfo<>(orderList);
		pageInfo.setList(orderVoList);
		
		return ResponseEntityUtil.success(pageInfo);
	}
	
	private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId){
		List<OrderVo> orderVoList= Lists.newArrayList();
		for(Order order :orderList) {
			List<OrderItem> orderItemList= Lists.newArrayList();
			if(userId!=null) {
				orderItemList=orderItemMapper.getByOrderNoUserId(order.getOrderNo(), userId);
			}else {
				orderItemList= orderItemMapper.getByOrderNo(order.getOrderNo());
			}
			
			OrderVo orderVo= assembleOrderVo(order, orderItemList);
			orderVoList.add(orderVo);
		}
		return orderVoList;
	}

	
	
	//////////////////////////////backend///////////////////////////
	
	@Override
	public ResponseEntity manageOrderList(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Order> orderList= orderMapper.selectAllOrder();
		List<OrderVo> orderVoList= assembleOrderVoList(orderList, null);
		PageInfo pageInfo=new PageInfo<>(orderList);
		pageInfo.setList(orderVoList);
		
		return ResponseEntityUtil.success(pageInfo);
	}

	@Override
	public ResponseEntity<OrderVo> manageOrderDetail(Long orderNo) {
		Order order= orderMapper.selectByOrderNo(orderNo);
		if(order!=null) {
			List<OrderItem> orderItemList= orderItemMapper.getByOrderNo(orderNo);
			OrderVo orderVo= assembleOrderVo(order, orderItemList);
			return ResponseEntityUtil.success(orderVo);
		}
		return ResponseEntityUtil.fail("订单不存在");
	}

	@Override
	public ResponseEntity<PageInfo> manageSearch(Long orderNo, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Order order= orderMapper.selectByOrderNo(orderNo);
		if(order!=null) {
			List<OrderItem> orderItemList= orderItemMapper.getByOrderNo(orderNo);
			OrderVo orderVo= assembleOrderVo(order, orderItemList);
			
			PageInfo pageInfo= new PageInfo<>(Lists.newArrayList(order));
			pageInfo.setList(Lists.newArrayList(orderVo));
			
			return ResponseEntityUtil.success(pageInfo);
		}
		return ResponseEntityUtil.fail("订单不存在");
	}

	@Override
	public ResponseEntity<String> manageSendGoods(Long orderNo) {
		Order order= orderMapper.selectByOrderNo(orderNo);
		if(order!=null) {
			if(order.getStatus()== Const.OrderStatusEnum.PAID.getCode()) {
				order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
				order.setSendTime(new Date());
				orderMapper.updateByPrimaryKeySelective(order);
				return ResponseEntityUtil.success("发货成功");
			}
		}
		return ResponseEntityUtil.fail("订单不存在");
	}
	
}
