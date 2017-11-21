package com.example.wenDaService.dao;

import com.example.wenDaService.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by pantingting on 2017/8/12.
 */
@Repository
@Mapper
public interface UserDAO {
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name,password,salt,head_url ";
    String SELECT_FIELDS = " id, "+INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME,"(",INSERT_FIELDS,") values(#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME,"where id = #{id}"})
    User selectByid(int id);

    @Select({"select",SELECT_FIELDS,"from",TABLE_NAME})
    List<User> selectAllUser();

    @Update({"update",TABLE_NAME,"set password = #{password} where id = #{id}"})
    void updatePassword(User user) ;

    @Delete({"delete from",TABLE_NAME,"where id = #{id}"})
    void deleteById(int id);
}

