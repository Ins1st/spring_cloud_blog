package com.bite.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.bite.blog.BlogServiceApi;
import com.bite.common.constant.Constants;
import com.bite.common.utils.*;
import com.bite.user.convert.BeanConvert;
import com.bite.user.dataobject.UserInfo;
import com.bite.user.mapper.UserInfoMapper;
import com.bite.blog.pojo.BlogInfoResponse;
import com.bite.user.service.UserService;
import com.bite.common.exception.BlogException;
import com.bite.common.pojo.Result;
import com.bite.user.api.pojo.UserInfoRegisterRequest;
import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private BlogServiceApi blogServiceApi;
    @Autowired
    private Redis redis;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //超时时间为两周
    private static final long EXPIRE_TIME=14*14*60*60;
    private static final String USER_PREFIX="user";
    @Override
    public UserLoginResponse login(UserInfoRequest user) {
        //验证账号密码是否正确
        UserInfo userInfo = queryUserInfo(user.getUserName());
        if (userInfo==null || userInfo.getId()==null){
            throw new BlogException("用户不存在");
        }

        if (!SecurityUtil.verify(user.getPassword(),userInfo.getPassword())){
            throw new BlogException("用户密码不正确");
        }
        //账号密码正确的逻辑
        Map<String,Object> claims = new HashMap<>();
        claims.put("id", userInfo.getId());
        claims.put("name", userInfo.getUserName());

        String jwt = JWTUtils.genJwt(claims);
        return new UserLoginResponse(userInfo.getId(), jwt);
    }

  

    @Override
    public UserInfoResponse getUserInfo(Integer userId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        UserInfo userInfo = selectUserInfoById(userId);
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public UserInfoResponse selectAuthorInfoByBlogId(Integer blogId) {
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        //远程调用

        //1. 根据博客ID, 获取作者ID
       Result<BlogInfoResponse> blogDeatail = blogServiceApi.getBlogDeatail(blogId);
        //2. 根据作者ID, 获取作者信息
        if (blogDeatail == null || blogDeatail.getData() == null){
            throw new BlogException("博客不存在");
        }
        UserInfo userInfo = selectUserInfoById(blogDeatail.getData().getUserId());
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }

    @Override
    public Integer register(UserInfoRegisterRequest registerRequest) {
        //校验参数：用户名不能重复，密码格式，邮箱格式，url格式
        checkUserInfo(registerRequest);
        //用户注册：插入数据库
        UserInfo userInfo = BeanConvert.convertUserInfoByEncrypt(registerRequest);
        try {
            int result = userInfoMapper.insert(userInfo);
            if (result == 1) {
                //存储数据到redis中
                //redis 存储失败，会导致查询时差不多信息，那么就从数据库中查询，那么此处异常就不处理了
                redis.set(buildKey(userInfo.getUserName()), JsonUtil.toJson(userInfo),EXPIRE_TIME);
                //发送消息
                userInfo.setPassword("");
                rabbitTemplate.convertAndSend(Constants.USER_EXCHANGE_NAME,"",JsonUtil.toJson(userInfo));
                return userInfo.getId();
            } else {
                throw new BlogException("用户注册失败");
            }

        } catch (Exception e) {
            log.error("用户登陆失败,e:",e);
            throw new BlogException("用户注册失败");
        }


    }

    private String buildKey(String userName) {
        return redis.buildKey(USER_PREFIX,userName);
    }

    private void checkUserInfo(UserInfoRegisterRequest param) {
    //用户名不能重复
        UserInfo userInfo = selectUserInfoByName(param.getUserName());
        if(userInfo !=null) {
            throw new BlogException("用户名已存在");
        }
        //TODO
        //邮箱格式，url格式
        if(!RegexUtil.checkMail((param.getEmail()))) {
            throw new BlogException("邮箱格式不合法");
        }
        if(!RegexUtil.checkURL(param.getGithubUrl())){
            throw new BlogException("githubUrl格式不合法");
        }
    }
    
    private UserInfo queryUserInfo(String userName) {
    //先从redis中获取数据，如果redis中不存在，从数据库中读取
        String key = buildKey(userName);
        boolean exists = redis.hasKey(key);
        if(exists) {
            //从redis中取数据
            log.info("从redis中获取数据，key:{}",key);
            String userJson = redis.get(key);
            UserInfo userInfo = JsonUtil.parseJson(userJson,UserInfo.class);
            return userInfo==null?selectUserInfoByName(userName):userInfo;
        }else{
            //从数据库中取数据
            log.info("从mysql中获取数据,userName:{}",userName);
            UserInfo userInfo = selectUserInfoByName(userName);
            //把数据库中的数据存储到redis中
            redis.set(key,JsonUtil.toJson(userInfo),EXPIRE_TIME);
            return userInfo;
        }
    }

    public UserInfo selectUserInfoByName(String userName) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUserName, userName).eq(UserInfo::getDeleteFlag, 0));
    }
    private UserInfo selectUserInfoById(Integer userId) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, userId).eq(UserInfo::getDeleteFlag, 0));
    }
    

}
