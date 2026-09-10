package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.content.bountyhunter.WildyExtKt;
import org.jesse.game.content.bountyhunter.teleport.TeleportToTarget;
import org.jesse.game.GameInterface;
import org.jesse.game.content.skills.magic.Magic;
import org.jesse.game.content.skills.magic.SpellDefinitions;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.content.skills.magic.spells.arceuus.ThrallSpell;
import org.jesse.game.content.skills.magic.spells.lunar.Vengeance;
import org.jesse.game.content.skills.magic.spells.regular.EnchantCrossbowBolt;
import org.jesse.game.content.skills.magic.spells.regular.Lvl1Enchant;
import org.jesse.game.content.skills.magic.spells.teleports.SpellbookTeleport;
import org.jesse.game.content.skills.magic.spells.teleports.structures.HomeStructure;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.logger.NearRealityLogger;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;

import java.util.Objects;

import static org.jesse.game.world.entity.player.container.RequestResult.SUCCESS;

/**
 * @author Kris | 07/01/2019 15:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpellbookInterface extends Interface {
    private static final Logger log = NearRealityLogger.getLogger(SpellbookInterface.class);

    @Override
    protected void attach() {
        put(10, "Crossbow Bolt Enchantments");
        put(12, "Jewellery Enchantments");
        put(13, "Lvl-1 Enchant");
        put(24, "Lvl-2 Enchant");
        put(37, "Lvl-3 Enchant");
        put(46, "Lvl-4 Enchant");
        put(61, "Lvl-5 Enchant");
        put(74, "Lvl-6 Enchant");
        put(77, "Lvl-7 Enchant");
        put(73, "Teleport to Target");
        put(198, "Spell filters");
        put(198, 0, "Show combat spells");
        put(198, 1, "Show teleport spells");
        put(198, 2, "Show utility spells");
        put(198, 3, "Show spells you lack the magic level to cast");
        put(198, 4, "Show spells you lack the runes to cast");
        put(198, 5, "Show spells you lack the requirements to cast");
        put(198, 6, "Enable icon resizing");
    }

    private void openJewelleryEnchantments(Player player) {
        player.getPacketDispatcher().sendClientScript(4517, 1, 100, 75);
        player.getPacketDispatcher().sendClientScript(4518, 100, 100);
        player.getPacketDispatcher().sendClientScript(4721, 50, 192466);
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Spell filters"), 0, 6, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        bind("Crossbow Bolt Enchantments", player -> new EnchantCrossbowBolt().spellEffect(player, 0, null));

        bind("Jewellery Enchantments", this::openJewelleryEnchantments);
        bind("Lvl-1 Enchant", (player, slotId, itemId, option) ->
            new Lvl1Enchant().spellEffect(player, player.getInventory().getItemById(itemId), slotId));

        bind("Show combat spells", player -> player.getSettings().toggleSetting(Setting.SHOW_COMBAT_SPELLS));
        bind("Show teleport spells", player -> player.getSettings().toggleSetting(Setting.SHOW_TELEPORT_SPELLS));
        bind("Show utility spells", player -> player.getSettings().toggleSetting(Setting.SHOW_UTILITY_SPELLS));
        bind("Show spells you lack the magic level to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_MAGIC_LEVEL_TO_CAST));
        bind("Show spells you lack the runes to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_RUNES_TO_CAST));
        bind("Show spells you lack the requirements to cast", player -> player.getSettings().toggleSetting(Setting.SHOW_SPELLS_YOU_LACK_THE_REQUIREMENTS_TO_CAST));
        bind("Enable icon resizing", player -> player.getSettings().toggleSetting(Setting.ENABLE_ICON_RESIZING));
        bind("Teleport to Target", player -> {
            var reqRunes = new Item[] {
                new Item(Magic.LAW_RUNE, 1), new Item(Magic.CHAOS_RUNE, 1), new Item(Magic.DEATH_RUNE, 1)
            };
            if (player.getInventory().containsItems(reqRunes)) {
                if (player.getInventory().deleteItems(reqRunes).getResult() == SUCCESS) {
                    if (WildyExtKt.isBountyPaired(player)) {
                        if (TeleportToTarget.Companion.canTeleportToTarget(player))
                            TeleportToTarget.Companion.teleportToTarget(player);
                        else
                            player.sendMessage("You cannot teleport to your target right now.");
                    }
                    else
                        player.sendMessage("You do not have a target to teleport right now.");
                }
            }
            else
                player.sendMessage("You do not have the required runes to cast this spell.");

        });
    }

    @Override
    public DefaultClickHandler getDefaultHandler() {
        return ((player, componentId, slotId, itemId, optionId) -> {
            if (player.isLocked()) {
                return;
            }
            final String option = getOptionString(componentId, optionId);

            final String spellName = SpellDefinitions.getSpellName(componentId);
            final DefaultSpell spell = Magic.getSpell(player.getCombatDefinitions().getSpellbook(), spellName, DefaultSpell.class);
            //The below block prevents flapping on the home teleport spell when multi-clicking it.
            if (spell != null
                    && spell.getSpellName().toLowerCase().endsWith("home teleport")
                    && player.getActionManager().getAction() instanceof HomeStructure.HomeTeleportAction
                    && player.getTemporaryAttributes().get("Current teleport spell") == ((SpellbookTeleport) spell).destination()
                    && Objects.equals(player.getTemporaryAttributes().get("Last Spellbook Click Option"), option)) {
                return;
            }
            player.getTemporaryAttributes().put("Last Spellbook Click Option", option);
            if (spell instanceof SpellbookTeleport) {
                player.getTemporaryAttributes().put("Current teleport spell", ((SpellbookTeleport) spell).destination());
            }
            if (spell instanceof Vengeance || spell instanceof ThrallSpell) {
                player.stop(Player.StopType.INTERFACES);
            } else {
                player.stop(Player.StopType.INTERFACES, Player.StopType.WALK, Player.StopType.ROUTE_EVENT);
            }
            //Exception for vengeance spell as it is a direct combat spell and shouldn't interrupt combat. And thralls.
            if (!(spell instanceof Vengeance) && !(spell instanceof ThrallSpell)) {
                player.stop(Player.StopType.ACTIONS);
            }
            if (option.equals("Warnings")) {
                final String name = SpellDefinitions.getSpellName(componentId);
                if ("low level alchemy".equals(name) || "high level alchemy".equals(name)) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            final int value = player.getVarManager().getBitValue(SettingsInterface.MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID);
                            options("Warning trigger: " + StringFormatUtil.format(value), new DialogueOption("Change warning value", () -> {
                                finish();
                                player.sendInputInt("Set new warning value: ", val -> {
                                    player.getVarManager().sendBit(SettingsInterface.MINIMUM_ALCH_TRIGGER_VALUE_VARBIT_ID, val);
                                    player.sendMessage("Alchemy warning set to: " + val);
                                });
                            }), new DialogueOption("Cancel"));
                        }
                    });
                }
                return;
            }
            if (spell == null) {
                return;
            }
            try {
                spell.execute(player, optionId, option);
            } catch (Exception e) {
                log.error("", e);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SPELLBOOK;
    }
}
