package com.zime.web.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.zime.web.config.FtpConfig;
import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.Cart;
import com.zime.web.dao.entity.Product;
import com.zime.web.dao.mapper.CartMapper;
import com.zime.web.dao.mapper.ProductMapper;
import com.zime.web.service.CartService;
import com.zime.web.util.BigDecimalUtil;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
import com.zime.web.vo.CartProductVo;
import com.zime.web.vo.CartVo;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private FtpConfig ftpConfig;
	
	public ResponseEntity<CartVo> add(Integer userId, Integer productId, Integer count){
		if(productId==null || count==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(), Errors.SYSTEM_REQUEST_PARAM_ERROR.label);
		}
		Cart cart= cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart==null) {
			//产品不在购物车里，新增一条产品记录
			Cart cartItem=new Cart();
			cartItem.setChecked(Const.Cart.CHECKED);
			cartItem.setQuantity(count);
			cartItem.setProductId(productId);
			cartItem.setUserId(userId);
			cartMapper.insert(cartItem);
		}else {
			//产品已存在，在现有数量上相加
			count= cart.getQuantity()+count;
			cart.setQuantity(count);
			cartMapper.updateByPrimaryKeySelective(cart);
		}

		return this.list(userId);

	}

	@Override
	public ResponseEntity<CartVo> list(Integer userId) {
		CartVo cartVo =this.getCartVoLimit(userId);
		return ResponseEntityUtil.success(cartVo);
	}

	@Override
	public ResponseEntity<CartVo> update(Integer userId, Integer productId, Integer count) {
		if(productId==null || count==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(), Errors.SYSTEM_REQUEST_PARAM_ERROR.label);
		}
		Cart cart= cartMapper.selectCartByUserIdProductId(userId, productId);
		if(cart!=null) {
			cart.setQuantity(count);
		}
		cartMapper.updateByPrimaryKeySelective(cart);
		
		return this.list(userId);
	}
	

	@Override
	public ResponseEntity<CartVo> deleteProduct(Integer userId, String productIds) {
		
		List<String> productList= Splitter.on(",").splitToList(productIds);
		if(CollectionUtils.isEmpty(productList)) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_REQUEST_PARAM_ERROR.getCode(), Errors.SYSTEM_REQUEST_PARAM_ERROR.label);
		}
		cartMapper.deleteByUserIdProductIds(userId, productList);
		
		return this.list(userId);
	}
	
	
	private CartVo getCartVoLimit(Integer userId) {
		CartVo cartVo= new CartVo();
		List<Cart> carts= cartMapper.selectCartByUserId(userId);
		List<CartProductVo> cartProductVos= Lists.newArrayList();
		
		BigDecimal cartTotalPrice= new BigDecimal("0");
		
		if(CollectionUtils.isNotEmpty(carts)) {
			for(Cart cartItem: carts) {
				CartProductVo cartProductVo=new CartProductVo();
				cartProductVo.setId(cartItem.getId());
				cartProductVo.setUserId(userId);
				cartProductVo.setProductId(cartItem.getProductId());
				
				Product product= productMapper.selectByPrimaryKey(cartItem.getProductId());
				if(product!=null) {
					cartProductVo.setProductMainImage(product.getMainImage());
					cartProductVo.setProductName(product.getName());
					cartProductVo.setProductPrice(product.getPrice());
					cartProductVo.setProductStatus(product.getStatus());
					cartProductVo.setProductStock(product.getStock());
					cartProductVo.setProductSubtitle(product.getSubtitle());
					//判断库存
					int buyLimitCount=0;
					if(product.getStock() >= cartItem.getQuantity()) {
						buyLimitCount=cartItem.getQuantity();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
					}else {
						buyLimitCount=product.getStock();
						cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
						Cart cartForQuantity=new Cart();
						cartForQuantity.setId(cartItem.getId());
						cartForQuantity.setQuantity(buyLimitCount);
						cartMapper.updateByPrimaryKeySelective(cartForQuantity);
					}
					cartProductVo.setQuantity(buyLimitCount);
					cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity().doubleValue()));
					cartProductVo.setProductChecked(cartItem.getChecked());
					
					if(cartItem.getChecked() == Const.Cart.CHECKED) {
						cartTotalPrice= BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
					}
					
					cartProductVos.add(cartProductVo);
				}
			}
		}
		cartVo.setCartTotalPrice(cartTotalPrice);
		cartVo.setCartProductVoList(cartProductVos);
		cartVo.setAllChecked(this.getAllCheckedStatus(userId));
		cartVo.setImageHost(ftpConfig.getServerHttpPrefix());
		return cartVo;
				
	}
	
	private boolean getAllCheckedStatus(Integer userId) {
		if(userId==null) {
			return false;
		}
		return cartMapper.selectCartProductCheckedStatusByUserId(userId)==0;
	}

	@Override
	public ResponseEntity<CartVo> selectOrUnSelectAll(Integer userId, Integer productId, Integer checked) {
		cartMapper.checkedOrUncheckedProduct(userId, productId, checked);
		return this.list(userId);
	}

	@Override
	public ResponseEntity<Integer> getCartProductCount(Integer userId) {
		if(userId==null) {
			ResponseEntityUtil.success(0);
		}
		
		return ResponseEntityUtil.success(cartMapper.selectCartProductCount(userId));
	}





}
