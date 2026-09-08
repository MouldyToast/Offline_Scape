package org.jesse.game.content.minigame.barrows;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.CollectionUtils;

import java.util.ArrayList;

/**
 * @author Kris | 30/11/2018 18:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsSarcophagus implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Search")) {
            final BarrowsWight wight = CollectionUtils.findMatching(BarrowsWight.values, npc -> npc.getSarcophagusId() == object.getId());
            if (wight != null && player.getBarrows().getCurrentWight() == null) {
                final Barrows barrows = player.getBarrows();
                if (wight.equals(barrows.getHiddenWight())) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            final ObjectArrayList<Dialogue.DialogueOption> options = new ObjectArrayList<DialogueOption>();
                            options.add(new DialogueOption("Yes, I'm fearless.", () -> {
                                final Barrows barrows = player.getBarrows();
                                player.setLocation(new Location(3551 + Utils.random(1), 9691, 0));
                                barrows.refreshDoors();
                            }));
                            options.add(new DialogueOption("No way, that looks scary!"));
                            options(options.size() == 3 ? "You've found two tunnels.<br>Would you like to enter " +
                                    "either?" : "You've found a tunnel.<br>Would you like to enter it?", options.toArray(new DialogueOption[0]));
                        }
                    });
                    return;
                }
                if (!barrows.getSlainWights().contains(wight)) {
                    barrows.sendWight(wight, player.getLocation(), "You dare disturb my rest!");
                }
            }
            player.sendMessage("You find nothing.");
        }
    }

    @Override
    public Object[] getObjects() {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        for (final BarrowsWight wight : BarrowsWight.values) {
            list.add(wight.getSarcophagusId());
        }
        return list.toArray();
    }
}
