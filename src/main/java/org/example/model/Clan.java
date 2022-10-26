package org.example.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.versionablecache.Versionable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Immutable object. Use {@link org.example.service.ClanService#save}
 * or {@link org.example.service.ClanService#updateGold} to update {@link Clan} instance.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString(exclude = "version")
public class Clan implements Versionable {
    public static final Long INITIAL_VERSION_VALUE = 1L;

    private final long id;     // id клана
    private final String name; // имя клана
    private final int gold;    // текущее количество золота в казне клана
    private final Long version;

    public static Clan createNew(Clan clan) {
        return createNew(clan.getName(), clan.getGold());
    }

    public static Clan createNew(String name, int gold) {
        return new Clan(ThreadLocalRandom.current().nextLong(), name, gold, INITIAL_VERSION_VALUE);
    }

    public Clan update(int gold) {
        return new Clan(id, name, gold, version);
    }

    public Clan update(int gold, String name) {
        return new Clan(id, name, gold, version);
    }

    public Clan add(int gold) {
        return new Clan(id, name, this.gold + gold, version);
    }

    @Override
    public Versionable incrementVersion() {
        return new Clan(id, name, gold, version + 1);
    }
}