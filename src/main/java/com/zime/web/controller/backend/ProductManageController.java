package com.zime.web.controller.backend;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.zime.web.config.FtpConfig;
import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.Product;
import com.zime.web.dao.entity.User;
import com.zime.web.service.FileService;
import com.zime.web.service.ProductService;
import com.zime.web.service.UserService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="产品管理操作接口",produces = "application/json")
@RestController
@RequestMapping("/manage/product/")
public class ProductManageController {
	@Autowired
	private UserService userService;
	@Autowired
	private ProductService productService;
	@Autowired
	private FileService fileService;
	@Resource
	private FtpConfig ftpConfig;

	@ApiOperation(value = "新增或更新产品操作接口",notes = "新增或更新产品")
	@PostMapping(value="save.do")	
	public ResponseEntity productSave(HttpSession session, Product product) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//产品添加的业务逻辑
			return productService.saveorUpdateProduct(product);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	@ApiOperation(value = "修改产品销售状态操作接口",notes = "修改产品销售状态")
	@PostMapping(value="set_sale_status.do")	
	public ResponseEntity setSaleStatus(HttpSession session, Integer productId, Integer status) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//修改产品销售状态逻辑
			return productService.setSaleStatus(productId,status);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}	
	
	@ApiOperation(value = "获取产品详情操作接口",notes = "获取产品详情")
	@PostMapping(value="detail.do")	
	public ResponseEntity getDetail(HttpSession session, Integer productId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品详情
			return productService.manageProductDetail(productId);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	@ApiOperation(value = "获取产品列表操作接口",notes = "获取产品列表")
	@PostMapping(value="list.do")	
	public ResponseEntity getList(HttpSession session,@RequestParam(value="pageNum", defaultValue="1") int pageNum,  @RequestParam(value="pageSize", defaultValue="10") int pageSize) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return productService.getProductList(pageNum, pageSize);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	
	@ApiOperation(value = "产品搜索操作接口",notes = "产品搜索")
	@PostMapping(value="search.do")	
	public ResponseEntity productSearch(HttpSession session, String productName, Integer productId, @RequestParam(value="pageNum", defaultValue="1") int pageNum,  @RequestParam(value="pageSize", defaultValue="10") int pageSize) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//获取产品列表业务逻辑
			return productService.searchProducts(productName, productId, pageNum, pageSize);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}
	
	@ApiOperation(value = "文件上传接口",notes = "文件上传")
	@PostMapping(value="upload.do")	
	public ResponseEntity upload(HttpSession session, @RequestParam(value="upload_file", required= false) MultipartFile file,HttpServletRequest request ) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "请先以管理员身份登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			String path= request.getSession().getServletContext().getRealPath("upload");
			String targetFileName= fileService.upload(file, path);
			String url= ftpConfig.getServerHttpPrefix()+targetFileName;
			
			Map fileMap= Maps.newHashMap();
			fileMap.put("uri", targetFileName);
			fileMap.put("url", url);
			return ResponseEntityUtil.success(fileMap);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(), "无权限操作");
		}
	}

	
	@ApiOperation(value = "富文件编辑器文件上传接口",notes = "文件上传")
	@PostMapping(value="richtext_img_upload.do")	
	public Map richtextImgUpload(HttpSession session, @RequestParam(value="upload_file", required= false) MultipartFile file,HttpServletRequest request, HttpServletResponse response ) {
		Map resultMap= Maps.newHashMap();
		
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			resultMap.put("success", false);
			resultMap.put("msg", "请以管理员身份登录");
			return resultMap;
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			String path= request.getSession().getServletContext().getRealPath("upload");
			String targetFileName= fileService.upload(file, path);
			
			if(StringUtils.isBlank(targetFileName)) {
				resultMap.put("success", false);
				resultMap.put("msg", "上传失败");
				return resultMap;
			}
			String url= ftpConfig.getServerHttpPrefix()+targetFileName;
			
			resultMap.put("success", false);
			resultMap.put("msg", "上传成功");
			resultMap.put("file_path", url);
			response.addHeader("Access_Controller_Allow_Headers", "X_File_Name");
			return resultMap;

		}else {
			resultMap.put("success", false);
			resultMap.put("msg", "无权限操作");
			return resultMap;
		}
	}
}
