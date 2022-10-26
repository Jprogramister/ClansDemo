package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.versionablecache.OptimisticLockException;

@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final ClanService clanService;

    @Override
    public void completeTask(long clanId, long taskId) {
        final var gold = complete(clanId, taskId);
        var clan = clanService.get(clanId);
        var updated = clan.update(clan.getGold() + gold);
        try {
            clanService.save(updated);
        } catch (OptimisticLockException e) {
            System.out.printf("Cannot update clan %s. Task %d has been executing too long%n", clan.getId(), taskId);
        }
    }

    private int complete(long clanId, long taskId) {
        return 10;
    }
}