package com.MultiCast.service.Impl;

import com.MultiCast.model.User;
import com.MultiCast.repository.UserRepository;
import com.MultiCast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addUser(String username,String password) {
        User user = new User(username,password,0);
        userRepository.save(user);
    }

    @Override
    public User modifyUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(int userid) {
        userRepository.deleteById(userid);
    }
}
