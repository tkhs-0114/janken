package oit.is.z2911.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchInfoMapper {
  @Select("SELECT * from matchinfo WHERE isActive = true")
  ArrayList<MatchInfo> selectActive();

  @Select("SELECT * from matchinfo WHERE id = #{id}")
  MatchInfo selectById(int id);

  @Select("SELECT * from matchinfo WHERE user1 = #{user2} and user2 = #{user1} and isActive = true limit 1")
  MatchInfo selectByUsers(int user1, int user2);

  @Update("UPDATE matchinfo SET isActive = false WHERE id = #{id}")
  void deactivateMatchInfo(int id);

  @Insert("INSERT INTO matchinfo (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  void insertMatchInfo(int user1, int user2, String user1Hand, boolean isActive);
}
