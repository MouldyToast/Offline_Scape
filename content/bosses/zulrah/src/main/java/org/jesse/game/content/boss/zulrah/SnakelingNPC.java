package org.jesse.game.content.boss.zulrah;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;

import java.util.Collections;

/**
 * @author Kris | 17. march 2018 : 17:30.41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SnakelingNPC extends NPC implements CombatScript {

	private static final Animation ANIM = new Animation(2413);
	private static final SoundEffect IMPACT = new SoundEffect(1930, 5);
	private int ticks;

	public SnakelingNPC(final ZulrahNPC zulrah, final Player player, final Location tile) {
		super(Utils.random(3) == 0 ? 2046 : 2045, tile, Direction.SOUTH, 5);
		setForceMultiArea(true);
		setAggressionDistance(25);
		setAttackDistance(6);
		setMaxDistance(25);
		setSpawned(true);
		lock(3);
		setAnimation(ANIM);
        World.sendSoundEffect(tile, IMPACT);
		this.player = player;
		this.zulrah = zulrah;
		this.supplyCache = false;
	}

	private final ZulrahNPC zulrah;
	private final Player player;

	@Override
	public void processNPC() {
	    if (zulrah.isStopped()) {
	        return;
        }
	    if (++ticks >= 67) {
	        this.applyHit(new Hit(1, HitType.REGULAR));
	        return;
        }
		if (isLocked() || isDead()) {
			return;
		}
		if (combat.getTarget() != player) {
			combat.setTarget(player);
		}
		combat.process();
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		zulrah.getSnakelings().remove(this);
		if (source instanceof final Player player) {
			zulrah.getSnakelingsDeathTicks().add(WorldThread.getCurrentCycle());
			if (zulrah.getSnakelingsDeathTicks().size() >= 3 && Collections.frequency(zulrah.getSnakelingsDeathTicks(), zulrah.getSnakelingsDeathTicks().get(0)) == zulrah.getSnakelingsDeathTicks().size()) {
				player.getCombatAchievements().complete(CAType.SNAKE_SNAKE_SNAAAAAAKE);
			}
		}
	}

    private static final SoundEffect PROJ_SOUND = new SoundEffect(224, 15);
    private static final SoundEffect IMPACT_SOUND = new SoundEffect(794, 2770, 15);
    private static final SoundEffect MELEE_SOUND = new SoundEffect(794, 15);

    private static final Projectile MAGIC_PROJ = new Projectile(1230, 60, 64, 30, 15, 18, 0, 5);

    @Override
	public int attack(final Entity target) {
        setAnimation(combatDefinitions.getAttackDefinitions().getAnimation());
		if (id == 2045) {
			zulrah.delayHit(0, new Hit(this, getRandomMaxHit(this, 15, MELEE, target), HitType.MELEE));
			World.sendSoundEffect(this, MELEE_SOUND);
		} else {
			World.sendSoundEffect(this, PROJ_SOUND);
			zulrah.delayHit(World.sendProjectile(this, target, MAGIC_PROJ), new Hit(this, getRandomMaxHit(this, 15, MAGIC, target), HitType.MAGIC));
			World.sendSoundEffect(target, new SoundEffect(IMPACT_SOUND.getId(), IMPACT_SOUND.getRadius(), MAGIC_PROJ.getProjectileDuration(this.getLocation(), target.getLocation())));
		}
		return 4;
	}

	public ZulrahNPC getZulrah() {
		return zulrah;
	}

}
