package com.zenyte.plugins.itemonnpc;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary; // Import for Kandarin diaries
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import mgi.utilities.StringFormatUtil;

/**
 * Adds a 50% cost discount if the player has completed the Kandarin Elite Diary.
 */
public class ZamorakianSpearOnOttoNPCAction implements ItemOnNPCAction {

    private static final Item ZAMORAKIAN_SPEAR = new Item(11824);
    private static final Item ZAMORAKIAN_HASTA = new Item(11889);
    private static final int NORMAL_COST = 300000;
    private static final double DISCOUNT_RATE = 0.5;

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        final boolean canWield = player.getSkills().getLevel(SkillConstants.ATTACK) >= 70;
        final boolean hasDiaryCompletion = player.getAchievementDiaries().isAllCompleted(KandarinDiary.ELITE);
        final int cost = hasDiaryCompletion ? (int) (NORMAL_COST * DISCOUNT_RATE) : NORMAL_COST;

        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Yes, I can convert a Zamorakian spear into a hasta.<br>The spirits require me to request " + StringFormatUtil.format(cost) + " coins from<br>you for this service.");
                if (hasDiaryCompletion) {
                    npc("Because you have completed the elite Kandarin diary,<br>you qualify for a 50% discount.");
                }
                if (!canWield) {
                    npc("<col=ff0000>~ WARNING ~</col><br><br>You do not have the requirements to wield a hasta!");
                }
                options("Do you wish to convert your spear?", "Yes", "No").onOptionOne(() -> {
                    if (player.getInventory().containsItem(new Item(995, cost))) {
                        setKey(5);
                        new FadeScreen(player, () -> {
                            player.getInventory().deleteItem(ZAMORAKIAN_SPEAR);
                            player.getInventory().deleteItem(new Item(995, cost));
                            player.getInventory().addItem(ZAMORAKIAN_HASTA);
                            WorldTasksManager.schedule(() -> player.getDialogueManager().start(new ItemChat(player, ZAMORAKIAN_HASTA, "Otto successfully transforms your Zamorakian spear into a hasta.")));
                        }).fade(2);
                    } else {
                        setKey(10);
                    }
                });
                plain(5, "Otto sets to work...", false);
                npc(10, "Sorry but you don\'t seem to have enough gold on you for this service.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ZAMORAKIAN_SPEAR.getId() };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 2914 };
    }
}