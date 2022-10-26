package org.example.versionablecache;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public interface VersionableCache<KEY, VALUE extends Versionable> {
    @Nullable
    VALUE get(Object key);

    /**
     * @throws RuntimeException if version of argument doesn't equal to current version in cache
     */
    VALUE put(KEY key, VALUE value) throws OptimisticLockException;

    @Nullable
    VALUE compute(KEY key, BiFunction<KEY, VALUE, VALUE> action);

    Set<Map.Entry<KEY, VALUE>> entrySet();
}
