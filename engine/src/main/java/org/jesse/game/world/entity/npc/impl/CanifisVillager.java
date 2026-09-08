package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Kris | 24/04/2019 17:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CanifisVillager extends NPC implements Spawnable {
    public CanifisVillager(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        this.originalId = id;
    }

    private final int originalId;
    private int ticks;

    @Override
    public boolean validate(final int id, final String name) {
        return id >= 2613 && id <= 2632;
    }

    @Override
    public void finish() {
        super.finish();
        setId(originalId);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (getId() != originalId) {
            if (!isUnderCombat()) {
                if (++ticks >= 50) {
                    ticks = 0;
                    final float healthPercentage = (float) getHitpoints() / getMaxHitpoints();
                    setTransformation(originalId);
                    setHitpoints((int) (getMaxHitpoints() * healthPercentage));
                }
            }
        }
    }

    private static final Animation transformation = new Animation(6540);

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        if (getId() == originalId) {
            final Entity source = hit.getSource();
            if (source instanceof Player) {
                final Player player = ((Player) source);
                if (player.getEquipment().getId(EquipmentSlot.WEAPON) == 2952) {
                    return;
                }
            }
            final float healthPercentage = (float) getHitpoints() / getMaxHitpoints();
            setTransformation(originalId - 19);
            setAnimation(transformation);
            lock(1);
            setHitpoints((int) (getMaxHitpoints() * healthPercentage));
            faceEntity(source);
            WorldTasksManager.schedule(() -> combat.forceTarget(source), 1);
        }
    }
}
