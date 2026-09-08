package org.jesse.game.content.event.christmas2019.cutscenes;

import com.google.common.base.Preconditions;
import org.jesse.game.content.event.christmas2019.ChristmasUtils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.cutscene.Cutscene;
import org.jesse.game.world.entity.player.cutscene.actions.CameraLookAction;
import org.jesse.game.world.entity.player.cutscene.actions.CameraPositionAction;
import org.jesse.game.world.entity.player.cutscene.actions.CameraResetAction;
import org.jesse.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;

import static org.jesse.game.content.event.christmas2019.ChristmasConstants.UNFROZEN_GUESTS_HASH_KEY;

/**
 * @author Kris | 21/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PastScourgeCutsceneP2Repeat extends Cutscene {
    @Override
    public void build() {
        addActions(0, () -> player.lock(), () -> player.getVarManager().sendBit(4606, 1), new CameraPositionAction(player, new Location(2079, 5398, 0), 1600, 100, 100), new CameraLookAction(player, new Location(2079, 5401, 0), 200, 100, 100), () -> player.sendMessage("You've broken the curse in the wrong order!"));
        addActions(1, () -> player.sendMessage("The curse re-establishes itself!"), () -> {
            final int hash = player.getNumericAttribute(UNFROZEN_GUESTS_HASH_KEY).intValue();
            final String guests = ChristmasUtils.getFrozenGuestOrder(player);
            final char[] chars = guests.toCharArray();
            for (int i = chars.length - 1; i >= 0; i--) {
                final char character = chars[i];
                final FrozenGuest respectiveGuest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getConstant() == character);
                Preconditions.checkArgument(respectiveGuest != null);
                final boolean isUnfrozen = ((hash >> respectiveGuest.ordinal()) & 1) == 1;
                if (isUnfrozen) {
                    final NPC npc = World.findNPC(respectiveGuest.getBaseNPC(), respectiveGuest.getTile(), 1).orElseThrow(RuntimeException::new);
                    npc.setInvalidAnimation(respectiveGuest.getPreFreezeAnimation());
                    player.getPacketDispatcher().sendGraphics(new Graphics(2507), npc.getLocation());
                }
            }
            player.addAttribute(UNFROZEN_GUESTS_HASH_KEY, 0);
        });
        addActions(2, () -> {
            for (final FrozenGuest guest : FrozenGuest.getValues()) {
                player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(guest.getBaseObject()).getVarbitId(), 1);
            }
        });
        final String chars = ChristmasUtils.getFrozenGuestOrder(player);
        final int length = chars.length();
        for (int i = 0; i < length; i++) {
            final char character = chars.charAt(i);
            addActions(3 + (i * 2), () -> {
                final FrozenGuest guest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getConstant() == character);
                Preconditions.checkArgument(guest != null);
                final Location tile = guest.getTile();
                final WorldObject object = World.getObjectWithId(tile, guest.getBaseObject());
                World.sendObjectAnimation(object, new Animation(15108));
                player.getPacketDispatcher().sendGraphics(new Graphics(1010, 0, 150), tile);
            });
        }
        addActions(15, player::unlock, new CameraResetAction(player), () -> player.getVarManager().sendBit(4606, 0), () -> ChristmasUtils.refreshAllVarbits(player));
    }
}
