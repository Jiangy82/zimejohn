package com.zime.web.controller;

import com.zime.web.contants.Errors;
import com.zime.web.controller.request.UploadImageBase64RequestBean;
import com.zime.web.service.OssService;

import com.zime.web.util.ExceptionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@SuppressWarnings("restriction")
@Api(description = "文件上传", produces = "application/json")
@RestController
@RequestMapping("/file")
public class FileUploadController   {
  @Resource
  private OssService ossService;

  /**
   * 上传图片
   * 
   * @param file
   * @param request
   * @return
   * @throws IOException
   */
  @ApiOperation(value = "上传图片", notes = "上传图片<br/>http://aligreen.alibaba.com/porn.html,在此检测rate超过80的为涉黄图片，会上传失败")
  @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
    // 尺寸验证
    String filePath = ossService.upload(file);
    return ResponseEntity.ok(filePath);
  }

  /**
   * 上传图片
   * 
   * @param file
   * @param request
   * @return
   * @throws IOException
   */
  @ApiOperation(value = "上传图片ie浏览器", notes = "上传图片ie浏览器", consumes = "application/json")
  @RequestMapping(value = "/uploadImageIe", method = RequestMethod.POST)
  public void uploadImageIe(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    // 尺寸验证
    measurementValidation(file.getInputStream());
    String filePath = ossService.upload(file);
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.write(filePath);
    out.flush();
    out.close();
  }

  /**
   * 直接上传图片(base64Code方式)
   * 
   * @param request
   * @return
   * @throws IOException
   */
  @ApiOperation(value = "上传图片base64Code方式", notes = "上传图片base64Code方式压缩", consumes = "application/json")
  @RequestMapping(value = "/uploadImageBase64", method = RequestMethod.POST)
  public ResponseEntity<String> uploadImageBase64(@RequestBody UploadImageBase64RequestBean bean, HttpServletRequest request)
      throws IOException {
    byte[] imgByte =
        new BASE64Decoder().decodeBuffer(bean.getImage().substring(bean.getImage().indexOf(",") + 1, bean.getImage().length()));
    // 尺寸验证
    String filePath = ossService.uploadImageBase64(imgByte);
    return ResponseEntity.ok(filePath);
  }

  /**
   * 上传音频
   * 
   * @param file
   * @param request
   * @return
   */
  @ApiOperation(value = "上传音频", notes = "上传音频", consumes = "application/json")
  @RequestMapping(value = "/uploadAudio", method = RequestMethod.POST)
  public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    String filePath = ossService.upload(file);
    return ResponseEntity.ok(filePath);
  }

  /**
   * 图片尺寸校验
   */
  private void measurementValidation(InputStream is) {
    BufferedImage source = null;
    try {
      source = ImageIO.read(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
    int owidth = source.getWidth();
    int oheight = source.getHeight();
    if (owidth > 1000 || oheight > 1000) {
      ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "图片尺寸过大");
    }
  }
}
