package com.zime.web.controller.backend;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.contants.Errors;
import com.zime.web.dao.entity.User;
import com.zime.web.service.CategoryService;
import com.zime.web.service.UserService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="分类管理操作接口",produces = "application/json")
@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private CategoryService categoryService;
	
	@ApiOperation(value = "添加分类操作接口",notes = "添加分类")
	@PostMapping(value="add_category.do")	
	public ResponseEntity addCategory(HttpSession session, String categoryName, @RequestParam(value="parentId", defaultValue="0")int parentId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "用户未登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			return categoryService.addCategory(categoryName, parentId);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(),"无权限操作，需要管理员权限");
		}
	}
	
	@ApiOperation(value = "修改分类名称操作接口",notes = "修改分类名称")
	@PostMapping(value="set_category.do")	
	public ResponseEntity setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "用户未登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			return categoryService.updateCategoryName(categoryId, categoryName);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(),"无权限操作，需要管理员权限");
		}
	}
	@ApiOperation(value = "获取分类操作接口",notes = "获取分类")
	@PostMapping(value="get_category.do")
	public ResponseEntity getChildParallelCategory(HttpSession session, Integer categoryId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "用户未登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			return categoryService.getChildParallelCategory(categoryId);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(),"无权限操作，需要管理员权限");
		}
	}

	@ApiOperation(value = "递归获取分类操作接口",notes = "递归获取分类")
	@PostMapping(value="get_deep_category.do")
	public ResponseEntity getCategorAndDeepChildCategory(HttpSession session, Integer categoryId) {
		User user= (User)session.getAttribute(Const.CURRENT_USER);
		if(user==null) {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NOT_LOGIN.getCode(), "用户未登录");
		}
		if(userService.checkAdminRole(user).isSuccess()) {
			//查询当前节点和节点的所有子节点
			return categoryService.selectCategoryAndChildrenById(categoryId);
		}else {
			return ResponseEntityUtil.fail(Errors.SYSTEM_NO_ACCESS.getCode(),"无权限操作，需要管理员权限");
		}
	}
}
