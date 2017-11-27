package com.example.wenDaService.dao;

import com.example.wenDaService.model.Question;
import com.example.wenDaService.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pantingting on 2017/8/12.
 */
@Repository
@Mapper
public interface QuestionDAO {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title,content,created_date,user_id,comment_count ";
    String SELECT_FIELDS = " id, "+INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME,"(",INSERT_FIELDS,") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //找出所有数据
    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME})
    List<Question> selectAllQuestions();

    //这里曾经犯过的错误，mybatis的xml文件在resource下面的目录，有一级写错了，
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                               @Param("offset") int offset,
                               @Param("limit")int limit);

    @Update({"update",TABLE_NAME,"set password = #{password} where id = #{id}"})
    void updatePassword(User user) ;

    @Update({"update",TABLE_NAME,"set comment_count = #{commentCount} where id=#{id}"})
    void updateCommentCount(@Param("id") int id,
                            @Param("commentCount") int commentCount);

    @Delete({"delete from",TABLE_NAME,"where id = #{id}"})
    void deleteById(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
    Question selectById(int id);
}

