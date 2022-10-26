package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.ClanRepository;

import java.util.Timer;
import java.util.TimerTask;

@RequiredArgsConstructor
public class ScheduledCacheSynchronizer {
    private static final String TIMER_NAME = "CACHE_SYNC_TIMER";
    private static final long DELAY = 1_000L;
    private final long period;

    private final ClanService service;
    private final ClanRepository clanRepository;

    public void start() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                service.getAll().forEach(entry -> clanRepository.put(entry.getValue()));
            }
        };
        Timer timer = new Timer(TIMER_NAME);
        timer.scheduleAtFixedRate(repeatedTask, DELAY, period);
    }
}
