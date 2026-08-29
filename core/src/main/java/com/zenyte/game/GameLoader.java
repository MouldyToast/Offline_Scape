package com.zenyte.game;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.cache.Cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Tommeh | 28 jul. 2018 | 13:03:30
 * @author Jire
 */
public final class GameLoader {

    public static Cache load() {
        return load(ForkJoinPool.commonPool());
    }

    public static Cache load(final ExecutorService pool) {
        final Cache cache = CacheManager.loadCacheFiles("./cache/data/cache/", true);
        CacheManager.loadDefinitions(pool, false);
        return cache;
    }

}
