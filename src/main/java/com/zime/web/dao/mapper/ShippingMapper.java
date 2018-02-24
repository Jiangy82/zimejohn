package com.zime.web.dao.mapper;

import java.util.List;

import com.zime.web.dao.entity.Shipping;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
    
    int deleteByShippingIdUserId(Integer userId, Integer shippingId);
    
    int updateByShipping(Shipping record);
    
    Shipping selectByShippingIdUserId(Integer userId, Integer shippingId);
    
    List<Shipping> selectByUserId(Integer userId);
}