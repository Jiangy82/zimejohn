package com.zime.web.controller;

import com.zime.web.dao.entity.Member;
import com.zime.web.dao.entity.User;
import com.zime.web.service.MemberService;
import com.zime.web.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LJH
 * @version 1.0
 * @Date 2018/1/16 16:55
 */
@Api(description="第一个接口描述",produces = "application/json")
@RestController
@RequestMapping("first")
public class TestController {

    @Resource
    private MemberService memberService;
    @Resource
    private UserService userService;

    @ApiOperation(value = "hello接口",notes = "hello笔记")
    @GetMapping("hello")
    public ResponseEntity first(){
        return ResponseEntity.ok("okok");
    }

    @ApiOperation(value = "会员信息")
    @GetMapping("member")
    public ResponseEntity<Member> member(){
        return ResponseEntity.ok(memberService.byMemberId("1"));
    }
    
//    @ApiOperation(value = "用户信息")
//    @GetMapping("user")    
//    public ResponseEntity<User> user(){
//    	return ResponseEntity.ok(userService.byUserId(1));
//    }
//    
//    @ApiOperation(value = "查找用户")
//    @PostMapping("chkuser")     
//    public ResponseEntity<Boolean>  checkUser(@RequestParam String username) {
//    	return ResponseEntity.ok(userService.checkUserName(username)>0?true:false);
//    }



}
