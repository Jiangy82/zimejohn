package com.zime.web.service;

import com.zime.web.dao.entity.Member;

/**
 * @author LJH
 * @version 1.0
 * @date 2018/1/24 10:10
 */
public interface MemberService {

    /**
     * 会员信息
     * @param memberId
     * @return
     */
    Member byMemberId(String memberId);


    /**
     * 手机号是否已存在
     *
     * @param mobile
     * @return true：是，false：不存在
     */
    boolean mobileExist(String mobile);


}
