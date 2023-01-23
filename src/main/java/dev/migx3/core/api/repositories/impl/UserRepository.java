package dev.migx3.core.api.repositories.impl;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.User;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements Repository<User> {

    private final Datastore datastore;

    public UserRepository(Core plugin) {
        datastore = plugin.getDatastore();
    }

    @Override
    public void delete(User obj) {
        datastore.delete(obj);
    }

    @Override
    public void save(User obj) {
        datastore.save(obj);
    }

    @Override
    public List<User> findAll() {
        Query<User> userQuery = datastore.find(User.class);
        List<User> users = new ArrayList<>();

        userQuery.stream().forEach(users::add);

        return users;
    }

    @Override
    public User findOne(String field, String value) {
        Query<User> userQuery = datastore.find(User.class).filter(Filters.gte(field, value));
        Optional<User> user = Optional.ofNullable(userQuery.first());

        return user.orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }
}