package org.jesse.game.content.achievementdiary;

import org.jesse.game.GameInterface;
import org.jesse.game.content.achievementdiary.diaries.*;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import mgi.types.config.enums.EnumDefinitions;

public class DiaryInterface extends Interface {

    @Override
    protected void attach() {
        put(7, "close");
    }

    @Override
    public void open(final Player player) {
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 119);
    }

    @Override
    protected void build() {
        bind("close", player -> player.getInterfaceHandler().closeInterface(this));

    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.DIARY_LIST;
    }
}

