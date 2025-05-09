package com.bite.user.convert;

import com.bite.common.utils.SecurityUtil;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.dataobject.UserInfo;

public class BeanConvert {
    public static UserInfo convertUserInfoByEncrypt(UserInfoRegisterRequest registerRequest){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(registerRequest.getUserName());
        userInfo.setPassword(SecurityUtil.encrypt(registerRequest.getPassword()));
        userInfo.setGithubUrl(registerRequest.getGithubUrl());
        userInfo.setEmail(registerRequest.getEmail());
        return userInfo;

    }
}
