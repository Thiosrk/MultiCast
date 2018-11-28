package com.MultiCast.service;

import com.MultiCast.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> getAll();

    User getUserById(int id);

    User getUserByUserName(String username);

    void addUser(String username,String password);

    User modifyUser(User user);

    void delete(int userid);

}
