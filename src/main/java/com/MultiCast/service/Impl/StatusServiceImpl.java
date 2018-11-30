package com.MultiCast.service.Impl;

import com.MultiCast.model.Status;
import com.MultiCast.repository.StatusRepository;
import com.MultiCast.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    StatusRepository statusRepository;


    @Override
    public Status getStatusByHostname(String hostname) {
        return statusRepository.findByHostname(hostname);
    }

    @Override
    public void updateStatus(Status status) {
        statusRepository.saveAndFlush(status);
    }
}
