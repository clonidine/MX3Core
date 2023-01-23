package dev.migx3.core.api.services.impl;

import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.services.Service;

public final class UserService extends Service<User> {

    public UserService(Repository<User> repository) {
        super(repository);
    }
}