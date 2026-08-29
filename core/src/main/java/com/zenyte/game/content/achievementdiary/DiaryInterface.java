package com.zenyte.game.content.achievementdiary;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
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

