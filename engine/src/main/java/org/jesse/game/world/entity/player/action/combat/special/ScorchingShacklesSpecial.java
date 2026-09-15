package org.jesse.game.world.entity.player.action.combat.special;

import org.jesse.game.task.WorldTask;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.race.Demon;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.action.combat.SpecialAttackScript;

import static org.jesse.game.task.WorldTasksManager.schedule;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
public class ScorchingShacklesSpecial implements SpecialAttackScript {
    public static final String BURNING_DAMAGE_KEY = "burning_damage";
    public static final int BURN_TICK_INTERVAL = 4;
    public static final int BURN_DAMAGE_PER_TICK = 1;
    private final Projectile projectile =
        new Projectile(2807, 128, 128, 0);

    @Override
    public void attack(Player player, PlayerCombat combat, Entity target) {
        if (!Demon.isDemon(((NPC) target), true)) {
            player.sendMessage("Scorching shackles won't work against a non-demon enemy.");
            return;
        }
        player.setAnimation(new Animation(player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle())));
        player.setGraphics(new Graphics(2806));
        var hit = combat.getHit(player, target, 1.3, 1, 1, false);
        schedule(new WorldTask() {
            @Override
            public void run() {
                var delay = World.sendProjectile(player, target, projectile);
                combat.delayHit(delay, hit);
                target.setGraphics(new Graphics(2808, delay, 0));
                target.freeze(20);
                var attribute = target.getTemporaryAttributes().get("burningDamage");
                if (attribute != null) {
                    var burn = Integer.parseInt(attribute.toString());
                    burn += 5;
                    target.getTemporaryAttributes().put("burningDamage", burn);
                }
                else
                    target.getTemporaryAttributes().put("burningDamage", 5);
                stop();
            }
        }, 1, 0);
    }
}
