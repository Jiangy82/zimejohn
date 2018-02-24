package com.zime.web.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.zime.web.config.OSSConfig;
import com.zime.web.contants.Errors;
import com.zime.web.service.OssService;
import com.zime.web.util.ExceptionUtil;
import com.zime.web.util.FileUtil;
import com.zime.web.util.OSSContentTypeUtil;
import com.zime.web.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 */
@Service("ossService")
public class OssServiceImpl implements OssService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private OSSConfig ossConfig;
    @Resource
    private OSSClient uploadOSSClient;
    @Resource
    private OSSClient downloadOSSClient;

    /**
     * oss上传文件，返回文件访问路径
     *
     * @param file：文件
     * @return
     */
    @Override
    public String upload(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String suffixName = originFileName.substring(originFileName.indexOf(".") + 1);
        String fileType = OSSContentTypeUtil.getContentType(suffixName);
        // 设置文件名
        String filePathName = generateRelativeStoragePath(suffixName);
        byte[] fileContent = null;
        try {
            fileContent = file.getBytes();
        } catch (Exception e) {
            logger.error("Cannot get file content from {}.", originFileName);
            ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "不能读取" + originFileName + "内容");
        }
        ObjectMetadata meta = new ObjectMetadata();
        // 设置上传文件长度
        meta.setContentLength(file.getSize());
        // 设置上传MD5校验
        String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(fileContent));
        meta.setContentMD5(md5);
        meta.setContentType(fileType);

        // 存储
        try {
            uploadOSSClient.putObject(ossConfig.getBucketName(), filePathName, file.getInputStream(), meta);
        } catch (OSSException | ClientException | IOException e) {
            logger.error("OSS storage error", e);
            ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "OSS storage exception");
        }
        String path = ossConfig.getDownloadEndpoint() + FileUtil.getFileSeparator() + filePathName;
        if (FileUtil.isImg(suffixName)) {
            // 图片访问处理样式，可在oss自定义,缩放、裁剪、压缩、旋转、格式、锐化、水印等
            path += StringUtils.isNotBlank(ossConfig.getStyleName()) ? "?x-oss-process=style/" + ossConfig.getStyleName() : "";
        }
        return path;
    }

    /**
     * base64code方式上传
     *
     * @param imageBytes 文件流
     * @return
     */
    @Override
    public String uploadImageBase64(byte[] imageBytes) {
        String fileType = "image/jpeg";
        // 设置文件名
        String filePathName = generateRelativeStoragePath("jpeg");
        ObjectMetadata meta = new ObjectMetadata();
        // 设置上传文件长度
        meta.setContentLength(imageBytes.length);
        // 设置上传MD5校验
        String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(imageBytes));
        meta.setContentMD5(md5);
        meta.setContentType(fileType);

        // 存储
        try {
            uploadOSSClient.putObject(ossConfig.getBucketName(), filePathName, new ByteArrayInputStream(imageBytes), meta);
        } catch (Exception e) {
            logger.error("OSS storage error", e);
            ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "OSS storage exception");
        }
        String path = ossConfig.getDownloadEndpoint() + FileUtil.getFileSeparator() + filePathName;
        // 图片访问处理样式，可在oss自定义,缩放、裁剪、压缩、旋转、格式、锐化、水印等
        return path + (StringUtils.isNotBlank(ossConfig.getStyleName()) ? "?x-oss-process=style/" + ossConfig.getStyleName() : "");
    }

    /**
     * File方式上传
     *
     * @param file 文件
     * @return
     */
    @Override
    public String uploadFile(File file) {
        // 存储
        InputStream is = null;
        try {
            String originFileName = file.getName();
            String suffixName = originFileName.substring(originFileName.indexOf(".") + 1);
            String fileType = OSSContentTypeUtil.getContentType(suffixName);
            // String fileType = "application/octet-stream";
            // 设置文件名
            String filePathName = generateRelativeStoragePath(suffixName);
            is = new FileInputStream(file);
            byte[] fileContent = new byte[is.available()];
            is.read(fileContent);
            ObjectMetadata meta = new ObjectMetadata();
            // 设置上传文件长度
            meta.setContentLength(file.length());
            // 设置上传MD5校验
            String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(fileContent));
            meta.setContentMD5(md5);
            meta.setContentType(fileType);
            uploadOSSClient.putObject(ossConfig.getBucketName(), filePathName, new ByteArrayInputStream(fileContent), meta);
            String path = ossConfig.getDownloadEndpoint() + FileUtil.getFileSeparator() + filePathName;
            return path;
        } catch (Exception e) {
            logger.error("OSS storage error", e);
            ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "OSS storage exception");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param url
     * @return
     */
    @Override
    public byte[] download(String url) {
        InputStream is = null;
        try {
            String key = url.split(ossConfig.getDownloadEndpoint() + "/")[1];
            OSSObject ossObject = downloadOSSClient.getObject(ossConfig.getBucketName(), key);
            is = ossObject.getObjectContent();
            byte[] data = IOUtils.readStreamAsByteArray(is);
            return data;
        } catch (Exception e) {
            logger.error("下载文件异常,url={}", url, e);
            e.printStackTrace();
            ExceptionUtil.throwException(Errors.SYSTEM_CUSTOM_ERROR.code, "下载文件异常");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取存储的相对路径 规则path + / + yyyyMMddHH + uuid
     *
     * @param suffixName 后缀名
     * @return
     */
    private String generateRelativeStoragePath(String suffixName) {
        SimpleDateFormat yyyyMMddHH = new SimpleDateFormat("yyyyMMddHH");
        String time = yyyyMMddHH.format(new Date());
        String uuid = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        String storagePath = this.ossConfig.getStoragePath();
        if (StringUtil.isNotBlank(storagePath)) {
            sb.append(storagePath).append("/");
        }
        sb.append(time).append(uuid);
        if (StringUtil.isNotBlank(suffixName)) {
            sb.append(".").append(suffixName);
        }
        return sb.toString();
    }

}
