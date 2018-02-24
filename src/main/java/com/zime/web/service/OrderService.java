package com.zime.web.service;

import com.zime.web.util.ResponseEntity;

public interface OrderService {

	ResponseEntity pay(Long orderNo, Integer userId, String path);
}
