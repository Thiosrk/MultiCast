package com.MultiCast.repository;

import com.MultiCast.model.Status;
import com.MultiCast.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StatusRepository extends JpaRepository<Status,Integer> {

    Status findByHostname(String hostname);

}
