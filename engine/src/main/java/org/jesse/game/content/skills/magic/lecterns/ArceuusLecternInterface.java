package org.jesse.game.content.skills.magic.lecterns;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static org.jesse.game.content.skills.magic.lecterns.RegularTablet.*;

/**
 * @author Kris | 03/09/2019 08:17
 * @author Glabay | 03/16/2025
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArceuusLecternInterface extends Interface {
    private static final int QUANTITY_VARP = 2224;
    private static final int SELECTED_TABLET_TYPE = 10600;

    @Override
    protected void attach() {
        put(4, "Make one");
        put(5, "Make five");
        put(6, "Make ten");
        put(7, "Make x");
        put(8, "Make all");

        put(14, "Create");
        put(15, "Draynor manor");
        put(16, "Battlefront");
        put(17, "Mind altar");
        put(18, "Salve graveyard");

        put(19, "Enchant Sapphire");
        put(20, "Bones to Bananas");
        put(21, "Varrock Teleport");
        put(22, "Enchant Emerald");
        put(23, "Lumbridge Teleport");
        put(24, "Falador Teleport");
        put(25, "Teleport to House");
        put(26, "Camelot Teleport");
        put(27, "Kourend Castle Teleport");
        put(28, "Enchant Ruby");
        put(29, "Ardounge Teleport");
        put(30, "Civitas illa Fortis Teleport");
        put(31, "Enchant Diamond");
        put(32, "Watchtower Teleport");
        put(33, "Bones to Peaches");
        put(34, "Enchant Dragonstone");
        put(35, "Enchant Onyx");

    }

    @Override
    public void open(final Player player) {
        player.getVarManager().sendBit(10599, 3);
        if (player.getVarManager().getValue(QUANTITY_VARP) < 1) {
            player.getVarManager().sendVar(QUANTITY_VARP, 1);
        }
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Make one",    player -> player.getVarManager().sendVar(QUANTITY_VARP, 1));
        bind("Make five",   player -> player.getVarManager().sendVar(QUANTITY_VARP, 5));
        bind("Make ten",    player -> player.getVarManager().sendVar(QUANTITY_VARP, 10));
        bind("Make x",      player -> player.sendInputInt("How many would you like to make?", value -> player.getVarManager().sendVar(QUANTITY_VARP, Math.max(0, Math.min(28, value)))));
        bind("Make all",    player -> player.getVarManager().sendVar(QUANTITY_VARP, 28));

        bind("Enchant Sapphire",            player -> setType(player, SAPPHIRE_ENCHANTMENT));
        bind("Bones to Bananas",            player -> setType(player, BONES_TO_BANANAS));
        bind("Varrock Teleport",            player -> setType(player, VARROCK_TELEPORT));
        bind("Enchant Emerald",             player -> setType(player, EMERALD_ENCHANTMENT));
        bind("Lumbridge Teleport",          player -> setType(player, LUMBRIDGE_TELEPORT));
        bind("Falador Teleport",            player -> setType(player, FALADOR_TELEPORT));
        bind("Teleport to House",           player -> setType(player, TELEPORT_TO_HOUSE));
        bind("Camelot Teleport",            player -> setType(player, CAMELOT_TELEPORT));
        bind("Kourend Castle Teleport",     player -> setType(player, KOUREND_CASTLE_TELEPORT));
        bind("Enchant Ruby",                player -> setType(player, RUBY_ENCHANTMENT));
        bind("Ardounge Teleport",           player -> setType(player, ARDOUGNE_TELEPORT));
        bind("Civitas illa Fortis Teleport",player -> setType(player, CIVITAS_ILLA_FORTIS_TELEPORT));
        bind("Enchant Diamond",             player -> setType(player, DIAMOND_ENCHANTMENT));
        bind("Watchtower Teleport",         player -> setType(player, WATCHTOWER_TELEPORT));
        bind("Bones to Peaches",            player -> setType(player, BONES_TO_PEACHES));
        bind("Enchant Dragonstone",         player -> setType(player, DRAGONSTONE_ENCHANTMENT));
        bind("Enchant Onyx",                player -> setType(player, ONYX_ENCHANTMENT));

        bind("Create", this::createSelectedTablet);
    }

    private void createSelectedTablet(Player player) {
        var selectedIndex = player.getVarManager().getValue(SELECTED_TABLET_TYPE);
        var type = RegularTablet.get(selectedIndex);
        if (type == null)
            return;
        player.getInterfaceHandler().closeInterface(getInterface());
        player.getActionManager().setAction(new TabletCreation(type, player.getVarManager().getValue(QUANTITY_VARP)));
    }

    private void setType(@NotNull final Player player, @NotNull final LecternTablet tablet) {
        player.getVarManager().sendVar(SELECTED_TABLET_TYPE, tablet.type());
        logger.debug("Selecting tablet type {} cached type {}", tablet.type(), player.getVarManager().getValue(SELECTED_TABLET_TYPE));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.ARCEUUS_LECTERN;
    }
}
