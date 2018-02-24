package com.zime.web.controller.backend;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zime.web.contants.Const;
import com.zime.web.dao.entity.User;
import com.zime.web.service.UserService;
import com.zime.web.util.ResponseEntity;
import com.zime.web.util.ResponseEntityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description="后台用户操作接口",produces = "application/json")
@RestController
@RequestMapping("/manage/user/")
public class UserManageController {
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "管理员登录接口",notes = "后台用户登录")
	@PostMapping(value="login.do")
	public ResponseEntity<User> login(String username, String password, HttpSession session ) {
		ResponseEntity<User> response=userService.login(username, password);
		if(response.isSuccess()) {
			User user= response.getData();
			if(user.getRole()==Const.Role.ROLE_ADMIN) {
				session.setAttribute(Const.CURRENT_USER, user);
				return response;
			}
			else {
				return ResponseEntityUtil.fail("不是管理员，无法登录");
			}
		}
		return response;
	}

}
