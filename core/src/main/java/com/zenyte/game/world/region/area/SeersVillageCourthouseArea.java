package com.zenyte.game.world.region.area;

import com.zenyte.game.content.achievementdiary.AchievementDiariesKeys;
import com.zenyte.game.world.entity.player.PrayerVarbits;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

public class SeersVillageCourthouseArea extends KingdomOfKandarin {
    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[] { new RSPolygon(new int[][] { { 2733, 3476 }, { 2739, 3476 }, { 2739, 3474 }, { 2741, 3474 }, { 2741, 3464 }, { 2731, 3464 }, { 2731, 3474 }, { 2733, 3474 }})};
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        if (player.getVarManager().getBitValue(PrayerVarbits.PIETY) == 1) {
            AchievementDiariesKeys.achievementDiaries(player).update(KandarinDiary.ENTER_SEERS_VILLAGE_COURTHOUSE);
        }
    }

    @Override
    public String name() {
        return "Seers Village Courthouse";
    }
}
