package org.jesse.plugins.renewednpc;

import org.jesse.game.content.slayer.dialogue.SumonaAssignmentD;
import org.jesse.game.content.slayer.SlayerMaster;
import org.jesse.game.content.skills.slayer.dialogue.*;
import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.DistancedEntityStrategy;
import org.jesse.game.world.entity.player.Analytics;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 25/11/2018 16:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SlayerMasterNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Rewards", openRewards());
        bind("Talk-to", talkTo());
        bind("Assignment", assignment());
        bind("Trade", openShop());
    }

    private OptionHandler openShop() {
        return new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.openShop("Slayer Equipment");
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1),
                        () -> execute(player, npc), true));
            }
        };
    }

    private OptionHandler assignment() {
        return new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
                if (npc.getId() == SlayerMaster.SUMONA.getNpcId()) {
                    player.getDialogueManager().start(new SumonaAssignmentD(player, npc));
                } else if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {
                    player.getDialogueManager().start(new TuraelAssignmentD(player, npc));
                } else {
                    player.getDialogueManager().start(new SlayerMasterAssignmentD(player, npc));
                }
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1),
                        () -> execute(player, npc), true));
            }
        };
    }

    private OptionHandler talkTo() {
        return new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (TreasureTrail.talk(player, npc)) {
                    return;
                }
                Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
                if (npc.getId() == SlayerMaster.SUMONA.getNpcId()) {
                    player.getDialogueManager().start(new SumonaD(player, npc));
                } else if (npc.getId() == SlayerMaster.TURAEL.getNpcId()) {
                    player.getDialogueManager().start(new TuraelD(player, npc));
                } else {
                    player.getDialogueManager().start(new SlayerMasterD(player, npc));
                }
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1),
                        () -> execute(player, npc), true));
            }
        };
    }

    private OptionHandler openRewards() {
        return new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getSlayer().openInterface();
                Analytics.flagInteraction(player, Analytics.InteractionType.SLAYER_MASTER);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                player.setRouteEvent(new EntityEvent(player, new DistancedEntityStrategy(npc, 1),
                        () -> execute(player, npc), true));
            }
        };
    }

    @Override
    public int[] getNPCs() {
        return new int[]{
                NpcId.TURAEL,
                NpcId.MAZCHNA,
                NpcId.VANNAKA,
                NpcId.CHAELDAR,
                NpcId.DURADEL,
                NpcId.SUMONA,
                NpcId.NIEVE,
                NpcId.KONAR_QUO_MATEN
        };
    }
}
