package com.zime.web.dao.mapper;
import java.util.List;

import com.zime.web.dao.entity.SysLog;



public interface SysLogMapper {
	
	List<SysLog> selectBySysLog();

    int insertSelective(SysLog record);

    int updateByPrimaryKeySelective(SysLog record);
}