package org.jesse.game.content.boss.grotesqueguardians.plugins;

import org.jesse.game.GameConstants;
import org.jesse.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import org.jesse.game.content.slayer.Assignment;
import org.jesse.game.content.slayer.BossTask;
import org.jesse.game.content.slayer.RegularTask;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.var.VarCollection;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.plugins.dialogue.ItemChat;
import org.jesse.plugins.dialogue.PlainChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 21/07/2019 | 20:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class SlayerTowerRoofEntranceObject implements ObjectAction {

    private static final Logger log = LoggerFactory.getLogger(SlayerTowerRoofEntranceObject.class);

    private static final Item BRITTLE_KEY = new Item(21724);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!GameConstants.GROTESQUE_GUARDIANS) {
            player.sendMessage("The Grotesque Guardians have temporarily been disabled. Come back later.");
            return;
        }
        if (option.equals("Unlock")) {
            if (!player.getInventory().containsItem(BRITTLE_KEY)) {
                player.getDialogueManager().start(new PlainChat(player, "You need a brittle key to unlock the gateway."));
                return;
            }
            player.addAttribute("brittle-entrance_unlocked", 1);
            VarCollection.BRITTLE_ENTRANCE_UNLOCKED.updateSingle(player);
            player.getInventory().deleteItem(BRITTLE_KEY);
            player.getDialogueManager().start(new ItemChat(player, BRITTLE_KEY, "You use the key and it is absorbed by the gateway as<br><br> it activates..."));
        } else if (option.equals("Go-through")) {
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || (!assignment.getTask().equals(RegularTask.GARGOYLES) && !assignment.getTask().equals(BossTask.GROTESQUE_GUARDIANS))) {
                player.getDialogueManager().start(new PlainChat(player, "You need a Gargoyle slayer task or Grotesque Guardians boss task to enter the roof."));
                return;
            }
            player.getDialogueManager().start(new PlainChat(player, "You enter the passageway, and it takes you to the roof of the tower."));
            new FadeScreen(player, () -> {
                player.lock();
                try {
                    final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                    final GrotesqueGuardiansInstance instance = new GrotesqueGuardiansInstance(player, area);
                    instance.constructRegion();
                } catch (OutOfSpaceException e) {
                    log.error("", e);
                }
            }).fade(3);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31681 };
    }
}
