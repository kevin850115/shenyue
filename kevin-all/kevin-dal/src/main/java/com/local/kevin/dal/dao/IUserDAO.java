package com.local.kevin.dal.dao;

import com.local.kevin.dal.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface IUserDAO {
    @Select("select *from User")
    public List<User> retrieveAllUsers();

    //ע������ֻ��һ����������#{}�еı�ʶ����������ȡ
    @Select("select *from User where id=#{idss}")
    public User retrieveUserById(int id);

    @Select("select *from User where id=#{id} and userName like #{name}")
    public User retrieveUserByIdAndName(@Param("id")int id,@Param("name")String names);

    @Insert("INSERT INTO user(userName,userAge,userAddress) VALUES(#{userName},"
            + "#{userAge},#{userAddress})")
    public void addNewUser(User user);

    @Delete("delete from user where id=#{id}")
    public void deleteUser(int id);

    @Update("update user set userName=#{userName},userAddress=#{userAddress}"
            + " where id=#{id}")
    public void updateUser(User user);
}