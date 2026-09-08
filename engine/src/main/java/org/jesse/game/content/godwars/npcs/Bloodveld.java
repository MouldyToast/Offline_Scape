package org.jesse.game.content.godwars.npcs;

import org.jesse.game.content.achievementdiary.diaries.WildernessDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;

/**
 * @author Kris | 21/08/2019 00:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bloodveld extends SpawnableKillcountNPC implements Spawnable, CombatScript {
    public Bloodveld(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean isTolerable() {
        return true;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("bloodveld");
    }

    private static final SoundEffect attackSound = new SoundEffect(312, 10, 0);

    @Override
    public int attack(final Entity target) {
        final Bloodveld npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, MAGIC, target), HitType.MELEE));
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (!(source instanceof Player)) {
            return;
        }
        final Player player = (Player) source;
        player.getAchievementDiaries().update(WildernessDiary.KILL_A_BLOODVELD);
        player.getCombatAchievements().complete(CAType.THE_DEMONIC_PUNCHING_BAG);
    }
}
