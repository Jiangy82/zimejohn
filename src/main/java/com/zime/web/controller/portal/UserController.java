package com.zime.web.controller.portal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.dao.entity.User;
import com.zime.web.service.UserService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="前端用户操作接口",produces = "application/json")
@RestController
@RequestMapping("/user/")
public class UserController {

	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "登录接口",notes = "用户登录")
	@PostMapping(value="login.do")
	@ResponseBody
	public ResponseEntity<User> login(String username, String password, HttpSession session ) {
		ResponseEntity<User> response=userService.login(username, password);
		if(response.isSuccess()) {
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	
	@ApiOperation(value = "登出接口",notes = "用户登出")
	@PostMapping(value="logout.do")	
	public ResponseEntity<String> logout(HttpSession session){
		session.removeAttribute(Const.CURRENT_USER);
		return ResponseEntityUtil.success();
	}
	
	@ApiOperation(value = "用户注册接口",notes = "用户注册")
	@PostMapping(value="register.do")	
	public ResponseEntity<String> register(User user){
		return userService.register(user);
	}
	
	@ApiOperation(value = "用户校验接口",notes = "信息校验")
	@PostMapping(value="check_valid.do")
	public ResponseEntity<String> checkValid(String str, String type){
		return userService.checkValid(str, type);
	}
	
	@ApiOperation(value = "获取用户信息接口",notes = "获取用户信息")
	@PostMapping(value="get_user_info.do")
	public ResponseEntity<User> getUserInfo(HttpSession session){
		User user= (User) session.getAttribute(Const.CURRENT_USER);
		if(user !=null) {
			return ResponseEntityUtil.success(user);
		}
		return ResponseEntityUtil.fail("用户未登录，无法获取当前用户信息");
	}
	
	@ApiOperation(value = "密保提示问题接口",notes = "获取密保提示问题")
	@PostMapping(value="forget_get_question.do")
	public ResponseEntity<String> selectQuestion(String username) {
		return userService.selectQuestion(username);
	}
	
	@ApiOperation(value = "确认密保问题答案接口",notes = "确认密保问题答案")
	@PostMapping(value="forget_check_answer.do")
	public ResponseEntity<String> forgetCheckAnswer(String username, String question, String answer){
		return userService.checkAnswer(username, question, answer);
	}
	
	@ApiOperation(value = "重置密码接口",notes = "重置密码")
	@PostMapping(value="forget_reset_password.do")
	public ResponseEntity<String> forgetResetPassword(String username, String newPassword, String forgetToken){
		return userService.forgetResetPassword(username, newPassword, forgetToken);
	}
	
	@ApiOperation(value = "登录状态下重置密码接口",notes = "登录状态下重置密码")
	@PostMapping(value="reset_password.do")	
	public ResponseEntity<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
	       User user= (User)session.getAttribute(Const.CURRENT_USER);
	       if(user==null) {
	    	   return ResponseEntityUtil.fail("用户未登录");
	       }
	       return userService.resetPassword(passwordOld, passwordNew, user);
	}
	@ApiOperation(value = "更新用户信息接口",notes = "更新用户信息")
	@PostMapping(value="update_information.do")
	public ResponseEntity<User> update_information(HttpSession session, User user){
		User currentUser= (User)session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ResponseEntityUtil.fail("用户未登录");
		}
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		ResponseEntity<User> response= userService.updateInformation(user);
		if(response.isSuccess()) {
			response.getData().setUsername(currentUser.getUsername());
			session.setAttribute(Const.CURRENT_USER, response.getData());
		}
		return response;
	}
	@ApiOperation(value = "获取用户信息接口",notes = "获取用户信息")
	@PostMapping(value="get_information.do")
	public ResponseEntity<User> get_information(HttpSession session){
		User currentUser= (User)session.getAttribute(Const.CURRENT_USER);
		if(currentUser==null) {
			return ResponseEntityUtil.fail(5, "用户未登录");
		}
		return userService.getInformation(currentUser.getId());
	}
}
