package org.jesse.plugins.renewednpc;

import org.jesse.game.content.achievementdiary.diaries.KourendDiary;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Analytics;
import org.jesse.logger.NearRealityLogger;
import org.slf4j.Logger;

/**
 * @author Kris | 25/11/2018 13:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ShopNPCPlugin extends NPCPlugin {

    private static final Logger logger = NearRealityLogger.getLogger(ShopNPCPlugin.class);

    @Override
    public void handle() {
        bind("Trade", (player, npc) -> {
            final int npcID = player.getTransmogrifiedId(npc.getDefinitions(), npc.getId());
            final ShopNPCHandler handler = ShopNPCHandler.map.get(npcID);
            if (handler == null) {
                logger.warn("Shop handler is null for NPC {}", npcID);
                return;
            }
            Analytics.flagInteraction(player, Analytics.InteractionType.SHOP_NPCS);
            if (handler == ShopNPCHandler.WARRENS_GENERAL_STORE) {
                player.getAchievementDiaries().update(KourendDiary.BROWSE_WARRENWS_GENERAL_STORE);
            }
            player.openShop(handler.shop);
        });
    }

    @Override
    public int[] getNPCs() {
        return ShopNPCHandler.map.keySet().toIntArray();
    }

}
