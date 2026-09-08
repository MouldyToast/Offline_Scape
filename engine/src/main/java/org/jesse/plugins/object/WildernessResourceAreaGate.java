package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.privilege.MemberRank;
import org.jesse.game.world.object.Door;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.area.wilderness.WildernessResourceArea;

/**
 * @author Kris | 24. juuni 2018 : 14:52:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("unused")
public final class WildernessResourceAreaGate implements ObjectAction {

    private static final DiaryReward[] DIARY_REWARDS = { DiaryReward.WILDERNESS_SWORD3, DiaryReward.MORYTANIA_LEGS2 };

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Open")) {
            if (!player.inArea("Wilderness Resource Area")) {
                if (DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD4, player) || player.getMemberRank().equalToOrGreaterThan(MemberRank.DIAMOND)) {
                    enter(player, object);
                } else {
                    player.getDialogueManager().start(new Dialogue(player) {

                        @Override
                        public void buildDialogue() {
                            int price = WildernessResourceArea.BASE_ENTRY_FEE;
                            for (int index = 0; index < DIARY_REWARDS.length; index++) {
                                final DiaryReward reward = DIARY_REWARDS[index];
                                if (DiaryUtil.eligibleFor(reward, player)) {
                                    price = (int) (price * (index == 0 ? 0.5 : 0.8));
                                    break;
                                }
                            }
                            final int cost = price;
                            options("Do you want to enter? It will cost you " + cost + " coins", "Yes", "No").onOptionOne(() -> {
                                if (player.getInventory().containsItem(995, cost)) {
                                    player.getInventory().deleteItem(new Item(995, cost));
                                    PlayerAttributesKt.setWildernessResourceAreaPaidFeeAmount(player, cost);
                                    enter(player, object);
                                } else {
                                    setKey(10);
                                }
                            });
                            item(10, new Item(995, 7500), "You don\'t have enough coins to pay Mandrith.");
                        }
                    });
                }
            } else {
                PlayerAttributesKt.setWildernessResourceAreaPaidFeeAmount(player, 0);
                enter(player, object);
            }
        } else if (option.equals("Peek")) {
            player.sendMessage("You peek through the gate...");
            WorldTasksManager.schedule(() -> {
                final int size = GlobalAreaManager.getArea(WildernessResourceArea.class).getPlayers().size();
                final String auxiliary = size == 1 ? "is" : "are";
                final String label = size == 1 ? "adventurer" : "adventurers";
                player.sendMessage("There " + auxiliary + " " + size + " " + label + " inside the resource area.");
            });
        }
    }

    private static final void enter(final Player player, final WorldObject object) {
        player.addWalkSteps(object.getX(), object.getY());
        player.lock(3);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            private WorldObject door;

            @Override
            public void run() {
                switch(ticks++) {
                    case 0:
                        door = Door.handleGraphicalDoor(object, null);
                        return;
                    case 1:
                        if (player.inArea("Wilderness Resource Area")) {
                            player.addWalkSteps(door.getX(), door.getY(), 1, false);
                        } else {
                            player.addWalkSteps(object.getX(), object.getY(), 1, false);
                        }
                        return;
                    case 3:
                        Door.handleGraphicalDoor(door, object);
                        stop();
                        return;
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.GATE_26760 };
    }
}
