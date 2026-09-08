package org.jesse.game.content.minigame.fightcaves.npcs;

import org.jesse.game.content.minigame.fightcaves.FightCaves;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 27/10/2018 17:37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class SplitTzKek extends FightCavesNPC implements CombatScript {

    private static final SoundEffect attackSound = new SoundEffect(595);

    SplitTzKek(final Location tile, final FightCaves caves) {
        super(TzHaarNPC.TZ_KEK_SPLIT, tile, caves);
    }

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();
        final SoundEffect sound = combatDefinitions.getSpawnDefinitions().getSpawnSound();
        if (sound != null) {
            World.sendSoundEffect(getMiddleLocation(), new SoundEffect(sound.getId(), 5, sound.getDelay()));
        }
        return npc;
    }

    @Override
    public int attack(Entity target) {
        animate();
        playSound(attackSound);
        delayHit(0, target, melee(target, combatDefinitions.getMaxHit()));
        return combatDefinitions.getAttackSpeed();
    }
}
