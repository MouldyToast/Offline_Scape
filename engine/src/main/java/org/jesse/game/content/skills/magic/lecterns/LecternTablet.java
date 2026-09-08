package org.jesse.game.content.skills.magic.lecterns;

import org.jesse.game.item.Item;

/**
 * @author Kris | 03/09/2019 08:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface LecternTablet {

    int getLevel();
    float getExperience();
    Item[] getRunes();
    Item getTab();
    default int type() {
        return -1;
    }

}
