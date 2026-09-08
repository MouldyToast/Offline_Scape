package com.zenyte.game.world.region.area.wilderness;

import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.region.RSPolygon;

public class EdgeIslandBankArea extends WildernessArea {

    @Override
    public boolean isWildernessArea(Position position) {
        return false;
    }

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] {
                edgeIslandBank
        };
    }

    @Override
    public void enter(Player player) {
        player.sendDeveloperMessage("Entering Edge Island Bank, registered safe");
    }

    @Override
    public void leave(Player player, boolean logout) {
        player.sendDeveloperMessage("Left Edge Island Bank, unregistered safe");
    }

    @Override
    public int broadcastValueThreshold() {
        return 0;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        return false;
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        return false;
    }

    @Override
    public boolean processCombat(final Player player, final Entity entity, final String style) {
        return false;
    }

    @Override
    public void onAttack(Player player, Entity entity, String style, CombatSpell spell, boolean splash) {
    }

    @Override
    public String name() {
        return NAME;
    }

    public static final String NAME = "Edge Island Bank";

}
