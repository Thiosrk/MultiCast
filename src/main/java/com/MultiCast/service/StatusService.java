package com.MultiCast.service;

import com.MultiCast.model.Status;

public interface StatusService {

    Status getStatusByHostname(String hostname);

    void updateStatus(Status status);


}
