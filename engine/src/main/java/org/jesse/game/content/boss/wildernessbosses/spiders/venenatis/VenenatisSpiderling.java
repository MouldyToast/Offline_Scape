package org.jesse.game.content.boss.wildernessbosses.spiders.venenatis;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Cresinkel
 */
public final class VenenatisSpiderling extends NPC implements CombatScript {

    private final Venenatis venenatis;

    public VenenatisSpiderling(Location tile, int radius, Venenatis venenatis) {
        super(NpcId.VENENATIS_SPIDERLING_12000, tile, Direction.NORTH, radius);
        setSpawned(true);
        this.venenatis = venenatis;
        venenatis.spiderlingAlive++;
        setAttackDistance(25);
        setAggressionDistance(25);
        setMaxDistance(50);
    }

    @Override
    public void processNPC() {
        if (venenatis.isDead() || venenatis.isDying() || venenatis.isFinished()) {
            sendDeath();
            return;
        }
        super.processNPC();
    }

    @Override
    public int attack(Entity target) {
        animate();
        attackSound();
        delayHit(0, target, melee(target, 3).onLand(hit -> {
            if (target instanceof Player player) {
                player.getPrayerManager().drainPrayerPoints(1);
                player.sendFilteredMessage("You feel yourself drained by the spiderling");
            }
        }));
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public void sendDeath() {
        venenatis.spiderlingAlive--;
        super.sendDeath();
    }

    @Override
    public boolean isForceAttackable() {
        return true;
    }
}
