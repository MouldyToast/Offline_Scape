package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

public class CommanderZilyana extends BreachEntity implements Spawnable, CombatScript {

    public CommanderZilyana(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.COMMANDER_ZILYANA_12445;
    }

    public static final Animation meleeAnimation = new Animation(6967);
    public static final Animation magicAnimation = new Animation(6970);
    public static final Graphics magicGraphics = new Graphics(1221);
    public static final SoundEffect meleeAttackSound = new SoundEffect(3876, 10, 0);
    public static final SoundEffect specialHittingSound = new SoundEffect(3887, 10, 30);

    @Override
    public int attack(final Entity target) {
        final CommanderZilyana npc = this;
        final int style = Utils.random(2);
        if (style < 2) {
            npc.setAnimation(meleeAnimation);
            World.sendSoundEffect(getMiddleLocation(), meleeAttackSound);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 31, MELEE, target), HitType.MELEE));
        }
        else {
            npc.freeze(2);
            npc.setAnimation(magicAnimation);
            for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(npc, 20, MAGIC, t);
                //zilyana deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                if (damage > 0) {
                    damage = Utils.random(10, 20);
                }
                delayHit(npc, 0, t, new Hit(npc, damage, HitType.MAGIC).onLand(hit -> t.setGraphics(magicGraphics)));
                World.sendSoundEffect(new Location(target.getLocation()), specialHittingSound);
            }
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
