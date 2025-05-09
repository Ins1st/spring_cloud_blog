package com.bite.user.api;

import com.bite.common.pojo.Result;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(value = "user-service",path = "/user")
public interface UserServiceApi {
    @RequestMapping("/login")
    Result<UserLoginResponse> login(@RequestBody UserInfoRequest user);
    @RequestMapping("/getUserInfo")
    Result<UserInfoResponse> getUserInfo(@RequestParam("userId") Integer userId);
    @RequestMapping("/getAuthorInfo")
    Result<UserInfoResponse> getAuthorInfo(@RequestParam("blogId") Integer blogId);
    @RequestMapping("/register")
    Result<Integer> register(@RequestBody UserInfoRegisterRequest registerRequest);
}
