package org.jesse.game.content.boss.wildernessbosses.spiders;

import org.jesse.game.task.TickTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;

/**
 * @author Cresinkel
 */
public class WebObject extends WorldObject {

    private final NPC boss;
    private final Graphics WEB_HURTING_GFX = new Graphics(2361);

    public WebObject(int id, int type, int rotation, int x, int y, NPC boss) {
        super(id, type, rotation, x, y, 2);
        this.boss = boss;
    }

    public void start() {
        startTask(this);
    }

    public void process() {
        if (!(boss.isDead() || boss.isDying() || boss.isFinished()) && this.getId() != 47085) {
            CharacterLoop.forEach(this.getLocation(), 0, Player.class, p -> {
                if (p.getAraneaBoots().isPlayerWebImmune()) return;
                p.applyHit(new Hit(boss, 3, HitType.DEFAULT, 0));
                p.getVariables().setRunEnergy(p.getVariables().getRunEnergy() - 10);
                p.getPrayerManager().drainPrayerPoints(3);
                p.setGraphics(WEB_HURTING_GFX);
            });
        }
    }

    private void startTask(WebObject web) {
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (boss.isDead() || boss.isDying() || boss.isFinished()) {
                    World.removeObject(web);
                    stop();
                    return;
                }
                if (ticks == 20) {
                    World.removeObject(web);
                    stop();
                    return;
                } else {
                    process();
                }
                ticks++;
            }
        }, 0, 1);
    }
}
