package org.jesse.plugins.itemonobject;

import org.jesse.game.content.minigame.castlewars.CastleWars;
import org.jesse.game.content.minigame.castlewars.CastleWarsOverlay;
import org.jesse.game.content.minigame.castlewars.CastleWarsOverlay.CWarsOverlayVarbit;
import org.jesse.game.content.minigame.castlewars.CastleWarsTeam;
import org.jesse.game.content.minigame.castlewars.CastlewarsRockPatch;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.plugins.itemonnpc.ItemOnBarricadeAction;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class PotionOnCastlewarsRocks implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        if (item.getId() != 4045)
            return;
        /**
         * Explosive potion on rocks
         */
        if (object.getId() == 4437 || object.getId() == 4438) {
            player.getInventory().deleteItem(item);
            World.sendGraphics(ItemOnBarricadeAction.EXPLOSION, object);
            if (object.getId() == ObjectId.ROCKS_4437) {
                World.spawnObject(new WorldObject(4438, object.getType(), object.getRotation(), object));
            } else {
                processVarbits(object, false);
                World.removeObject(object);
            }
            player.sendMessage(object.getId() == 4437 ? "You manage to remove some of the rocks." : "You manage to clear the rest of the rocks.");
        }
        if (object.getId() == ObjectId.CAVE_WALL) {
            final CastlewarsRockPatch wallData = CastlewarsRockPatch.getData(object);
            if (wallData == null) {
                return;
            }
            player.getInventory().deleteItem(item);
            World.sendGraphics(ItemOnBarricadeAction.EXPLOSION, object);
            World.spawnObject(wallData.getPatch());
            player.sendMessage("You've collapsed the tunnel!");
            processVarbits(object, true);
            CharacterLoop.forEach(wallData.getPatch(), 1, Entity.class, entity -> {
                if (CollisionUtil.collides(wallData.getPatch().getX(), wallData.getPatch().getY(), 2, entity.getX(), entity.getY(), entity.getSize())) {
                    if (entity instanceof Player) {
                        entity.applyHit(new Hit(entity.getHitpoints(), HitType.REGULAR));
                    }
                }
            });
        }
    }

    /**
     * Explosive potion to collapse walls
     */
    public static void processVarbits(final WorldObject object, final boolean collapsed) {
        final CastlewarsRockPatch wallData = CastlewarsRockPatch.getData(object);
        if (wallData == null) {
            return;
        }
        final boolean saradomin = CastleWarsTeam.SARADOMIN.getRockPatches().contains(wallData);
        final CastleWarsTeam team = saradomin ? CastleWarsTeam.SARADOMIN : CastleWarsTeam.ZAMORAK;
        final CastleWarsOverlay.CWarsOverlayVarbit varbit = wallData.equals(CastlewarsRockPatch.SOUTH) || wallData.equals(CastlewarsRockPatch.NORTH) ? CWarsOverlayVarbit.ROCKS_NS : CWarsOverlayVarbit.ROCKS_EW;
        CastleWars.setVarbit(team, varbit, collapsed ? 0 : 1);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 4045 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROCKS_4437, ObjectId.ROCKS_4438, ObjectId.CAVE_WALL };
    }
}
