package com.zenyte;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.Definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;

import static mgi.types.Definitions.cacheLowPriorityDefinitions;
import static mgi.types.Definitions.highPriorityDefinitions;

public final class CacheManager {

    public static boolean DEVELOPMENT_MODE = false;
    public static final String DEFAULT_CACHE_PATH = "cache/data/cache/";

    private static int[] crc;
    private static Cache cache;

    public static Cache getCache() {
        return cache;
    }

    public static void setCache(Cache cache) {
        CacheManager.cache = cache;
    }

    public static Cache loadDetached() {
        return loadDetached(false);
    }

    public static Cache loadDetached(boolean readOnly) {
        return loadDetached(DEFAULT_CACHE_PATH, readOnly);
    }

    public static Cache loadDetached(String path) {
        return loadDetached(path, false);
    }

    public static Cache loadDetached(String path, boolean readOnly) {
        Cache cache = loadCacheFiles(path, readOnly);
        loadDefinitions(ForkJoinPool.commonPool(), false);
        return cache;
    }

    public static Cache loadCacheFiles(String path, boolean readOnly) {
        Cache cache = Cache.openCache(path, readOnly);
        return loadCache(cache);
    }

    public static Cache loadCache(Cache cache) {
        crc = cache.getCrcs();
        return CacheManager.cache = cache;
    }

    public static void loadAllDefinitions() {
        loadDefinitions(ForkJoinPool.commonPool(), true);
    }

    public static void loadDefinitions() {
        loadDefinitions(ForkJoinPool.commonPool(), false);
    }

    public static void loadDefinitions(ExecutorService pool) {
        loadDefinitions(pool, false);
    }

    public static void loadDefinitions(ExecutorService pool, boolean allDefs) {
        List<Callable<Void>> callables = new ObjectArrayList<>(
                highPriorityDefinitions.length + (allDefs ? cacheLowPriorityDefinitions.length : 0));
        for (final Class<?> clazz : highPriorityDefinitions) {
            callables.add(() -> {
                Definitions.load(clazz).run();
                return null;
            });
        }
        if(allDefs) {
            for (final Class<?> clazz : cacheLowPriorityDefinitions) {
                callables.add(() -> {
                    Definitions.load(clazz).run();
                    return null;
                });
            }
        }
        try {
            pool.invokeAll(callables);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCRC(int index) {
        return crc[index];
    }

    public static int[] getCrc() {
        return crc;
    }

    private CacheManager() {
        throw new UnsupportedOperationException();
    }

}
