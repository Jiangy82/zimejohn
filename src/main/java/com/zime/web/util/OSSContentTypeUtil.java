package com.zime.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class OSSContentTypeUtil {

  private static Logger logger = LoggerFactory.getLogger(OSSContentTypeUtil.class);

  private static Map<String, String> map = null;

  public static void init(String propertieFilePath) {
    try {
      OSSContentTypeUtil.map = FileUtil.loadFileProperties(propertieFilePath);
    } catch (IOException e) {
      logger.error("", e);
    }
  }

  public static String getContentType(String key) {
    if (map != null) {
      return map.get(key);
    }
    return null;
  }

}
