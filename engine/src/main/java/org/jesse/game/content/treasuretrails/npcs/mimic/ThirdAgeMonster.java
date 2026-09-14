package org.jesse.game.content.treasuretrails.npcs.mimic;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 27/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
class ThirdAgeMonster extends NPC implements CombatScript {
    static final int WARRIOR = 8635, RANGER = 8636, MAGE = 8637;
    private static final Graphics drawbackGraphics = new Graphics(1611, 0, 96);
    private static final Projectile rangedProjectile = new Projectile(1574, 168, 120, 40, 15, 3, 64, 5);

    ThirdAgeMonster(@MagicConstant(valuesFromClass = ThirdAgeMonster.class) int id, @NotNull Location tile) {
        super(id, tile, Direction.SOUTH, 0);
        assert id == WARRIOR || id == RANGER || id == MAGE;
        spawned = true;
        setAnimation(combatDefinitions.getSpawnDefinitions().getSpawnAnimation());
        lock(3);
    }

    @Override
    public int attack(Entity target) {
        if (id == MAGE) {
            useSpell(CombatSpell.EARTH_WAVE, target, combatDefinitions.getMaxHit());
        } else if (id == RANGER) {
            animate();
            setGraphics(drawbackGraphics);
            delayHit(World.sendProjectile(this, target, rangedProjectile), target, ranged(target, combatDefinitions.getMaxHit()));
        } else if (id == WARRIOR) {
            animate();
            executeMeleeHit(target, combatDefinitions.getMaxHit());
        } else {
            throw new IllegalStateException();
        }
        return combatDefinitions.getAttackSpeed();
    }
}
