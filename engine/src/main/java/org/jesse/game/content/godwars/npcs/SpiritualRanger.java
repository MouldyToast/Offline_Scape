package org.jesse.game.content.godwars.npcs;

import org.jesse.game.content.chambersofxeric.greatolm.scripts.Lightning;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 21/08/2019 00:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpiritualRanger extends SpawnableKillcountNPC implements Spawnable, CombatScript {

    private static final Graphics specialGraphics = new Graphics(1692, 0, 96);
    private static final Projectile specialProjectile = new Projectile(1693, 160, 0, 35, 10, 120, 64, 0);
    private static final Graphics graphics = new Graphics(19, 0, 90);
    private static final Projectile projectile = new Projectile(10, 168, 120, 40, 15, 10, 64, 5);
    private static final Projectile thrownaxeProjectile = new Projectile(1197, 64, 88, 30, 15, 10, 64, 5);
    private static final SoundEffect attackSound = new SoundEffect(2693, 10, 0);

    protected SpiritualRanger(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(getCombatDefinitions().getAttackAnim());
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        if (getId() == NpcId.SPIRITUAL_RANGER_2242) {
            sendHit(target, thrownaxeProjectile);
        } else if (getId() == NpcId.SPIRITUAL_RANGER_11291 && Utils.randomBoolean(8)) {
            setGraphics(specialGraphics);
            Location arrowLocation = target.getLocation().copy();
            int ticks = specialProjectile.build(this, arrowLocation);
            WorldTasksManager.schedule(() -> {
                if (target.getLocation().matches(arrowLocation)) {
                    if (target instanceof Player) {
                        Lightning.deactivateOverheadProtectionPrayers(((Player) target), ((Player) target).getPrayerManager(), true);
                    }
                    target.scheduleHit(this, new Hit(Utils.random(35), HitType.REGULAR), -1);
                    target.freeze(8);
                    target.setGraphics(CombatSpell.BIND.getHitGfx());
                }
            }, ticks);
        } else {
            setGraphics(graphics);
            sendHit(target, projectile);
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    private void sendHit(Entity target, Projectile projectile) {
        delayHit(this, World.sendProjectile(this, target, projectile), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
    }

    @Override
    public boolean canBeMulticannoned(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.SPIRITUAL_RANGER ||
                id == NpcId.SPIRITUAL_RANGER_2242 ||
                id == NpcId.SPIRITUAL_RANGER_3160 ||
                id == NpcId.SPIRITUAL_RANGER_3167 ||
                id == NpcId.SPIRITUAL_RANGER_11291;
    }
}
