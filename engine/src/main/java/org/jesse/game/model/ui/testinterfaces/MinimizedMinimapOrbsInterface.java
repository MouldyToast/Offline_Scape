package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.consumables.Consumable;
import org.jesse.game.content.consumables.drinks.BarbarianMix;
import org.jesse.game.content.consumables.drinks.Potion;
import org.jesse.game.content.consumables.edibles.Food;
import org.jesse.game.content.skills.magic.Magic;
import org.jesse.game.content.skills.magic.SpellState;
import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.lunar.CureMe;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.Toxins;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.area.wilderness.WildernessArea;
import org.jesse.logger.NearRealityLogger;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.Logger;

import static org.jesse.game.GameInterface.WORLD_MAP;
import static org.jesse.game.world.entity.Toxins.ToxinType.*;

/**
 * @author Kris | 19/07/2019 04:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MinimizedMinimapOrbsInterface extends Interface {
    private static final Logger log = NearRealityLogger.getLogger(MinimizedMinimapOrbsInterface.class);

    @Override
    protected void attach() {
        put(3, "Experience Tracker");
        put(6, "Cure Toxins");
        put(17, "Prayer");
        put(25, "Run");
        put(49, "View World Map");
        put(33, "Toggle special");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Toggle special", (player, slotId, itemId, option) -> {
            //In case of spoofing.
            if (WildernessArea.isWithinWilderness(player)) {
                return;
            }
            if (player.isLocked()) {
                return;
            }
            player.getCombatDefinitions().setSpecial(!player.getCombatDefinitions().isUsingSpecial(), false);
        });
        bind("Experience Tracker", (player, slotId, itemId, option) -> {
            if (option == 1) {
                player.getSettings().toggleSetting(Setting.EXPERIENCE_TRACKER);
            } else if (option == 2) {
                if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                    player.sendMessage("You can't configure your XP drops at the moment.");
                    return;
                }
                GameInterface.EXPERIENCE_TRACKER.open(player);
            } else if (option == 3) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options(TITLE, new DialogueOption("Show x" + player.getSkillingXPRate() + " experience drops", () -> {
                            player.setXpDropsMultiplied(false);
                            player.setXPDropsWildyOnly(false);
                            player.getVarManager().sendVar(3504, 1);
                        }), new DialogueOption("Show x1 experience drops in Wilderness", () -> {
                            player.setXpDropsMultiplied(true);
                            player.setXPDropsWildyOnly(true);
                            player.getVarManager().sendVar(3504, WildernessArea.isWithinWilderness(player.getX(), player.getY()) ? player.getSkillingXPRate() : 1);
                        }), new DialogueOption("Show x1 experience drops everywhere", () -> {
                            player.setXpDropsMultiplied(true);
                            player.setXPDropsWildyOnly(false);
                            player.getVarManager().sendVar(3504, player.getSkillingXPRate());
                        }), new DialogueOption("Cancel"));
                    }
                });
            }
        });
        bind("Cure Toxins", (player, slotId, itemId, option) -> {
            if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                player.sendMessage("You can't do that right now.");
                return;
            }
            final Toxins toxins = player.getToxins();
            final Toxins.ToxinType type = toxins.isVenomed() ? VENOM : toxins.isPoisoned() ? POISON : toxins.isDiseased() ? DISEASE : null;
            if (type == null) {
                return;
            }
            final Inventory inventory = player.getInventory();
            for (int i = 0; i < 28; i++) {
                final Item item = inventory.getItem(i);
                if (item == null) {
                    continue;
                }
                final ItemDefinitions definitions = item.getDefinitions();
                if (definitions == null || definitions.isNoted()) {
                    continue;
                }
                final String name = item.getName().toLowerCase();
                switch (type) {
                case DISEASE: 
                    if (name.contains("relicym's")) {
                        final Consumable consumable = Consumable.consumables.get(item.getId());
                        if (consumable != null) {
                            if (consumable == Potion.RELICYMS_BALM || consumable == BarbarianMix.RELICYMS_MIX) {
                                consumable.consume(player, item, i);
                                return;
                            }
                        }
                    }
                    continue;
                case POISON: 
                    {
                        final Consumable consumable = Consumable.consumables.get(item.getId());
                        if (consumable == Potion.ANTIPOISON || consumable == Potion.SUPERANTIPOISON || consumable == Potion.ANTIDOTE_PLUS || consumable == Potion.ANTIDOTE_PLUS_PLUS || consumable == Food.STRANGE_FRUIT || consumable == Potion.GUTHIX_REST || consumable == Potion.SANFEW_SERUM || consumable == BarbarianMix.ANTIPOISON_MIX || consumable == BarbarianMix.ANTIPOISON_SUPERMIX || consumable == BarbarianMix.ANTIDOTE_PLUS_MIX) {
                            consumable.consume(player, item, i);
                            return;
                        }
                        continue;
                    }
                case VENOM: 
                    final Consumable consumable = Consumable.consumables.get(item.getId());
                    if (consumable == Potion.ANTIPOISON || consumable == Potion.SUPERANTIPOISON || consumable == Potion.ANTIDOTE_PLUS_PLUS || consumable == BarbarianMix.ANTIPOISON_MIX || consumable == BarbarianMix.ANTIPOISON_SUPERMIX || consumable == Potion.ANTI_VENOM || consumable == Potion.ANTI_VENOM_PLUS) {
                        consumable.consume(player, item, i);
                        return;
                    }
                }
            }
            if (type == POISON || type == VENOM) {
                final CureMe spell = Magic.getSpell(Spellbook.LUNAR, "cure me", CureMe.class);
                if (spell != null && spell.canCast(player)) {
                    if (!spell.canUse(player)) {
                        return;
                    }
                    final SpellState state = new SpellState(player, spell.getLevel(), spell.getRunes());
                    if (state.check(false)) {
                        try {
                            spell.execute(player, 1, "Cast");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                        return;
                    }
                }
            }
            player.sendMessage("You haven't got any potions to cure the " + type.toString().toLowerCase() + ".");
        });
        bind("Prayer", (player, slotId, itemId, option) -> {
            if (option == 1) {
                player.getPrayerManager().toggleQuickPrayers();
            } else if (option == 2) {
                if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                    player.sendMessage("You can't set up your prayers at the moment.");
                    return;
                }
                player.getPrayerManager().openQuickPrayers();
            }
        });
        bind("Run", player -> player.setRun(!player.isRun()));
        bind("View World Map", (player, slotId, itemId, option) -> {
            if (player.isLocked() || player.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL)) {
                player.sendMessage("You can't do that right now.");
                return;
            }
            if (option == 3) {
                if (player.isOnMobile()) {
                    player.getSettings().toggleSetting(Setting.MINIMIZE_MINIMAP);
                }
                return;
            }
            if (!player.getWorldMap().isVisible()) {
                player.getWorldMap().setFullScreen(option == 2);
                WORLD_MAP.open(player);
            } else {
                player.getWorldMap().close();
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.MINIMIZED_ORBS;
    }
}
