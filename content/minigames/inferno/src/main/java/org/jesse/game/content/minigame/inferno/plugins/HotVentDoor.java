package org.jesse.game.content.minigame.inferno.plugins;

import org.jesse.game.content.MaxCape;
import org.jesse.game.content.area.tzhaar.TzHaar;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;

import java.util.List;

/**
 * @author Kris | 15. apr 2018 : 23:49.49
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class HotVentDoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean proven = player.getNumericAttribute("infernoVar").intValue() >= 1;
        if (!proven) {
            player.getDialogueManager().start(new Dialogue(player, TzHaar.TZHAAR_KET) {

                @Override
                public void buildDialogue() {
                    final List<NPC> npcs = CharacterLoop.find(player.getLocation(), 10, NPC.class, n -> n.getId() == TzHaar.TZHAAR_KET);
                    final NPC npc = npcs.get(0);
                    if (npc != null) {
                        npc.setFaceLocation(new Location(player.getLocation()));
                    }
                    npc("Oy! Get back from there, no JalYt allowed through.").executeAction(() -> {
                        if (player.getEquipment().getId(EquipmentSlot.CAPE) == TzHaar.FIRE_CAPE || player.getEquipment().getId(EquipmentSlot.CAPE) == MaxCape.FIRE.getCape()) {
                            player.sendMessage("Perhaps the guards would be impressed by your fire cape.");
                        }
                    });
                    if (player.getInventory().containsItem(TzHaar.FIRE_CAPE, 1) || player.getInventory().containsItem(MaxCape.FIRE.getCape(), 1)) {
                        final Item item = player.getInventory().containsItem(TzHaar.FIRE_CAPE_ITEM) ? TzHaar.FIRE_CAPE_ITEM : new Item(MaxCape.FIRE.getCape());
                        player("I managed to defeat TzTok-Jad and obtain this fire cape.");
                        item(item, "You hold out your fire cape and show it to TzHaar-Ket.");
                        npc("This is most impressive JalYt-Mej-" + player.getName() + ".");
                        player("Surely this proves I am capable? Can I pleeease come through now?");
                        npc("I suppose so. I\'ll grant you access to Mor Ul Rek. The guards will open the gates for you, you are the first JalYt to pass these gates!").executeAction(() -> {
                            player.addAttribute("infernoVar", 1);
                            pass(player, object);
                        });
                    }
                }
            });
        } else {
            pass(player, object);
        }
    }

    private static final void pass(final Player player, final WorldObject object) {
        player.lock(2);
        player.setRunSilent(2);
        if ((object.getRotation() & 1) == 0) {
            if (player.getY() > object.getY()) {
                player.addWalkSteps(player.getX(), player.getY() - 2, 2, false);
            } else {
                player.addWalkSteps(player.getX(), player.getY() + 2, 2, false);
            }
        } else {
            if (player.getX() > object.getX()) {
                player.addWalkSteps(player.getX() - 2, player.getY(), 2, false);
            } else {
                player.addWalkSteps(player.getX() + 2, player.getY(), 2, false);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOT_VENT_DOOR_30266 };
    }
}
