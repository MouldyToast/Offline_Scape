package com.zenyte.game.content.area.prifddinas.zalcano.plugins;

import com.near_reality.game.content.commands.DeveloperCommands;
import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoDialogues;
import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.content.area.prifddinas.zalcano.actions.ImbueRefinedTephraAction;
import com.zenyte.game.content.area.prifddinas.zalcano.actions.TephraSmeltAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.PlainMessage;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import static com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants.*;

public class ZalcanoObjectActions implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {

        if (!DeveloperCommands.INSTANCE.getEnabledZalcano()){
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("Zalcano is currently disabled.");
                }
            });
            return;
        }

        int id = object.getId();
        if (id == ZALCANO_BARRIER) {
            boolean goingInsideLair = player.getPosition().getY() == 6063;
            boolean hasWarning = player.getBooleanAttribute(ZalcanoConstants.WARNING_NAME);
            if (goingInsideLair) {
                if (hasWarning) {
                    walkThroughBarrier(player);
                } else {
                    player.getDialogueManager().start(ZalcanoDialogues.barrier(player));
                }
            } else {
                walkThroughBarrier(player);
            }

            // TODO: the nice screen animation when entering layer
        } else if (id == ZALCANO_OUTSIDE_TELEPORT_PLATFORM_ID) {
            player.teleport(ZALCANO_LAYER_LOCATION);
        } else if (id == ZALCANO_TELEPORT_PLATFORM_ID) {
            player.teleport(ZALCANO_OUTSIDE_LOCATION);
        } else if (id == ZALCANO_FURNACE) {
            player.getActionManager().setAction(new TephraSmeltAction());
        } else if (id == ZALCANO_IMBUE_ALTAR) {
            player.getActionManager().setAction(new ImbueRefinedTephraAction());
        } else {
            throw new IllegalStateException("Unexpected value: " + object.getId());
        }

    }

    public static void walkThroughBarrier(Player player) {
        var boss = ZalcanoInstance.INSTANCE.getZalcano();
        if (boss != null && boss.getHitpoints() < boss.getMaxHitpoints()) {
            player.sendMessage("There is currently an active fight, please wait until it's over.");
            return;
        }
        boolean goingInsideLair = player.getPosition().getY() == 6063;
        player.lock(3);
        player.getTemporaryAttributes().put("zalcano_smelting_tephra", Boolean.FALSE);
        player.addWalkSteps(goingInsideLair ? Direction.SOUTH : Direction.NORTH, 2, -1, false);
    }


    @Override
    public Object[] getObjects() {
        return new Object[] {
                ZALCANO_OUTSIDE_TELEPORT_PLATFORM_ID,
                ZALCANO_TELEPORT_PLATFORM_ID ,// which one i
                ZALCANO_BARRIER,
                ZALCANO_FURNACE,
                ZALCANO_IMBUE_ALTAR
        };
    }
}
