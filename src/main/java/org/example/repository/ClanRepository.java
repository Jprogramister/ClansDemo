package org.example.repository;

import org.example.model.Clan;

import java.util.Optional;

public interface ClanRepository {
    Optional<Clan> findById(long id);

    /**
     * Update already existing or created new record in DB.
     * @param clan object to save
     * @return saved entity
     */
    Clan put(Clan clan);
}
