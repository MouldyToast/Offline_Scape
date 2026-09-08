package org.jesse.game.world.entity.npc.actions;

import org.jesse.ContentConstants;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.logger.NearRealityLogger;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Kris | 6. jaan 2018 : 2:53.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum NPCHandler {
    ;

    private static final Logger logger = NearRealityLogger.getLogger(NPCHandler.class);


    public static void add(final Class<?> c) {
        final NPCPlugin action;
        try {
            action = (NPCPlugin) c.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        action.handle();
    }

    public static void handle(@NotNull final Player player, @NotNull final NPC npc, final boolean forcerun,
                              final int option) {
        if (npc.isFinished()) return;
        final int id = npc.getId();
        final NPCDefinitions baseDefinitions = NPCDefinitions.getOrThrow(id);
        final int transmogrifiedId = player.getTransmogrifiedId(baseDefinitions, id);
        if (transmogrifiedId == -1) {
            return;
        }
        final NPCDefinitions transformedDefinitions = NPCDefinitions.getOrThrow(transmogrifiedId);
        final String name = transformedDefinitions.getName();
        final String op = transformedDefinitions.getOption(option);
        NPCPlugin.NPCPluginHandler plugin = NPCPlugin.getHandler(transmogrifiedId, Utils.getOrDefault(op, "null"));
        if (plugin == null) {
            plugin = NPCPlugin.getHandler(id, Utils.getOrDefault(op, "null"));
        }
        final String pluginName = plugin == null ? "Absent" : plugin.getPlugin() == null ? "Default" :
                plugin.getPlugin().getClass().getSimpleName();
        if (logger.isDebugEnabled())
            logger.debug("[" + pluginName + "] " + name + "(base: " + id + (baseDefinitions.getTransmogrifiedIds() == null ? "" : (", visible: " + transmogrifiedId)) + "), option: " + op + "("+option+"), index: " + npc.getIndex() + ", tile: [" + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane() + "], varbit: " + npc.getDefinitions().getVarbit());
        if (op == null || player.isLocked() || !player.isVisibleInViewport(npc) || player.isFullMovementLocked()) {
            return;
        }
        if (player.isStunned()) {
            player.sendFilteredMessage("You're stunned.");
            return;
        }
        player.stopAll();
        //TODO: Rewrite game locks into multiple types and create scrying as a possible type.
        if (player.getTemporaryAttributes().get("Scrying") != null) {
            return;
        }
        if (forcerun) {
            if (player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
                player.sendMessage("NPC: <col=C22731>" + npc.getName(player) + "</col> - <col=C22731>" + transmogrifiedId + "</col>, coords: " + npc.getX() + ", " + npc.getY() + ", " + npc.getPlane());
                player.setLocation(new Location(npc.getLocation()));
                return;
            }
            player.setRun(true);
        }
        if (plugin != null) {
            plugin.getOption().click(player, npc, new NPCPlugin.NPCOption(option, Utils.getOrDefault(op, "null")));
            return;
        }
        player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
            player.stopAll();
            player.setFaceEntity(npc);
            if (npc.getRadius() > 0)
                npc.setInteractingWith(player);
            player.sendMessage("Nothing interesting happens.");
        }, true));
    }

}
