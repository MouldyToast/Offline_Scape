package com.zenyte.game.content.skills.slayer.dialogue;

import com.zenyte.game.content.skills.slayer.SlayerKeys;
import com.near_reality.game.content.slayer.Assignment;
import com.near_reality.game.content.slayer.BossTask;
import com.near_reality.game.content.slayer.RegularTask;
import com.near_reality.game.content.slayer.SlayerMaster;
import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 7. nov 2017 : 1:05.01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class TuraelAssignmentD extends Dialogue {
    public TuraelAssignmentD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        if (SlayerKeys.slayer(player).getAssignment() != null) {
            npc("You're still on an assignment. You need to finish that one first.");
            npc("You need to kill " + SlayerKeys.slayer(player).getAssignment().getAmount() + " " + SlayerKeys.slayer(player).getAssignment().getTask().toString() + ".");
            final RegularTask data = SlayerKeys.slayer(player).getAssignment().getTask() instanceof BossTask ? null : ((RegularTask) SlayerKeys.slayer(player).getAssignment().getTask());
            if (data == null || (SlayerKeys.slayer(player).getMaster() == SlayerMaster.KRYSTILIA || data.getCertainTaskSet(SlayerMaster.TURAEL) == null || data.getCertainTaskSet(SlayerMaster.TURAEL).getMaximumAmount() < SlayerKeys.slayer(player).getAssignment().getAmount())) {
                npc("You're still hunting " + SlayerKeys.slayer(player).getAssignment().getTask().toString() + ", you have " + SlayerKeys.slayer(player).getAssignment().getAmount() + " to go.");
                npc("Although it's not an assignment that I'd normally give... I guess I could  give you a new " +
                        "assignment, if you'd like.");
                npc("If you do get a new one, you will reset your standard task streak of " + SlayerKeys.slayer(player).getCurrentStreak() + ".");
                options(TITLE, "Yes please.", "No, thanks.").onOptionOne(() -> {
                    final Assignment task = SlayerKeys.slayer(player).generateTask(SlayerMaster.TURAEL);
                    SlayerKeys.slayer(player).setCurrentStreak(0);
                    SlayerKeys.slayer(player).setAssignment(task);
                    player.getDialogueManager().finish();
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
                        }
                    });
                    setKey(150);
                }).onOptionTwo(this::finish);
            }
            return;
        }
        final Slayer slayer = SlayerKeys.slayer(player);
        final Assignment task = slayer.generateTask(SlayerMaster.TURAEL);
        if (slayer.getMaster() != SlayerMaster.TURAEL) {
            slayer.setMaster(SlayerMaster.TURAEL);
        }
        SlayerKeys.slayer(player).setAssignment(task);
        npc("Your new task is to kill " + task.getAmount() + " " + task.getTask().toString() + ".");
        options(TITLE, "Got any tips for me?", "Okay, great!").onOptionOne(() -> setKey(100));
        npc(100, task.getTask().getTip());
    }
}
