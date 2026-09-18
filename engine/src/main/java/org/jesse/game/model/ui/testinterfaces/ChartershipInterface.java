package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.achievementdiary.diaries.KaramjaDiary;
import org.jesse.game.content.sailing.CharterLocation;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.PlainChat;

import static org.jesse.game.content.sailing.CharterLocation.*;

/**
 * @author Tommeh | 27-10-2018 | 20:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ChartershipInterface extends Interface {
    // Port list order for the list-based chartering interface (885:4 list_content)
    private static final CharterLocation[] PORT_ORDER = {
        PORT_TYRAS, PORT_PHASMATYS, CATHERBY, SHIPYARD,
        MUSA_POINT, BRIMHAVEN, PORT_KHAZARD, PORT_SARIM,
        MOS_LE_HARMLESS, CORSAIR_COVE, PRIFDDINAS
    };

    @Override
    protected void attach() {
        put(4, "Port list");  // 885:4 list_content - contains all port entries
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Port list", (player, slotId, itemId, option) -> {
            if (slotId < 0 || slotId >= PORT_ORDER.length) {
                return;
            }
            charter(player, PORT_ORDER[slotId]);
        });
    }

    private void charter(final Player player, final CharterLocation destination) {
        final CharterLocation location = CharterLocation.getLocation(player.getLocation());
        if (location == null || destination == null) {
            return;
        }
        final Item cost = new Item(995, destination.getCosts()[location.ordinal()]);
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        if (!player.getInventory().containsItem(cost)) {
            player.getDialogueManager().start(new PlainChat(player, "You don't have enough gold in your inventory to sail<br>to " + destination + "."));
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("To sail to " + destination + " from here will cost you " + cost.getAmount() + " gold.<br><br>Are you sure you want to pay that?");
                options(TITLE, "Ok", "Choose again", "No").onOptionOne(() -> {
                    player.lock(3);
                    new FadeScreen(player, () -> {
                        if (location.equals(CharterLocation.SHIPYARD)) {
                            player.getAchievementDiaries().update(KaramjaDiary.CHARTER_A_SHIP_FROM_SHIPYARD);
                        }
                        player.getInventory().deleteItem(cost);
                        player.setLocation(destination.getLocation());
                    }).fade(3);
                }).onOptionTwo(() -> GameInterface.SHIP_DESTINATION_CHART.open(player));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SHIP_DESTINATION_CHART;
    }
}
