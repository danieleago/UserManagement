package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.api.model.User;

import java.util.List;

@Mapper
public interface UserManagementMapper {

    User getUserById(@Param("id") int id);

    List<User> searchUser(@Param("keyword") String keyword);
    int insertUser(@Param("user") User user);

    void bulkUser(@Param("userList") List<User> userList);

    boolean updateUser(@Param("user") User user);

    boolean deleteUser(@Param("id") int id);
}
