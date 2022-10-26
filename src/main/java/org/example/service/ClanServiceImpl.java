package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Clan;
import org.example.versionablecache.OptimisticLockException;
import org.example.versionablecache.VersionableCache;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ClanServiceImpl implements ClanService {
    private final AuditService auditService;
    private final VersionableCache<Long, Clan> cache;

    @Override
    @Nullable
    public Clan get(long clanId) {
       return cache.get(clanId);
    }

    @Override
    public Set<Map.Entry<Long, Clan>> getAll() {
        return cache.entrySet();
    }

    @Override
    public void save(Clan clan) throws OptimisticLockException {
        var updated = cache.put(clan.getId(), clan);
        auditService.accept(clan, updated);
    }

    @Override
    public void updateGold(long clanId, int diff) {
        cache.compute(clanId, (key, clan) -> {
            final var updated = clan.update(clan.getGold() + diff);
            auditService.accept(clan, updated);
            return updated;
        });
    }
}
