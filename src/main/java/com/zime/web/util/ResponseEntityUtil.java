package com.zime.web.util;

import com.zime.web.contants.Errors;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * 响应返回数据工具类
 *
 */
public class ResponseEntityUtil {

  public static <T> ResponseEntity<T> success(T data) {
    ResponseEntity<T> entity = new ResponseEntity<T>();
    entity.setData(data);
    entity.setCode(Errors.SUCCESS.code);
    entity.setHttpStatus(HttpStatus.OK.value());
    entity.setTimestamp(Long.valueOf(System.currentTimeMillis()));
    return entity;
  }

  public static <T> ResponseEntity<T> success() {
    ResponseEntity<T> entity = new ResponseEntity<T>();
    entity.setTimestamp(Long.valueOf(System.currentTimeMillis()));
    entity.setCode(Errors.SUCCESS.code);
    entity.setHttpStatus(HttpStatus.OK.value());
    return entity;
  }

  public static ResponseEntity<Void> fail(Integer httpStatus, Integer code, String message, HttpServletRequest request) {
    ResponseEntity<Void> entity = build();
    entity.setCode(code);
    entity.setHttpStatus(httpStatus);
    entity.setException(message);
    if (null != request) {
      entity.setPath(request.getRequestURI());
    }
    return entity;
  }

  public static <T> ResponseEntity<T> fail(Integer code, String message) {
    ResponseEntity<T> entity = new ResponseEntity<T>();
    entity.setTimestamp(Long.valueOf(System.currentTimeMillis()));
    entity.setCode(code);
    entity.setException(message);
    return entity;
  }

  public static <T> ResponseEntity<T> fail(String message) {
    ResponseEntity<T> entity = new ResponseEntity<T>();
    entity.setTimestamp(Long.valueOf(System.currentTimeMillis()));
    entity.setCode(Errors.SYSTEM_CUSTOM_ERROR.code);
    entity.setException(message);
    return entity;
  }

  private static ResponseEntity<Void> build() {
    ResponseEntity<Void> entity = new ResponseEntity<Void>();
    entity.setTimestamp(Long.valueOf(System.currentTimeMillis()));
    return entity;
  }

  public static ResponseEntity<Void> fail(Integer httpStatus, Integer code, String message, HttpServletRequest request, Exception e) {
    ResponseEntity<Void> entity = build();
    entity.setCode(code);
    entity.setHttpStatus(httpStatus);
    entity.setException(message);
    if (null != request) {
      entity.setPath(request.getRequestURI());
    }
    entity.setErrMsg(e.getMessage());
    return entity;
  }

}
