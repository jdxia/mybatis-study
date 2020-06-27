package org.example.dao;

import org.example.pojo.User;

import java.util.List;

public interface IUserDao {
    public List<User> selectAllUser() throws Exception;

    public User selectUser(User user) throws Exception;
}
