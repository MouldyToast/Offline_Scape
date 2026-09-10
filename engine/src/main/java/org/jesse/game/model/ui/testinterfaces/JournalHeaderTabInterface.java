package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.InterfaceHandler;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 12/05/2019 | 18:24
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JournalHeaderTabInterface extends Interface {

    @Override
    protected void attach() {
        put(2, "Character Summary");
        put(10, "Quests");
        put(18, "Achievement Diary");
        put(26, "Server Events");
        put(44, "Game Noticeboard");
        put(34, "Extra");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Quests", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.QUEST_TAB));
        bind("Achievement Diary", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.ACHIEVEMENT_DIARIES));
        bind("Character Summary", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.CHARACTER_SUMMARY));
        bind("Extra", player -> player.getInterfaceHandler().setJournal(InterfaceHandler.Journal.EXTRA));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.JOURNAL_HEADER_TAB;
    }
}
