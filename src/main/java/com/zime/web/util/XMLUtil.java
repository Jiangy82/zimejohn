package com.zime.web.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by FirenzesEagle on 2016/7/7 0007. Email:liumingbo2008@gmail.com
 */
public class XMLUtil {

  /**
   * 将微信服务器发送的Request请求中Body的XML解析为Map
   *
   * @param request
   * @return
   * @throws Exception
   */
  public static Map<String, String> parseRequestXmlToMap(HttpServletRequest request) throws Exception {
    // 解析结果存储在HashMap中
    Map<String, String> resultMap;
    InputStream inputStream = request.getInputStream();
    resultMap = parseInputStreamToMap(inputStream);
    return resultMap;
  }

  /**
   * 将输入流中的XML解析为Map
   *
   * @param inputStream
   * @return
   * @throws DocumentException
   * @throws IOException
   */
  @SuppressWarnings("unchecked")
  public static Map<String, String> parseInputStreamToMap(InputStream inputStream) throws DocumentException, IOException {
    // 解析结果存储在HashMap中
    Map<String, String> map = new HashMap<String, String>();
    // 读取输入流
    SAXReader reader = new SAXReader();
    Document document = reader.read(inputStream);
    // 得到xml根元素
    Element root = document.getRootElement();
    // 得到根元素的所有子节点
    List<Element> elementList = root.elements();
    // 遍历所有子节点
    for (Element e : elementList) {
      map.put(e.getName(), e.getText());
    }
    // 释放资源
    inputStream.close();
    return map;
  }

  /**
   * 将String类型的XML解析为Map
   *
   * @param str
   * @return
   * @throws Exception
   */
  public static Map<String, String> parseXmlStringToMap(String str) throws Exception {
    Map<String, String> resultMap;
    InputStream inputStream = new ByteArrayInputStream(str.getBytes("UTF-8"));
    resultMap = parseInputStreamToMap(inputStream);
    return resultMap;
  }


  private static String charset = "UTF-8";

  /**
   * 转成xml字符串
   * 
   * @param obj obj必须标注@XmlRootElement
   * @return
   */
  public static String toXml(Object obj) {
    StringWriter writer = null;
    try {
      JAXBContext context = JAXBContext.newInstance(obj.getClass());
      Marshaller marshaller = context.createMarshaller();
      // 设置编码格式
      marshaller.setProperty(Marshaller.JAXB_ENCODING, charset);
      // 设置是否格式化
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
      // 省略xm头声明信息
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      writer = new StringWriter();
      marshaller.marshal(obj, new StreamResult(writer));
      String result = writer.toString();
      return result;
    } catch (Exception e) {
      e.printStackTrace();;
      throw new RuntimeException(e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * xml转成对象
   * 
   * @param xml
   * @param c
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T> T toObject(String xml, Class<T> c) {
    if (xml == null || "".equals(xml.trim())) {
      return null;
    }
    StringReader reader = null;
    try {
      JAXBContext context = JAXBContext.newInstance(c);
      javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
      reader = new StringReader(xml);
      T result = (T) unmarshaller.unmarshal(new StreamSource(reader));
      return result;
    } catch (Exception e) {
      e.printStackTrace();;
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * 获取返回内容
   * 
   * @param request
   * @return
   */
  public static String streamToXml(HttpServletRequest request) {
    StringBuffer buffer = new StringBuffer();
    try {
      InputStream in = request.getInputStream();
      byte[] b = new byte[4096];
      for (int n; (n = in.read(b)) != -1;) {
        buffer.append(new String(b, 0, n, "UTF-8"));
      }
      in.close();
    } catch (Exception e) {
    }
    return buffer.toString();
  }
}
