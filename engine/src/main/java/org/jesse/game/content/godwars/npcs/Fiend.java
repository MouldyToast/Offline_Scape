package org.jesse.game.content.godwars.npcs;

import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 00:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Fiend extends SpawnableKillcountNPC implements CombatScript, Spawnable {
    protected Fiend(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    private static final SoundEffect icefiendAttackSound = new SoundEffect(531, 10, 0);
    private static final SoundEffect pyrefiendAttackSound = new SoundEffect(696, 10, 0);

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("icefiend") || name.equalsIgnoreCase("pyrefiend");
    }

    @Override
    public int attack(Entity target) {
        final Fiend npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MAGIC));
        World.sendSoundEffect(getMiddleLocation(), getDefinitions().getLowercaseName().equals("icefiend") ? icefiendAttackSound : pyrefiendAttackSound);
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
