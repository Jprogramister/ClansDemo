package org.example.service;

import org.example.model.Clan;
import org.example.versionablecache.OptimisticLockException;

import java.util.Map;
import java.util.Set;

public interface ClanService {
    Clan get(long clanId);
    Set<Map.Entry<Long, Clan>> getAll();
    void save(Clan clan) throws OptimisticLockException;
    void updateGold(long clanId, int diff);
}