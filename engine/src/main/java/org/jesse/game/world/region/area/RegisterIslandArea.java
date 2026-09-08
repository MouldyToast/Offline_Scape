package org.jesse.game.world.region.area;

import org.jesse.game.GameInterface;
import org.jesse.game.content.skills.magic.spells.teleports.Teleport;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.model.HintArrow;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;
import org.jesse.game.world.region.area.plugins.PrayerPlugin;
import org.jesse.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import org.jesse.game.world.region.area.plugins.TeleportPlugin;
import org.jesse.game.world.region.area.plugins.TradePlugin;
import org.jesse.plugins.renewednpc.ZenyteGuide;

/**
 * @author Tommeh | 1-2-2019 | 16:03
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RegisterIslandArea extends PolygonRegionArea implements TeleportPlugin, TradePlugin, PrayerPlugin, RandomEventRestrictionPlugin {

    public static final Location ZENYTE_GUIDE_LOCATION = new Location(3094, 3107, 0);

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(12336)};
    }

    @Override
    public void enter(Player player) {
        if (player.getBooleanAttribute("registered")) return;
        World.findNPC(ZenyteGuide.NPC_ID, ZENYTE_GUIDE_LOCATION, 10)
                .ifPresent(npc -> player.getPacketDispatcher().sendHintArrow(new HintArrow(npc)));
        WorldTasksManager.schedule(() -> GameInterface.CHARACTER_DESIGN.open(player), 1);
    }

    @Override
    public void leave(Player player, boolean logout) {
    }

    @Override
    public String name() {
        return "Register Island";
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        return false;
    }

    @Override
    public boolean canTrade(final Player player, final Player partner) {
        return false;
    }

    @Override
    public boolean activatePrayer(final Player player, final Prayer prayer) {
        return false;
    }

}

