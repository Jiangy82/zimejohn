package com.zime.web.service;

import com.zime.web.util.ResponseEntity;
import com.zime.web.vo.CartVo;

public interface CartService {
	ResponseEntity<CartVo> add(Integer userId, Integer productId, Integer count);
	
	ResponseEntity<CartVo> update(Integer userId, Integer productId, Integer count);
	
	ResponseEntity<CartVo> deleteProduct(Integer userId, String productIds);

	ResponseEntity<CartVo> list(Integer userId);
	
	ResponseEntity<CartVo> selectOrUnSelectAll(Integer userId, Integer productId, Integer checked);

	ResponseEntity<Integer> getCartProductCount(Integer userId);
}
