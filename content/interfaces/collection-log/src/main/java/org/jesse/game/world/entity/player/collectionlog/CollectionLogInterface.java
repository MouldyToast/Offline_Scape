package org.jesse.game.world.entity.player.collectionlog;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CABossType;
import org.jesse.game.world.entity.player.calog.CALogBossOverviewInterface;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.plugins.events.LoginEvent;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import mgi.types.config.StructDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.jesse.game.GameConstants.WORLD_PROFILE;
import static org.jesse.game.world.entity.player.collectionlog.CollectionLogConstants.*;
import static org.jesse.game.world.entity.player.collectionlog.CollectionLogRewardHandler.getCollectionLogItems;

/**
 * @author Kris | 12/03/2019 23:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CollectionLogInterface extends Interface {

    private static final int CATEGORY_SCRIPT = 2388;
    private static final int BUILD_INTERFACE_SCRIPT = 7797;

    private static final int SCROLL_LAYER_COMPONENT_PARAM = 685;
    private static final int ELEMENTS_LAYER_COMPONENT_PARAM = 686;
    private static final int ELEMENT_NAME_COMPONENT_PARAM = 687;
    private static final int ELEMENTS_SCROLLBAR_COMPONENT = 688;
    private static final int COLLECTION_LOG_TOTAL_UNLOCKED_VARP = 2943;
    private static final int COLLECTION_LOG_TOTAL_UNLOCKABLE_VARP = 2944;
    private static final String SEARCH_LETTERS = "abcdefghijklmnopqrstuvwxyz \t";

    @Override
    protected void attach() {
        put(4, CLCategoryType.BOSS.category());
        put(5, CLCategoryType.RAIDS.category());
        put(6, CLCategoryType.CLUES.category());
        put(7, CLCategoryType.MINIGAMES.category());
        put(8, CLCategoryType.OTHER.category());
        put(21, "Combat achievements");
        put(1, "Close");
        int id = 42;
        for (char c : SEARCH_LETTERS.toCharArray()) {
            put(id++, "Search letter " + c);
        }

        for (CLCategoryType type : CLCategoryType.values) {
            final StructDefinitions struct = Objects.requireNonNull(StructDefinitions.get(type.struct()));
            final int layerComponent = getStructParam(struct, ELEMENTS_LAYER_COMPONENT_PARAM);
            put(layerComponent, type + " layer");
        }
    }

    @Override
    public void open(Player player) {
        for (char c : SEARCH_LETTERS.toCharArray()) {
            player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("Search letter " + c), -1, -1, AccessMask.CLICK_OP1);
        }
        refreshTotalUnlocked(player);
        player.getPacketDispatcher().sendUpdateItemContainer(player.getCollectionLog().getContainer());
        player.getInterfaceHandler().sendInterface(this);

        final CLCategoryType category = (CLCategoryType) player.getTemporaryAttributes().getOrDefault(CATEGORY_ATTR_KEY, CLCategoryType.BOSS);
        final int subCategory = (int) player.getTemporaryAttributes().getOrDefault(SUB_CATEGORY_ATTR_KEY, 0);
        populate(player, category, subCategory);
        final Container container = player.getCollectionLog().getContainer();
        container.setFullUpdate(true);
        container.refresh(player);
    }

    @Override
    public void close(Player player, Optional<GameInterface> replacement) {
        super.close(player, replacement);
        player.getPacketDispatcher().sendClientScript(2158);
    }

    private static void refreshTotalUnlocked(Player player) {
        player.getVarManager().sendVarInstant(COLLECTION_LOG_TOTAL_UNLOCKABLE_VARP, CollectionLog.COLLECTION_LOG_ITEMS.size());
        player.getVarManager().sendVarInstant(COLLECTION_LOG_TOTAL_UNLOCKED_VARP, player.getCollectionLog().getContainer().getSize());
    }

    @Subscribe
    public static void onLogin(@NotNull final LoginEvent event) {
        refreshTotalUnlocked(event.getPlayer());
    }

    private void populate(@NotNull final Player player, @NotNull final CLCategoryType type, final int subCategory) {
        final var categoryEnum = getEnum(type);
        final var length = categoryEnum.getSize();
        Preconditions.checkArgument(subCategory >= 0 && subCategory < length);
        final int subStructId = categoryEnum.getValue(subCategory).orElseThrow(RuntimeException::new);
        final var functions = CollectionLogCategories.getFunctions(getCategoryName(subStructId));
        final var varIds = new int[] { 2048, 2941, 2942 };
        if (functions != null)
            applyFunctionsToPlayerVars(player, functions, varIds);
        sendInterfaceData(player, type, subCategory, length);
    }

    private String getCategoryName(int structId) {
        final var struct = Objects.requireNonNull(StructDefinitions.get(structId));
        return struct.getValue(STRUCT_POINTER_SUB_ENUM_CAT_NAME).orElseThrow(RuntimeException::new).toString();
    }

    private IntArrayList getOptions(StructDefinitions struct, CLCategoryType type, int subCategory) {
        var options = new IntArrayList(5);
        options.add(getStructParam(struct, SCROLL_LAYER_COMPONENT_PARAM));
        options.add(getStructParam(struct, ELEMENTS_LAYER_COMPONENT_PARAM));
        options.add(getStructParam(struct, ELEMENT_NAME_COMPONENT_PARAM));
        options.add(getStructParam(struct, ELEMENTS_SCROLLBAR_COMPONENT));
        options.add(type.struct());
        options.add(subCategory);
        return options;
    }

    private void sendInterfaceData(Player player, CLCategoryType type, int subCategory, int length) {
        final var struct = Objects.requireNonNull(StructDefinitions.get(type.struct()));
        final var options = getOptions(struct, type, subCategory);
        var dispatcher = player.getPacketDispatcher();
        dispatcher.sendComponentSettings(getInterface(), options.getInt(1), 0, length, AccessMask.CLICK_OP1);
//        dispatcher.sendComponentSettings(getInterface(), getComponent("Claim Reward"), 0, 9, AccessMask.CLICK_OP1);
        // dispatcher.sendClientScript(CATEGORY_SCRIPT, type.ordinal());

        // old way
//        dispatcher.sendClientScript(CATEGORY_SCRIPT, type.ordinal());
//        dispatcher.sendClientScript(BUILD_INTERFACE_SCRIPT, options.toArray());

        // new way
        options.add(0, type.ordinal());
        dispatcher.sendClientScript(BUILD_INTERFACE_SCRIPT, options.toArray());

        player.getTemporaryAttributes().put(CATEGORY_ATTR_KEY, type);
        player.getTemporaryAttributes().put(SUB_CATEGORY_ATTR_KEY, subCategory);
    }

    private void applyFunctionsToPlayerVars(Player player, Function<Player, Integer>[] functions, int[] varIds) {
        IntStream.iterate(0, i -> i < functions.length && i < varIds.length, i -> i + 1)
                .forEach(i -> player.getVarManager().sendVarInstant(varIds[i], functions[i].apply(player)));
    }

    private static int getStructParam(StructDefinitions struct, int param) {
        final Optional<?> optional = struct.getValue(param);
        final Object enumId = optional.orElseThrow(RuntimeException::new);
        assert enumId instanceof Integer;
        return (int) enumId;
    }

    static IntEnum getEnum(CLCategoryType type) {
        final var struct = Objects.requireNonNull(StructDefinitions.get(type.struct()));
        final var optional = struct.getValue(STRUCT_POINTER_ENUM_CAT);
        final var enumId = optional.orElseThrow(RuntimeException::new);
        assert enumId instanceof Integer;
        return EnumDefinitions.getIntEnum((Integer) enumId);
    }

    private static void updateSearchResults(Player player) {
        int index = 0;
        for (final CLCategoryType type : CLCategoryType.values) {
            final IntEnum categoryEnum = getEnum(type);
            final ObjectSet<Int2IntMap.Entry> entrySet = categoryEnum.getValues().int2IntEntrySet();
            for (final Int2IntMap.Entry entry : entrySet) {
                final var subCategoryStructId = entry.getIntValue();
                final var logItems = getCollectionLogItems(subCategoryStructId);
                for (Int2IntMap.Entry logSlot : logItems) {
                    final int item = logSlot.getIntValue();
                    final int amount = player.getCollectionLog().getContainer().getAmountOf(item);
                    player.getPacketDispatcher().sendClientScript(4100, item, amount, index, subCategoryStructId);
                }
                index++;
            }
        }
    }

    @Override
    protected void build() {
        bind(CLCategoryType.BOSS.category(), (player, slotId, itemId, option) -> populate(player, CLCategoryType.BOSS, 0));
        bind(CLCategoryType.RAIDS.category(), (player, slotId, itemId, option) -> populate(player, CLCategoryType.RAIDS, 0));
        bind(CLCategoryType.CLUES.category(), (player, slotId, itemId, option) -> populate(player, CLCategoryType.CLUES, 0));
        bind(CLCategoryType.MINIGAMES.category(), (player, slotId, itemId, option) -> populate(player, CLCategoryType.MINIGAMES, 0));
        bind(CLCategoryType.OTHER.category(), (player, slotId, itemId, option) -> populate(player, CLCategoryType.OTHER, 0));
        bind("Combat achievements", (player, slotId, itemId, option) -> {
            final CLCategoryType currentCategory = (CLCategoryType) player.getTemporaryAttributes().getOrDefault(CATEGORY_ATTR_KEY, CLCategoryType.BOSS);
            final int currentSubCategory = (int) player.getTemporaryAttributes().getOrDefault(SUB_CATEGORY_ATTR_KEY, 0);
            final Optional<CABossType> optionalType = Arrays.stream(CABossType.values).
                    filter(b -> b.getCollLogVal() == currentSubCategory && currentCategory.equals(b.isRaid() ? CLCategoryType.RAIDS : CLCategoryType.BOSS)).findFirst();
            if (optionalType.isPresent()) {
                player.getVarManager().sendBitInstant(CALogBossOverviewInterface.BOSS_SELECT_VARBIT, optionalType.get().ordinal() + 1);
                GameInterface.CA_BOSS.open(player);
            }
        });
        bind("Close", (player) -> player.getInterfaceHandler().closeInterfaces());
        for (CLCategoryType type : CLCategoryType.values) {
            bind(type + " layer", (player, slotId, itemId, option) -> populate(player, getCurrentCategory(player), slotId));
        }

        for (char c : SEARCH_LETTERS.toCharArray()) {
            bind("Search letter " + c, (player) -> {
                /* Avoid updating the results more than once a tick - it's very expensive. */
                long lastTick = player.getNumericTemporaryAttribute("collection log last search tick").longValue();
                if (lastTick < WorldThread.getCurrentCycle()) {
                    player.addTemporaryAttribute("collection log last search tick", WorldThread.getCurrentCycle());
                    updateSearchResults(player);
                }
            });
        }
    }

    @NotNull
    private CLCategoryType getCurrentCategory(@NotNull final Player player) {
        final Object categoryAttr = player.getTemporaryAttributes().get(CATEGORY_ATTR_KEY);
        Preconditions.checkArgument(categoryAttr instanceof CLCategoryType);
        return (CLCategoryType) categoryAttr;
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.COLLECTION_LOG;
    }
}
