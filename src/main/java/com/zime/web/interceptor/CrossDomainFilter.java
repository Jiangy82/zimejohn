package com.zime.web.interceptor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 允许跨域过滤器
 *
 * Filter比Spring的Intercepter更早介入请求生命周期，所以可以更早的处理OPTIONS请求.
 * @author LJH
 */
public class CrossDomainFilter extends OncePerRequestFilter {

  private static final Logger LOG = LoggerFactory.getLogger(CrossDomainFilter.class);

  private volatile boolean allowCrossDomain = true;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    // 设置允许跨域访问
    LOG.debug("Request Orign = {},url={}", request.getHeader("Origin"),request.getRequestURL());
    
    if(allowCrossDomain){
      // 重要：clientIp不能为*，否则session无法传递到服务器端.
      response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
      response.addHeader("Access-Control-Allow-Credentials", "true");
      /**
       * 未处理缓存
       */
      response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
      response.addHeader("Access-Control-Allow-Headers",
          "X-Requested-With, X-Access-Token "+
          ", X-Upload-Auth-Token, Origin, Content-Type, Cookie");
    }
    if(request.getMethod().equals("OPTIONS")){
    	return ;
    }else{
    	filterChain.doFilter(request, response);
    }
  }

  public void setAllowCrossDomain(boolean allowCrossDomain) {
    this.allowCrossDomain = allowCrossDomain;
  }
}
