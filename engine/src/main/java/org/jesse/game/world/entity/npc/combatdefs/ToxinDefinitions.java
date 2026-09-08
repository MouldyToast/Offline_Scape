package org.jesse.game.world.entity.npc.combatdefs;

import org.jesse.game.world.entity.Toxins.ToxinType;

/**
 * @author Kris | 18/11/2018 02:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ToxinDefinitions {
    private ToxinType type;
    private int damage;

    public ToxinDefinitions clone() {
        final ToxinDefinitions defs = new ToxinDefinitions();
        defs.type = type;
        defs.damage = damage;
        return defs;
    }

    public ToxinType getType() {
        return type;
    }

    public int getDamage() {
        return damage;
    }
}
