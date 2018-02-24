package com.zime.web.service.impl;

import com.zime.web.dao.entity.Member;
import com.zime.web.dao.entity.User;
import com.zime.web.dao.mapper.MemberMapper;
import com.zime.web.dao.mapper.UserMapper;
import com.zime.web.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author LJH
 * @version 1.0
 * @date 2018/1/24 10:11
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Override
    public Member byMemberId(String memberId) {
        return memberMapper.selectByPrimaryKey(memberId);
    }

    /**
     * 手机号是否已存在
     *
     * @param mobile
     * @return true：是，false：不存在
     */
    @Override
    public boolean mobileExist(String mobile) {
        return this.memberMapper.mobileExist(mobile);
    }
}
