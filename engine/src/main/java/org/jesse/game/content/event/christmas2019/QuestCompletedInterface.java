package org.jesse.game.content.event.christmas2019;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.world.entity.player.Player;

import java.util.List;

/**
 * @author Kris | 22/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class QuestCompletedInterface extends Interface {
    @Override
    protected void attach() {
        put(4, "Quest completed title");
        put(5, "Quest completed item");
        put(6, "Quest points");
        put(9, "Award line 1");
        put(10, "Award line 2");
        put(11, "Award line 3");
        put(12, "Award line 4");
        put(13, "Award line 5");
        put(14, "Award line 6");
        put(15, "Award line 7");
    }

    @Override
    public void open(Player player) {
        final Object title = player.getTemporaryAttributes().get("quest completed title");
        final Item item = (Item) player.getTemporaryAttributes().get("quest completed item");
        @SuppressWarnings("unchecked")
        final List<String> rewards = (List<String>) player.getTemporaryAttributes().get("quest completed rewards");
        player.getInterfaceHandler().sendInterface(this);
        player.getPacketDispatcher().sendComponentVisibility(getInterface().getId(), getComponent("Quest points"), true);
        player.getPacketDispatcher().sendComponentText(getInterface().getId(), getComponent("Quest completed title"), title);
        player.getPacketDispatcher().sendComponentItem(getInterface().getId(), getComponent("Quest completed item"), item.getId(), 300);
        for (int i = 0; i < 7; i++) {
            player.getPacketDispatcher().sendComponentText(getInterface().getId(), getComponent("Award line " + (i + 1)), i >= rewards.size() ? "" : rewards.get(i));
        }
    }

    @Override
    protected void build() {
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.QUEST_COMPLETED;
    }
}
