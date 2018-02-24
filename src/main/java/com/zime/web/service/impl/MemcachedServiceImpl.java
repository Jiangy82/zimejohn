package com.zime.web.service.impl;


import com.zime.web.config.AppConfig;
import com.zime.web.service.MemcachedService;
import net.spy.memcached.MemcachedClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Description: 缓存业务
 * @date 2017年1月23日
 * @version V1.0
 */
@Service("memcachedService")
public class MemcachedServiceImpl implements MemcachedService {

  @Resource
  private MemcachedClient memcachedClient;
  @Resource
  private AppConfig appConfig;

  /**
   * 设置缓存
   * 
   * @param key
   * @param exprieTime：过期时间，单位秒（例如exprieTime=30，为30秒）
   * @param value：值
   */
  @Override
  public void set(String key, int exprieTime, Object value) {
    memcachedClient.set(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key, exprieTime, value);
  }

  /**
   * 删除
   */
  @Override
  public void delete(String key) {
    memcachedClient.delete(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key);
  }

  /**
   * 获取缓存值
   */
  @Override
  public Object get(String key) {
    return memcachedClient.get(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key);
  }

  /**
   * 获得缓存中的数据并重置其过期时间.
   */
  @Override
  public Object refresh(String key, int exprieTime) {
    Object value = memcachedClient.get(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key);
    if (value != null) {
      memcachedClient.set(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key, exprieTime, value);
    }
    return value;
  }

  /**
   * 缓存值+1，返回+1之后的值
   * 
   * @param key
   * @return
   */
  protected long incr(String key) {
    return memcachedClient.incr(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key, 1);
  }

  /**
   * 缓存值-1，返回-1之后的值
   * 
   * @param key
   * @return
   */
  protected long decr(String key) {
    return memcachedClient.decr(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+key, 1, 1l);
  }

  /**
   * 批量获取缓存值
   * 
   * @param keys
   * @return
   */
  protected Map<String, Object> getBulk(List<String> keys) {
    return memcachedClient.getBulk(appConfig.getAppName()+"_"+appConfig.getEnv()+"_"+keys);
  }

}
