package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.content.minigame.castlewars.CastleWars;
import org.jesse.game.content.minigame.castlewars.CastleWarsTeam;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity._Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.Flags;
import org.jesse.game.world.region.Chunk;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BarricadeNPC extends NPC {
    /**
     * True - Saradomin
     * False - Zamorak
     */
    private final boolean team;
    private int burnTicks = 0;

    public BarricadeNPC(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.team = id < 5723;
        if (team) {
            CastleWarsTeam.setSaraBarricades(CastleWarsTeam.getSaraBarricades() + 1);
        } else {
            CastleWarsTeam.setZamBarricades(CastleWarsTeam.getZamBarricades() + 1);
        }
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (getId() == 5723 || getId() == 5725) {
            burnTicks++;
        } else {
            burnTicks = 0;
        }
        if (burnTicks == 5 || !CastleWars.isActive()) {
            updateBarricades();
            finish();
        }
    }

    public void updateBarricades() {
        if (team) {
            CastleWarsTeam.setSaraBarricades(Math.max(0, CastleWarsTeam.getSaraBarricades() - 1));
        } else {
            CastleWarsTeam.setZamBarricades(Math.max(0, CastleWarsTeam.getZamBarricades() - 1));
        }
    }

    @Override
    public void finish() {
        updateBarricades();
        super.finish();
    }

    @Override
    public boolean isMovableEntity() {
        return false;
    }

    @Override
    public void unclip() {
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        int hash;
        int lastHash = -1;
        Chunk chunk = null;
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                if ((hash = Chunk.getChunkHash(x1 >> 3, y1 >> 3, z)) != lastHash) {
                    chunk = World.getChunk(lastHash = hash);
                }
                assert chunk != null;
                World.getRegion(_Location.getRegionId(x1, y1), true).removeFlag(z, x1 & 63, y1 & 63, Flags.OCCUPIED_BLOCK_NPC | Flags.OCCUPIED_BLOCK_PLAYER | Flags.OCCUPIED_PROJECTILE_BLOCK_NPC | Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER);
            }
        }
    }

    @Override
    public void clip() {
        if (isFinished()) {
            return;
        }
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                World.getRegion(_Location.getRegionId(x1, y1), true).addFlag(z, x1 & 63, y1 & 63, Flags.OCCUPIED_BLOCK_NPC | Flags.OCCUPIED_BLOCK_PLAYER | Flags.OCCUPIED_PROJECTILE_BLOCK_NPC | Flags.OCCUPIED_PROJECTILE_BLOCK_PLAYER);
            }
        }
    }

    @Override
    public void setRespawnTask() {
    }

    @Override
    public void autoRetaliate(final Entity source) {
    }

    @Override
    public void setFaceEntity(final Entity entity) {
    }

    @Override
    public final void setFaceLocation(final Location tile) {
    }
}
