package org.jesse.game.content.skills.magic.spells.teleports.structures;

import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;

/**
 * @author Kris | 25/03/2019 19:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class XericsTalismanStructure implements TeleportStructure {

    private static final Animation animation = new Animation(3865);
    private static final Graphics graphics = new Graphics(1612);

    @Override
    public Animation getStartAnimation() {
        return animation;
    }

    @Override
    public Graphics getStartGraphics() {
        return graphics;
    }
}
