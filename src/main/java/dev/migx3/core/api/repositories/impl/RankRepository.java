package dev.migx3.core.api.repositories.impl;

import dev.migx3.core.Core;
import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.repositories.exception.ObjectNotFoundException;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import dev.morphia.query.experimental.filters.Filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RankRepository implements Repository<Rank> {

    private final Datastore datastore;

    public RankRepository(Core plugin) {
        datastore = plugin.getDatastore();
    }

    @Override
    public void delete(Rank obj) {
        datastore.delete(obj);
    }

    @Override
    public void save(Rank obj) {
        datastore.save(obj);
    }

    @Override
    public List<Rank> findAll() {
        Query<Rank> rankQuery = datastore.find(Rank.class);
        List<Rank> ranks = new ArrayList<>();

        rankQuery.stream().forEach(ranks::add);

        return ranks;
    }

    @Override
    public Rank findOne(String field, String value) {
        Query<Rank> rankQuery = datastore.find(Rank.class).filter(Filters.gte(field, value));
        Optional<Rank> rank = Optional.ofNullable(rankQuery.first());

        return rank.orElseThrow(() -> new ObjectNotFoundException("Rank not found"));
    }
}
