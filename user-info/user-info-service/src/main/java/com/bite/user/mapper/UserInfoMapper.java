package com.bite.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bite.user.dataobject.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
