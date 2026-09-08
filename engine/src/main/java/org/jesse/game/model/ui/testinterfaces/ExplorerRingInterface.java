package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.achievementdiary.plugins.item.ExplorersRing;
import org.jesse.game.content.skills.magic.Magic;
import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.ItemSpell;
import org.jesse.game.content.skills.magic.spells.regular.HighLevelAlchemy;
import org.jesse.game.content.skills.magic.spells.regular.LowLevelAlchemy;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;

import static org.jesse.game.GameInterface.EXPLORER_RING_ALCH;

/**
 * @author Christopher
 * @since 1/25/2020
 */
public class ExplorerRingInterface extends Interface {
    public static final int CHARGES_VARBIT = 4554;
    private static final int HIGH_ALCHEMY_VARBIT = 5398;

    @Override
    protected void attach() {
        put(1, "Low alchemy");
        put(2, "High alchemy");
        put(4, "Close");
        put(7, "Inventory");
        put(8, "Charges");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Inventory"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2);
        player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
    }

    @Override
    protected void build() {
        bind("Close", player -> player.getInterfaceHandler().sendInterface(GameInterface.SPELLBOOK));
        bind("Low alchemy", player -> {
            player.getVarManager().sendBit(HIGH_ALCHEMY_VARBIT, false);
            player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
        });
        bind("High alchemy", ((player, slotId, itemId, option) -> {
            if (player.carryingItem(ItemId.EXPLORERS_RING_4)) {
                player.getVarManager().sendBit(HIGH_ALCHEMY_VARBIT, true);
                player.getVarManager().sendBit(CHARGES_VARBIT, player.getVariables().getFreeAlchemyCasts());
            } else {
                player.sendMessage("You must unlock the elite tier of rewards from the Lumbridge Diaries before you can do that.");
                player.sendSound(new SoundEffect(2277));
            }
        }));
        bind("Inventory", ((player, slotId, itemId, option) -> {
            if (ExplorersRing.rings.contains(itemId)) {
                player.sendMessage("You see no reason to cast on this item.");
                return;
            }
            final ItemSpell spell = player.getVarManager().getBitValue(HIGH_ALCHEMY_VARBIT) == 1 ? Magic.getSpell(Spellbook.NORMAL, "high level alchemy", HighLevelAlchemy.class) : Magic.getSpell(Spellbook.NORMAL, "low level alchemy", LowLevelAlchemy.class);
            if (spell == null) {
                return;
            }
            spell.execute(player, player.getInventory().getItem(slotId), slotId);
        }));
    }

    @Override
    public GameInterface getInterface() {
        return EXPLORER_RING_ALCH;
    }
}
