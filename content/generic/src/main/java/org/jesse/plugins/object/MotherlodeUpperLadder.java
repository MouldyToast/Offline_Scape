package org.jesse.plugins.object;

import org.jesse.game.content.minigame.motherlode.UpperMotherlodeArea;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dialogue.impl.NPCChat;
import org.jesse.game.world.entity.player.privilege.MemberRank;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class MotherlodeUpperLadder implements ObjectAction {

    private static final Location TOP = new Location(3755, 5675, 0);

    private static final Location BOTTOM = new Location(3755, 5672, 0);

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(UpperMotherlodeArea.polygon.contains(player.getLocation()) ? TOP : BOTTOM), getRunnable(player, object, name, optionId, option), getDelay()));
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final boolean ruby = player.getMemberRank().equalToOrGreaterThan(MemberRank.TOPAZ);
        final boolean hasAccess = ruby || player.getBooleanAttribute("motherlode_upstairs");
        final boolean top = UpperMotherlodeArea.polygon.contains(player.getLocation());
        if (top || (hasAccess && player.getSkills().getLevelForXp(SkillConstants.MINING) >= 72)) {
            player.lock();
            WorldTasksManager.schedule(() -> {
                player.setAnimation(top ? Animation.LADDER_DOWN : Animation.LADDER_UP);
                WorldTasksManager.schedule(() -> {
                    player.setLocation(top ? BOTTOM : TOP);
                    player.unlock();
                });
            });
        } else {
            final String rubyMessage = (ruby ? "" : " Ye also need to buy upstairs access from me.");
            final String message = "Ye wants to go up there, eh? Well, ye\'ll need at least 72 Mining first. An\' don\'t think you can fool me with yer potions and fancy stat-boosts. Get yer level up for real." + rubyMessage;
            player.getDialogueManager().start(new NPCChat(player, 6562, message));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 19044, 19045, ObjectId.LADDER_19047, ObjectId.LADDER_19049 };
    }
}
