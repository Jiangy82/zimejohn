package com.zime.web.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.zime.web.dao.entity.Shipping;
import com.zime.web.dao.mapper.ShippingMapper;
import com.zime.web.service.ShippingService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;
@Service
public class ShippingServiceImpl implements ShippingService{
	@Autowired
	private ShippingMapper shippingMapper;

	@Override
	public ResponseEntity add(Integer userId, Shipping shipping) {

		shipping.setUserId(userId);
		int resultCount= shippingMapper.insert(shipping);
		if(resultCount>0) {
			Map<String, Integer> result= Maps.newHashMap();
			result.put("shippingId", shipping.getId());
			return ResponseEntityUtil.success(result);
		}

		return ResponseEntityUtil.fail("新建地址失败");
	}

	@Override
	public ResponseEntity del(Integer userId, Integer shippingId) {
		int resultCount= shippingMapper.deleteByShippingIdUserId(userId, shippingId);
		if(resultCount>0) {
			return ResponseEntityUtil.success("删除地址成功");
		}
		return ResponseEntityUtil.success("删除地址失败");
	}

	@Override
	public ResponseEntity update(Integer userId, Shipping shipping) {
		shipping.setUserId(userId);
		int resultCount= shippingMapper.updateByShipping(shipping);
		if(resultCount>0) {
			return ResponseEntityUtil.success("更新地址成功");
		}

		return ResponseEntityUtil.fail("更新地址失败");
	}

	@Override
	public ResponseEntity select(Integer userId, Integer shippingId) {
		Shipping shipping= shippingMapper.selectByShippingIdUserId(userId, shippingId);
		if(shipping==null) {
			return ResponseEntityUtil.fail("无法查询该收货地址");
		}
		return ResponseEntityUtil.success(shipping);
	}

	@Override
	public ResponseEntity<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippings= shippingMapper.selectByUserId(userId);
		PageInfo pageInfo=new PageInfo(shippings);
		return ResponseEntityUtil.success(pageInfo);
	}

}
