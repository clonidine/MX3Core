package dev.migx3.core.api.repositories;

import java.util.List;

public interface Repository<T> {

    void delete(T obj);

    void save(T obj);

    List<T> findAll();

    T findOne(String field, String value);
}