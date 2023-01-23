package dev.migx3.core.api.services.impl;

import dev.migx3.core.api.domain.Ban;
import dev.migx3.core.api.repositories.Repository;
import dev.migx3.core.api.services.Service;

public final class BanService extends Service<Ban> {

    public BanService(Repository<Ban> repository) {
        super(repository);
    }
}
