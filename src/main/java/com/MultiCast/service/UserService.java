package com.MultiCast.service;

import com.MultiCast.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {

    List<User> getAll();

    User getUserById(int id);

    User getUserByUserName(String username);

    void saveUser(User user);

    User modifyUser(User user);

    void delete(int userid);

}
