package dev.migx3.core.api.repositories.impl;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Ban;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BanRepository implements Repository<Ban> {

    private final Datastore datastore;

    public BanRepository(Core plugin) {
        datastore = plugin.getDatastore();
    }

    @Override
    public void delete(Ban obj) {
        datastore.delete(obj);
    }

    @Override
    public void save(Ban obj) {
        datastore.save(obj);
    }

    @Override
    public List<Ban> findAll() {
        Query<Ban> banQuery = datastore.find(Ban.class);
        List<Ban> bans = new ArrayList<>();

        banQuery.stream().forEach(bans::add);

        return bans;
    }

    @Override
    public Ban findOne(String field, String value) {
        Query<Ban> banQuery = datastore.find(Ban.class).filter(Filters.gte(field, value));
        Optional<Ban> ban = Optional.ofNullable(banQuery.first());

        return ban.orElseThrow(() -> new ObjectNotFoundException("Ban not found"));
    }
}