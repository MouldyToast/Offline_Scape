package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.util.Direction;
import org.jesse.game.util.ProjectileUtils;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.CharacterLoop;

/**
 * @author Kris | 21/04/2019 18:29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanSpawn extends NPC {

    private static final Graphics SPLASH = new Graphics(1295);
    private static final Animation SPAWN = new Animation(7160);
    private static final Animation ABOUT_TO_EXPLODE = new Animation(7159);
    private static final SoundEffect sound = new SoundEffect(3033, 5, 0);
    private static final SoundEffect aboutToExplodeSound = new SoundEffect(665, 5, 0);
    private static final SoundEffect explodeSound = new SoundEffect(1021, 5, 0);

    public LizardmanSpawn(final Location tile, final Player target, final boolean temple) {
        super(6768, tile, Direction.SOUTH, 5);
        ticks = Utils.random(10, 20);
        this.target = target;
        this.temple = temple;
        this.setFaceEntity(target);
        World.sendSoundEffect(tile, sound);
    }

    private final boolean temple;
    private int ticks;
    private final Player target;

    @Override
    public void processNPC() {
        resetWalkSteps();
        if ((ProjectileUtils.isProjectileClipped(null, null, this, target, true)) || !Utils.isOnRangeExcludingDiagonal(getX(),
                getY(), 1, target.getX(), target.getY(), target.getSize(), 0)) {
            calcFollow(target, isRun() ? 2 : 1, true, isIntelligent(), false);
        }

        if (ticks < 4) {
            this.lock(2);
            setAnimation(ABOUT_TO_EXPLODE);
            World.sendSoundEffect(getLocation(), aboutToExplodeSound);
        }
        if (--ticks == 0) {
            World.sendSoundEffect(getLocation(), explodeSound);
            setGraphics(SPLASH);
            World.sendGraphics(SPLASH, new Location(getLocation()));
            finish();
            CharacterLoop.find(getLocation(), temple ? 1 : 2, Player.class, player -> !player.isDead() && !isProjectileClipped(target, true) && (this.isMultiArea() || player == target)).forEach(p ->
                    p.applyHit(new Hit(this, 5 + Math.min(5, ((int) (p.getLocation().getDistance(getLocation()) * 2F))), HitType.REGULAR)));
        }
    }

    @Override
    public NPC spawn() {
        this.lock(1);
        setAnimation(SPAWN);
        return super.spawn();
    }

}
