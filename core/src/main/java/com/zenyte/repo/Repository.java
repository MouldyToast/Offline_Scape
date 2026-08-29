package com.zenyte.repo;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-02-19
 */
public interface Repository<T, ID> {

    T get(ID id);

    T update(T object);

    void create(T object);

    void delete(ID id);

}
