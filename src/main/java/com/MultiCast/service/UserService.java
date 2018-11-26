package com.MultiCast.service;

import com.MultiCast.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public List<User> getAll();

    public User getUserById(int id);

    public User getUserByUserName(String username);

    public void createUser(User user);


}
