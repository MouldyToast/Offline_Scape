package org.jesse.game.content.minigame.fightcaves.npcs;

import org.jesse.game.content.minigame.fightcaves.FightCaves;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 8. nov 2017 : 21:25.25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
final class YtMejKot extends FightCavesNPC implements CombatScript {

    private static final SoundEffect attackSound = new SoundEffect(608);
    private static final SoundEffect healSound = new SoundEffect(167);

    YtMejKot(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
    }
	
	private static final Graphics GFX = new Graphics(444, 0, 550);
	private static final Animation ANIMATION = new Animation(2639);
	private int hitsDealtCount;
    private final int maximumHealth = getMaxHitpoints() >> 1;
	
	@Override
	public void sendDeath() {
		super.sendDeath();
		caves.checkWave(this);
	}

    @Override
    public int attack(Entity target) {
	    if (!heal()) {
	        playSound(attackSound);
	        delayHit(0, target, new Hit(this, getRandomMaxHit(this, 25, MELEE, target), HitType.MELEE));
            setAnimation(combatDefinitions.getAttackAnim());
        }
        return combatDefinitions.getAttackSpeed();
    }

    /**
     * Whether the attack is replaced with heal effect or not.
     * Heal effect has a chance of occurring every other attack, granted the npc's health is below half.
     * Chance of the effect triggerring is 1/10.
     * @return whether or not to call heal effect.
     */
    private boolean heal() {
        if ((++hitsDealtCount & 0x1) == 1 && getHitpoints() <= maximumHealth && Utils.random(9) == 0) {
            setGraphics(GFX);
            setAnimation(ANIMATION);
            int distanceX, distanceY, size;
            for (NPC npc : caves.getMonsters()) {
                distanceX = npc.getX() - getX();
                distanceY = npc.getY() - getY();
                size = npc.getSize();
                if (distanceX > size || distanceX < -1
                        || distanceY > size || distanceY < -1) {
                    continue;
                }
                playSound(healSound);
                npc.heal(Utils.random(1, 10));
            }
            return true;
        }
        return false;
    }
}
