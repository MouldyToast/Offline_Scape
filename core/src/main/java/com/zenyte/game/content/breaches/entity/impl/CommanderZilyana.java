package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

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
