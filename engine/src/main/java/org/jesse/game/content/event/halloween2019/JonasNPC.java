package org.jesse.game.content.event.halloween2019;

import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.events.player.UncheckedEntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import mgi.custom.halloween.HalloweenNPC;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Kris | 03/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JonasNPC extends NPC {
    private static final Location tile = new Location(3121, 3343, 0);
    private static final Animation knockout = new Animation(395);
    private static final SoundEffect sound = new SoundEffect(913);

    public JonasNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    public JonasNPC() {
        super(HalloweenNPC.JONAS_SHORTSWORD.getRepackedNPC(), tile.transform(0, 0, 3), Direction.NORTH, 0);
    }

    private int delayUntilRetreat = Integer.MIN_VALUE;

    @Override
    public void processNPC() {
        if (delayUntilRetreat == Integer.MIN_VALUE) {
            return;
        }
        if (--delayUntilRetreat <= 0) {
            setLocation(tile.transform(0, 0, 3));
            delayUntilRetreat = Integer.MIN_VALUE;
        }
    }

    public void strike(@NotNull final Player victim) {
        setLocation(tile);
        setFaceLocation(victim.getLocation());
        delayUntilRetreat = 12;
        WorldTasksManager.schedule(() -> {
            victim.sendSound(sound);
            victim.getDialogueManager().finish();
            setAnimation(knockout);
            victim.performDefenceAnimation(this);
        });
        WorldTasksManager.schedule(() -> {
            victim.setAnimation(Player.DEATH_ANIMATION);
            WorldTasksManager.schedule(() -> {
                victim.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 174);
                victim.getPacketDispatcher().sendClientScript(948, 0, 255, 5767168, 0, 60);
            }, 2);
            WorldTasksManager.schedule(() -> {
                victim.setAnimation(Animation.STOP);
                victim.setLocation(new Location(1623, 4704, 0));
                victim.getPacketDispatcher().sendClientScript(948, 5767168, 0, 0, 255, 60);
                HalloweenUtils.setStage(victim, HalloweenUtils.SEARCHED_BLOODY_AXE);
                WorldTasksManager.schedule(() -> {
                    final Optional<NPC> shilop = World.findNPC(HalloweenNPC.SHILOP.getRepackedNPC(), victim.getLocation(), 15);
                    victim.getDialogueManager().start(new Dialogue(victim, HalloweenNPC.SHILOP.getRepackedNPC(), shilop.orElse(null)) {
                        @Override
                        public void buildDialogue() {
                            player("Gahh! My head...");
                            npc("Shh! He'll hear you.").executeAction(() -> {
                                if (npc != null) {
                                    player.faceEntity(npc);
                                }
                            });
                            player("Shilop?");
                            npc("You know me?").executeAction(() -> {
                                if (npc != null) {
                                    player.setRunSilent(5);
                                    player.setRouteEvent(new UncheckedEntityEvent(player, new EntityStrategy(npc), () -> continueConversation(player, npc), true));
                                    return;
                                }
                                continueConversation(player, null);
                            });
                        }
                    });
                });
                WorldTasksManager.schedule(() -> {
                    victim.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
                    victim.unlock();
                }, 1);
            }, 6);
        }, 3);
    }

    private static final void continueConversation(@NotNull final Player player, final NPC shilop) {
        player.getDialogueManager().finish();
        player.getDialogueManager().start(new Dialogue(player, HalloweenNPC.SHILOP.getRepackedNPC(), shilop) {
            @Override
            public void buildDialogue() {
                player("Your mother had me come looking for you. Where are we?");
                npc("I don't know! I'm scared.");
                player("Alright, well, I can't just stand here and do nothing. I have to find a way out of here.");
            }
        });
    }
}
