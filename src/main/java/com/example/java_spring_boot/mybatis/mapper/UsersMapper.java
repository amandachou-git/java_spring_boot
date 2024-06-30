package com.example.java_spring_boot.mybatis.mapper;

import com.example.java_spring_boot.dao.entity.UsersEntity;
import com.example.java_spring_boot.enums.UserAuthorityEnum;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UsersMapper {
    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "authority", column = "authority", javaType = UserAuthorityEnum.class),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createDt", column = "create_dt"),
            @Result(property = "lastModifiedBy", column = "last_modified_by"),
            @Result(property = "lastModifiedDt", column = "last_modified_dt")
    })
    List<UsersEntity> findAll();

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "authority", column = "authority", javaType = UserAuthorityEnum.class),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createDt", column = "create_dt"),
            @Result(property = "lastModifiedBy", column = "last_modified_by"),
            @Result(property = "lastModifiedDt", column = "last_modified_dt")
    })
    UsersEntity findById(String id);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "authority", column = "authority", javaType = UserAuthorityEnum.class),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "premium", column = "premium"),
            @Result(property = "trailExpiration", column = "trail_expiration"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createDt", column = "create_dt"),
            @Result(property = "lastModifiedBy", column = "last_modified_by"),
            @Result(property = "lastModifiedDt", column = "last_modified_dt")
    })
    UsersEntity findByEmail(String useremail);

    // insert, update, delete語句是不需要返回值的，它們都是默認返回一個int
    @Insert("""
            INSERT INTO users (email, password, authority, enabled, premium, create_by, create_dt)
            VALUES (#{email}, #{password}, #{authority}, #{enabled}, #{premium}, #{createBy}, #{createDt})
            """)
    int insert(UsersEntity users);

    @Update("""
            UPDATE users SET
                email = #{email},
                password = #{password},
                authority = #{authority},
                last_modified_by = #{lastModifiedBy},
                last_modified_dt = #{lastModifiedDt}
            WHERE id = #{id}
            """)
    int update(UsersEntity users);

    @Delete("DELETE FROM users WHERE id = #{id}")
    int delete(String id);

    @Select(
            "<script>" +
            "SELECT * FROM users WHERE authority IN " +
            "<foreach collection='authorities' item='item' open='(' separator=',' close=')'> " +
                "#{item}" +
            "</foreach>" +
            "</script>"
    )
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "authority", column = "authority", javaType = UserAuthorityEnum.class),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createDt", column = "create_dt"),
            @Result(property = "lastModifiedBy", column = "last_modified_by"),
            @Result(property = "lastModifiedDt", column = "last_modified_dt")
    })
    List<UsersEntity> findByAuthorityIn(@Param("authorities") List<UserAuthorityEnum> authorities);
}
