package com.zealyoung.bookmanager.dao;

import com.zealyoung.bookmanager.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
    @Author:Zealyoung
 */
@Repository
@Mapper
public interface UserDAO {

    @Insert({"insert into user(name, email, password) " +
            "values(#{name},#{email},#{password})"})
    int addUser(User user);

    @Select({"select * from user where id = #{id}"})
    User selectById(int id);

    @Select({"select * from user where name = #{name}"})
    User selectByName(String name);

    @Select({"select * from user where email = #{email}"})
    User selectByEmail(String email);
}
