package com.zenyte.game.content.rottenpotato;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zenyte.game.content.rottenpotato.handler.RottenPotatoActionHandler;
import com.zenyte.game.content.rottenpotato.handler.npc.DespawnNpc;
import com.zenyte.game.content.rottenpotato.handler.npc.KillNpc;
import com.zenyte.game.content.rottenpotato.handler.object.AnimateObject;
import com.zenyte.game.content.rottenpotato.handler.player.*;
import com.zenyte.game.world.entity.player.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Christopher
 * @since 3/27/2020
 */
public enum RottenPotatoAction {
    BAN(RottenPotatoItemOption.PUNISHMENT, Ban.class),
    IP_BAN(RottenPotatoItemOption.PUNISHMENT, IPBan.class),
    MUTE(RottenPotatoItemOption.PUNISHMENT, Mute.class),
    IP_MUTE(RottenPotatoItemOption.PUNISHMENT, IPMute.class),
    KICK(RottenPotatoItemOption.PUNISHMENT, Kick.class),
    CLEAR_CONTRACT(RottenPotatoItemOption.UTILITY, RemoveFarmContract.class),
    TELE_TO(RottenPotatoItemOption.UTILITY, TeleTo.class),
    TELE_TO_ME(RottenPotatoItemOption.UTILITY, TeleToMe.class),
    KILL_NPC(RottenPotatoItemOption.NONE, KillNpc.class),
    DESPAWN_NPC(RottenPotatoItemOption.NONE, DespawnNpc.class),
    ANIMATE_OBJECT(RottenPotatoItemOption.NONE, AnimateObject.class),
    ;

    public static final Multimap<RottenPotatoItemOption, RottenPotatoActionHandler> itemOptionMap =
            HashMultimap.create();
    public static final Multimap<RottenPotatoActionType, RottenPotatoActionHandler> actionTypeMap =
            HashMultimap.create();
    private static final RottenPotatoAction[] actions = values();

    static {
        for (RottenPotatoAction action : actions) {
            itemOptionMap.put(action.itemOption, action.actionHandler);
            actionTypeMap.put(action.actionHandler.type(), action.actionHandler);
        }
    }

    private final RottenPotatoItemOption itemOption;
    private final RottenPotatoActionHandler actionHandler;

    RottenPotatoAction(RottenPotatoItemOption itemOption, Class<? extends RottenPotatoActionHandler> actionHandler) {
        this.itemOption = itemOption;
        try {
            this.actionHandler = actionHandler.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Could not create instance for " + actionHandler.getName(), e);
        }
    }

    public static List<RottenPotatoActionHandler> getActions(final Player player,
                                                             final RottenPotatoItemOption itemOption) {
        return itemOptionMap.get(itemOption).stream()
            .filter(handler -> player.getPrivilege().inherits(handler.getPrivilege()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<RottenPotatoActionHandler> getActions(final Player player,
                                                             final RottenPotatoActionType actionType) {
        return actionTypeMap.get(actionType).stream()
            .filter(handler -> player.getPrivilege().inherits(handler.getPrivilege()))
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
