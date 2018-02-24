package com.zime.web.service;

import com.zime.web.dao.entity.Shipping;
import com.zime.web.util.ResponseEntity;

public interface ShippingService {

	ResponseEntity add(Integer userId, Shipping shipping);

	ResponseEntity del(Integer userId, Integer shippingId);

	ResponseEntity update(Integer userId, Shipping shipping);

	ResponseEntity select(Integer userId, Integer shippingId);

	ResponseEntity list(Integer userId, Integer pageNum, Integer pageSize);
}
