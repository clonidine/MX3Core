package dev.migx3.core.api.services.impl;

import dev.migx3.core.api.domain.Rank;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.services.Service;

public final class RankService extends Service<Rank> {

    public RankService(Repository<Rank> repository) {
        super(repository);
    }
}
