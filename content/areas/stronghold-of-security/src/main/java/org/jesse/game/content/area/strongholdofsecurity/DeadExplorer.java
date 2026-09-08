package org.jesse.game.content.area.strongholdofsecurity;

import org.jesse.game.item.Item;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 4. sept 2018 : 21:35:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DeadExplorer implements ObjectAction {

    private static final Item NOTES = new Item(9004);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Search")) {
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    if (player.containsItem(NOTES)) {
                        player.sendMessage("You don't find anything.");
                        return;
                    }
                    player.getInventory().addItem(NOTES).onFailure(item -> World.spawnFloorItem(item, player));
                    plain("You rummage around in the dead explorer's bag.....");
                    plain("You find a book of hand written notes.");
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DEAD_EXPLORER };
    }
}
