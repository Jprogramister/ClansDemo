package org.example.versionablecache;

import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class VersionableCacheImpl<KEY, VALUE extends Versionable> implements VersionableCache<KEY, VALUE> {
    private final Map<KEY, VALUE> cache = new ConcurrentHashMap<>();
    private final Function<KEY, Optional<VALUE>> dateSourceProvider;

    @Override
    @Nullable
    public VALUE get(Object key) {
        final var cachedRecord = cache.get(key);
        if (nonNull(cachedRecord)) {
            return cachedRecord;
        } else {
            return dateSourceProvider.apply((KEY) key)
                    .orElse(null);
        }
    }

    @Override
    public VALUE put(KEY key, VALUE value) throws OptimisticLockException {
        if (!compute(key, value.getVersion(), (k, saved) -> value)) {
            throw new OptimisticLockException("Optimistic lock exception");
        }
        return value;
    }

    @Override
    @Nullable
    public VALUE compute(KEY key, BiFunction<KEY, VALUE, VALUE> action) {
        if (!cache.containsKey(key)) {
            return dateSourceProvider.apply(key)
                    .map(item -> cache.computeIfPresent(key, action))
                    .orElse(null);
        }
        return cache.computeIfPresent(key, action);
    }

    @Override
    public Set<Map.Entry<KEY, VALUE>> entrySet() {
        return cache.entrySet();
    }

    boolean compute(KEY key, long expectedVersion, BiFunction<KEY, VALUE, VALUE> action) {
        var updated = new AtomicBoolean(false);
        cache.compute(key, (k, saved) -> {
            var modified = action.apply(k, saved);
            if (isNull(saved)) {
                updated.set(true);
                return modified;
            }
            if (saved.getVersion() == expectedVersion) {
                updated.set(true);
                return (VALUE) modified.incrementVersion();
            }
            return saved;
        });
        return updated.get();
    }
}
