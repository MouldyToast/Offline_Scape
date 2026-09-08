package org.jesse.game.content.colosseum;

import org.jesse.game.GameInterface;
import org.jesse.game.model.MinimapState;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.cutscene.Cutscene;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.cutscene.actions.CameraLookAction;
import org.jesse.game.world.entity.player.cutscene.actions.CameraPositionAction;
import org.jesse.game.world.entity.player.cutscene.actions.CameraResetAction;
import org.jesse.game.world.entity.player.cutscene.actions.DialogueAction;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import org.jesse.game.world.object.WorldObject;
import net.rsprot.protocol.game.outgoing.camera.util.CameraEaseFunction;

import java.util.Arrays;
import java.util.List;

import static org.jesse.game.GameInterface.*;
import static org.jesse.game.GameInterface.EMOTE_TAB;
import static org.jesse.game.GameInterface.EQUIPMENT_TAB;
import static org.jesse.game.GameInterface.GAME_NOTICEBOARD;
import static org.jesse.game.GameInterface.INVENTORY_TAB;
import static org.jesse.game.GameInterface.PRAYER_TAB_INTERFACE;
import static org.jesse.game.GameInterface.SETTINGS;
import static org.jesse.game.GameInterface.SPELLBOOK;

public class ColosseumCutscene extends Cutscene {

    private static final Location START_LOCATION = new Location(1823, 3111);
    private static final Location SOL_LOCATION = new Location(1823, 3123);
    private static final List<GameInterface> closedTabs = Arrays.asList(COMBAT_TAB, SKILLS_TAB, JOURNAL_HEADER_TAB, INVENTORY_TAB, EQUIPMENT_TAB, PRAYER_TAB_INTERFACE, SPELLBOOK, SETTINGS, EMOTE_TAB, GAME_NOTICEBOARD);

    private final ColosseumInstance instance;
    private NPC solNpc;

    public ColosseumCutscene(final ColosseumInstance instance) {
        this.instance = instance;
    }

    @Override
    public void build() {
        int ticks = 0;
        addActions(ticks, () -> closedTabs.forEach(tab -> player.getInterfaceHandler().closeInterface(tab)), () -> {
            Location spawnLocation = instance.getLocation(SOL_LOCATION);
            player.lock();
            player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
            player.getVarManager().sendBit(4606, 1);
            player.setLocation(instance.getLocation(START_LOCATION));
            WorldObject object = World.getObjectWithType(spawnLocation, 10, true);
            World.removeObject(object);
            solNpc = new NPC(NpcId.SOL_HEREDIT_12827, spawnLocation, Direction.SOUTH, 0, true).spawn();
        });
        ticks++;
        addActions(ticks, new CameraPositionAction(player, instance.getLocation(new Location(1825, 3113, 0)), 1200, 5, 10),
                new CameraLookAction(player, instance.getLocation(new Location(1825, 3121, 0)), 1200, 232, 100)
        );
        ticks += 5;

        int attempts = player.getNumericAttribute(ColosseumInstance.ATTEMPTS_ATTRIBUTE).intValue();
        if (attempts > 3) {
            addActions(ticks, new DialogueAction(new NPCChat(player, NpcId.SOL_HEREDIT, "You might be an incapable fighter, but at least you're persistent. Shall we?")));
        } else if (attempts == 3) {
            addActions(ticks, new DialogueAction(new Dialogue(player, NpcId.SOL_HEREDIT) {
                @Override
                public void buildDialogue() {
                    npc("What is it they say? Third time's the charm? Let's see if that's true.");
                    npc("To arms!");
                }
            }));
        } else if (attempts == 2) {
            addActions(ticks, new DialogueAction(new Dialogue(player, NpcId.SOL_HEREDIT) {
                @Override
                public void buildDialogue() {
                    npc("You're back so soon? A lesser combatant would've retired in shame after the show you put on last time...");
                    npc("But you're clearly different. Let's see what you have this time...");
                }
            }));
        } else {
            addActions(ticks, new DialogueAction(new Dialogue(player, NpcId.SOL_HEREDIT) {
                @Override
                public void buildDialogue() {
                    npc("By Ralos, we finally have a worthy challenger.	");
                    npc("You've torn through multiple waves of combatants and you're still standing. I'm almost impressed.");
                    npc("Now let's see how you handle a real foe...");
                }
            }));
        }

        ticks += 2;
        addActions(ticks, () -> {
            solNpc.setAnimation(new Animation(10876));
        });
        ticks++;
        final FadeScreen fs = new FadeScreen(player);
        addActions(ticks, () -> {
            player.sendMessage(Colour.RED.wrap("Sol Heredit jumps down from his seat..."));
        }, fs::fade);
        ticks += 3;
        addActions(ticks, () -> {
            player.unlock();
            player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
            player.getVarManager().sendBit(4606, 0);
            solNpc.finish();
            player.getPacketDispatcher().sendCamRotateTo(225, 0, 1, CameraEaseFunction.LINEAR);
            instance.beginSolCombat();
        }, new CameraResetAction(player), () -> closedTabs.forEach(tab -> tab.open(player)));
        ticks++;
        addActions(ticks, fs::unfade);
    }

}