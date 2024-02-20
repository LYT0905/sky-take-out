package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Mark
 * @date 2024/2/15
 */

@Mapper
public interface UserMapper {
    /**
     * 根据openid查找用户
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User getByOpenId(String openId);

    /**
     * 插入用户
     * @param user
     */
    void insert(User user);


    @Select("select * from user where id = #{id}")
    User getById(Long userId);
}
