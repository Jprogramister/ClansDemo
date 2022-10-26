package org.example.repository;

import lombok.SneakyThrows;
import org.example.model.Clan;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Emulates a database behaviour.
 * It is not thread safety class.
 */
public class ClanRepositoryImpl implements ClanRepository {
    private final Map<Long, Clan> data = new HashMap<>();

    @SneakyThrows
    @Override
    public Optional<Clan> findById(long id) {
        Thread.sleep(1_000);
        return Optional.ofNullable(data.get(id));
    }

    @SneakyThrows
    @Override
    public Clan put(Clan clan) {
        Thread.sleep(1_000);
        var res = findById(clan.getId())
                .map(existing -> existing.update(clan.getGold(), clan.getName()))
                .orElseGet(() -> Clan.createNew(clan.getName(), clan.getGold()));
        data.put(res.getId(), res);
        return res;
    }
}
