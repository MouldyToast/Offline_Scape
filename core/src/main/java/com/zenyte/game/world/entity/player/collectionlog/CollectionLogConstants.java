package com.zenyte.game.world.entity.player.collectionlog;

/**
 * Shared collection log constants, kept in core so that classes here and the
 * collection log interface plugin (content module) can both reference them.
 */
public final class CollectionLogConstants {

    public static final String CATEGORY_ATTR_KEY = "COLLECTION_LOG_CATEGORY";
    public static final String SUB_CATEGORY_ATTR_KEY = "COLLECTION_LOG_SUB_CATEGORY";
    public static final int STRUCT_POINTER_ENUM_CAT = 683;
    public static final int STRUCT_POINTER_SUB_ENUM_CAT_NAME = 689;
    public static final int STRUCT_POINTER_SUB_ENUM_CAT = 690;

    private CollectionLogConstants() {
    }
}
