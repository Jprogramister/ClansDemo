package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.Clan;

@RequiredArgsConstructor
public class UserAddGoldServiceImpl implements UserAddGoldService {
    private final ClanService clanService;

    @Override
    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = clanService.get(clanId);
        clanService.updateGold(clan.getId(), gold);
    }
}