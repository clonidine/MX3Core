package dev.migx3.core.api.services;

import dev.migx3.core.api.repositories.Repository;

import java.util.List;

public class Service<T> {

    private final Repository<T> repository;

    public Service(Repository<T> repository) {
        this.repository = repository;
    }

    public void delete(T obj) {
        repository.delete(obj);
    }

    public void save(T obj) {
        repository.save(obj);
    }

    public List<T> findAll() {
        return repository.findAll();
    }

    public T findOne(String field, String value) {
        return repository.findOne(field, value);
    }
}
