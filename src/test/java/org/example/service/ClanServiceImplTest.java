package org.example.service;

import org.example.model.Clan;
import org.example.repository.AuditRepositoryImpl;
import org.example.repository.ClanRepositoryImpl;
import org.example.versionablecache.OptimisticLockException;
import org.example.versionablecache.VersionableCacheImpl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClanServiceImplTest {
    private static ClanService clanService;

    @BeforeClass
    public static void setUp() {
        var clanRepository = new ClanRepositoryImpl();
        var cache = new VersionableCacheImpl<>(clanRepository::findById);
        clanService = new ClanServiceImpl(new AuditService(new AuditRepositoryImpl()), cache);
        ScheduledCacheSynchronizer cacheSynchronizer = new ScheduledCacheSynchronizer(10_000, clanService, clanRepository);
        cacheSynchronizer.start();
    }

    @Test
    public void testSaveClan() throws OptimisticLockException {
        var clan = Clan.createNew("Test", 10);
        clanService.save(clan);
        var saved = clanService.get(clan.getId());
        Assert.assertEquals(clan.getId(), saved.getId());
    }

    @Test
    public void testUpdateClan() throws OptimisticLockException {
        var clan = Clan.createNew("Test", 10);
        clanService.save(clan);
        var saved = clanService.get(clan.getId());
        var updated = saved.add(10);
        clanService.save(updated);
        Assert.assertEquals(1,clanService.get(updated.getId()).getVersion() - updated.getVersion());
        Assert.assertEquals(updated.getGold(), clanService.get(updated.getId()).getGold());
    }

    @Test
    public void testUpdateWithLockException() throws OptimisticLockException {
        var clan = Clan.createNew("Test", 10);
        clanService.save(clan);
        var saved = clanService.get(clan.getId());
        clanService.save(saved);
        Assert.assertThrows(OptimisticLockException.class, () -> clanService.save(clan));
    }
}