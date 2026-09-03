package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.Dragonfire;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireProtection;
import com.zenyte.game.world.entity.npc.impl.slayer.dragons.DragonfireType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;


public class KingBlackDragon extends BreachEntity implements Spawnable, CombatScript {


    public KingBlackDragon(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.KING_BLACK_DRAGON_12440;
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player player)) return 0;
        NPC npc = this;
        int random = Utils.random(isWithinMeleeDistance(npc, target) ? 2 : 1);
        if (random == 0) {
            npc.setAnimation(DRAGONFIRE_ANIM);
            World.sendProjectile(npc, target, DRAGONFIRE_PROJ);
            Dragonfire dragonfire = new Dragonfire(DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player));
            delayHit(
                npc,
                DRAGONFIRE_PROJ.getTime(npc, target),
                target,
                new Hit(
                    npc,
                    Utils.random(dragonfire.getDamage()),
                    HitType.REGULAR
                ).onLand((hit) -> {
                    player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
                    PlayerCombat.appendDragonfireShieldCharges(player);
                    target.setGraphics(DRAGONFIRE_GFX);
                })
            );
        }
        else if (random == 2) {
            if (Utils.random(1) == 0) {
                npc.setAnimation(ATTACK_ANIM);
            }
            else {
                npc.setAnimation(SECONDARY_ATTACK_ANIM);
            }
            delayHit(npc, 0, target,
                new Hit(npc, getRandomMaxHit(npc, 25, CombatScript.MELEE, target), HitType.MELEE));
        }
        else {
            int atk = Utils.random(2);
            switch (atk) {
                case 0 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM);
                    World.sendProjectile(npc, target, POISON_PROJ);
                    Dragonfire.DragonfireBuilder dragonfire = new Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player)
                    ) {
                        @Override
                        public int getDamage() {
                            float tier = getAccumulativeTier();
                            if (tier == 0.0f) return 65;
                            if (tier == 0.25f) return 60;
                            if (tier == 0.5f) return 35;
                            if (tier == 0.75f) return 25;
                            return 10;
                        }
                    };
                    delayHit(
                        npc,
                        POISON_PROJ.getTime(npc, target),
                        target,
                        new Hit(
                            npc,
                            Utils.random(dragonfire.getDamage()),
                            HitType.REGULAR
                        ).onLand(hit -> {
                            player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's poisonous breath"));
                            if (Utils.random(3) == 0) {
                                target.getToxins().applyToxin(Toxins.ToxinType.POISON, 8, npc);
                            }
                            target.setGraphics(POISON_GFX);
                            PlayerCombat.appendDragonfireShieldCharges(player);
                        })
                    );
                }
                case 1 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM);
                    World.sendProjectile(npc, target, FREEZING_PROJ);
                    Dragonfire.DragonfireBuilder dragonfire = new Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player)
                    ) {
                        @Override
                        public int getDamage() {
                            float tier = getAccumulativeTier();
                            if (tier == 0.0f) return 65;
                            if (tier == 0.25f) return 60;
                            if (tier == 0.5f) return 35;
                            if (tier == 0.75f) return 25;
                            return 10;
                        }
                    };
                    delayHit(
                        npc,
                        FREEZING_PROJ.getTime(npc, target),
                        target,
                        new Hit(
                            npc,
                            Utils.random(dragonfire.getDamage()),
                            HitType.REGULAR
                        ).onLand(hit -> {
                            target.setGraphics(FREEZING_GFX);
                            PlayerCombat.appendDragonfireShieldCharges(player);
                            player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's icy breath"));
                            if (Utils.random(3) == 0) {
                                player.freeze(
                                    16,
                                    0,
                                    entity -> player.sendMessage("The dragon's icy attack freezes you.")
                                );
                            }
                        })
                    );
                }
                case 2 -> {
                    npc.setAnimation(DRAGONFIRE_ANIM);
                    World.sendProjectile(npc, target, SHOCKING_PROJ);
                    Dragonfire.DragonfireBuilder dragonfire = new Dragonfire.DragonfireBuilder(
                        DragonfireType.STRONG_DRAGONFIRE, 65, DragonfireProtection.getProtection(this, player)
                    ) {
                        @Override
                        public int getDamage() {
                            float tier = getAccumulativeTier();
                            if (tier == 0.0f) return 65;
                            if (tier == 0.25f) return 60;
                            if (tier == 0.5f) return 35;
                            if (tier == 0.75f) return 25;
                            return 10;
                        }
                    };
                    delayHit(
                        npc,
                        SHOCKING_PROJ.getTime(npc, target),
                        target,
                        new Hit(
                            npc,
                            Utils.random(dragonfire.getDamage()),
                            HitType.REGULAR
                        ).onLand(hit -> {
                            target.setGraphics(SHOCKING_GFX);
                            PlayerCombat.appendDragonfireShieldCharges(player);
                            player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's shocking breath"));
                            if (Utils.random(3) == 0) {
                                player.getSkills().drainCombatSkills(2);
                                player.sendMessage("The dragon's shocking attack drains your stats.");
                            }
                        })
                    );
                }
            }
        }
        return 4;
    }

    private static final Projectile DRAGONFIRE_PROJ = new Projectile(393, 40, 30, 40, 15, 28, 0, 5);
    private static final Projectile POISON_PROJ = new Projectile(394, 40, 30, 40, 15, 28, 0, 5);
    private static final Projectile FREEZING_PROJ = new Projectile(395, 40, 30, 40, 15, 28, 0, 5);
    private static final Projectile SHOCKING_PROJ = new Projectile(396, 40, 30, 40, 15, 28, 0, 5);

    private static final Graphics DRAGONFIRE_GFX = new Graphics(430, 0, 90);
    private static final Graphics POISON_GFX = new Graphics(429, 0, 90);
    private static final Graphics FREEZING_GFX = new Graphics(431, 0, 90);
    private static final Graphics SHOCKING_GFX = new Graphics(428, 0, 90);

    private static final Animation ATTACK_ANIM = new Animation(80);
    private static final Animation SECONDARY_ATTACK_ANIM = new Animation(91);
    private static final Animation DRAGONFIRE_ANIM = new Animation(81);
}