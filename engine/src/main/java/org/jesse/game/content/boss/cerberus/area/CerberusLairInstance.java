package org.jesse.game.content.boss.cerberus.area;

import org.jesse.game.content.boss.cerberus.Cerberus;
import org.jesse.game.content.boss.cerberus.CerberusRoom;
import org.jesse.game.content.slayer.SlayerMaster;
import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.area.plugins.CannonRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfBoundaryException;
import org.jesse.logger.NearRealityPrintStream;

/**
 * @author John J. Woloszyk / Kryeus
 */
public class CerberusLairInstance extends DynamicArea implements AbstractCerberusLair, EntityAttackPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

    private static final Location OUTSIDE_TILE = new Location(1310, 1249, 0);
    private Player player;

    private Cerberus cerberus;

    public CerberusLairInstance(Player player, AllocatedArea area) {
        super(area, 152, 151);
        this.player = player;
    }

    @Override
    public void constructRegion() {
        if (constructed) {
            return;
        }
        GlobalAreaManager.add(this);
        try {
            MapBuilder.copyPlanesMap(area, staticChunkX, staticChunkY, chunkX, chunkY, sizeX, sizeY, 0);
        } catch (OutOfBoundaryException e) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream());
        }
        constructed = true;
        constructed();
    }

    @Override
    public void constructed() {
        player.lock();
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (ticks == 2) {
                    new FadeScreen(player, () -> player.setLocation(getLocation(CerberusRoom.INSTANCED.getExit()))).fade(2);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);

        cerberus = (Cerberus) new Cerberus(getLocation(1239, 1250, 0), this).spawn();
    }

    @Override
    public void enter(Player player) {
        player.setViewDistance(Player.SCENE_DIAMETER);
    }

    @Override
    public void leave(Player player, boolean logout) {
        if(logout)
            player.forceLocation(OUTSIDE_TILE);
        player.resetViewDistance();
    }

    @Override
    public String name() {
        return player.getName() + "Cerberus Instance";
    }


    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        if (entity instanceof NPC) {
            final String name = ((NPC) entity).getDefinitions().getName();
            if (name.equals("Cerberus")) {
                if (!player.getSlayer().isCurrentAssignment(entity) || player.getSlayer().getMaster() == SlayerMaster.KRYSTILIA) {
                    player.sendMessage("You can only kill Cerberus while you're on a slayer task.");
                    return false;
                }
            }
        }
        return true;
    }
}
