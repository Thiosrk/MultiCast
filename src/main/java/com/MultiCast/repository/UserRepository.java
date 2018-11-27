package com.MultiCast.repository;

import com.MultiCast.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUsername(String username);

}
