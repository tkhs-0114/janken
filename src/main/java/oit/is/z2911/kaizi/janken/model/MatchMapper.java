package oit.is.z2911.kaizi.janken.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface MatchMapper {
  @Select("SELECT * from matches WHERE isActive = #{isActive}")
  ArrayList<Match> selectByActive(boolean isActive);

  @Select("SELECT * from matches WHERE id = #{id}")
  Match selectById(int id);

  @Select("SELECT * from matches WHERE user1 = #{user2} and user2 = #{user1} and isActive = true limit 1")
  Match selectByUsers(int user1, int user2);

  @Update("UPDATE matches SET user2Hand = #{user2Hand}, isActive = false WHERE id = #{id}")
  void finMatch(int id, String user2Hand);

  @Insert("INSERT INTO matches (user1, user2, user1Hand, isActive) VALUES (#{user1}, #{user2}, #{user1Hand}, #{isActive})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertMatch(Match match);
}
