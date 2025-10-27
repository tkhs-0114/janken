package oit.is.z2911.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
  @Select("SELECT * from users")
  ArrayList<User> selectAll();

  @Select("SELECT * from users where id = #{id}")
  User selectByUserId(int id);

  @Select("SELECT * from users where userName = #{name}")
  User selectByUserName(String name);
}
