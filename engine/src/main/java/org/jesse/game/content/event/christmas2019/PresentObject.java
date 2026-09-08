package org.jesse.game.content.event.christmas2019;

import org.jesse.game.GameInterface;
import org.jesse.game.content.advent.AdventCalendarManager;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Emote;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import java.util.Arrays;

/**
 * @author Kris | 22/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PresentObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getInventory().getFreeSlots() < 2) {
            player.sendMessage("You need some space in your inventory to open the present.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                final String impName = ChristmasUtils.getImpName(player);
                plain("To " + player.getName() + " and " + impName + ". Have a very merry Christmas. From your pal, Santa.").executeAction(() -> {
                    player.lock(3);
                    player.setAnimation(new Animation(827));
                    WorldTasksManager.schedule(() -> player.sendMessage("Inside the box you find a candy cane."));
                    WorldTasksManager.schedule(() -> {
                        player.getTemporaryAttributes().put("quest completed title", "You have completed A Christmas Warble.");
                        player.getTemporaryAttributes().put("quest completed item", new Item(ChristmasConstants.CANDY_CANE));
                        player.getTemporaryAttributes().put("quest completed rewards", Arrays.asList("Candy cane.", "Ghost costume.", "The Snowman Dance emote.", "The Freeze Emote", "The Dramatic Point emote."));
                        GameInterface.QUEST_COMPLETED.open(player);
                        player.getInventory().addOrDrop(new Item(ChristmasConstants.CANDY_CANE));
                        player.getTrackedHolidayItems().add(ChristmasConstants.CANDY_CANE);
                        player.getEmotesHandler().unlock(Emote.FREEZE);
                        player.getEmotesHandler().unlock(Emote.SNOWMAN_DANCE);
                        AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.EVENT_COMPLETE);
                        AdventCalendarManager.increaseChallengeProgress(player, 2022, 16, 1);
                    }, 1);
                });
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ChristmasConstants.PRESENT_OBJECT};
    }
}
