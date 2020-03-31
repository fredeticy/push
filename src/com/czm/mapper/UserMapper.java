package com.czm.mapper;

import org.apache.ibatis.annotations.Param;

import com.czm.po.User;

public interface UserMapper {
	
	public User findUserById(@Param("userid")String userid);
}
