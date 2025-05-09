package com.bite.user.controller;

import com.bite.user.service.UserService;
import com.bite.common.pojo.Result;
import com.bite.user.api.UserServiceApi;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController implements UserServiceApi {

    @Autowired
    private UserService userService;

    @Override
    public Result<UserLoginResponse> login(@Validated @RequestBody UserInfoRequest user){
        log.info("用户登录, userName: {}", user.getUserName());
        return Result.success(userService.login(user));
    }
    @Override
    public Result<UserInfoResponse> getUserInfo(@NotNull Integer userId){

        return Result.success(userService.getUserInfo(userId));
    }
    @Override
    public Result<UserInfoResponse> getAuthorInfo(@NotNull Integer blogId){
        return Result.success(userService.selectAuthorInfoByBlogId(blogId));
    }

    @Override
    public Result<Integer> register(@Validated @RequestBody UserInfoRegisterRequest registerRequest) {
        return Result.success(userService.register(registerRequest));
    }
}
