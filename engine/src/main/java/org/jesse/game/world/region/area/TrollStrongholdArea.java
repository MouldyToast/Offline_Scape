package org.jesse.game.world.region.area;

import org.jesse.game.content.achievementdiary.diaries.FremennikDiary;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.PolygonRegionArea;
import org.jesse.game.world.region.RSPolygon;

public class TrollStrongholdArea extends PolygonRegionArea {

    @Override
    public RSPolygon[] polygons() {
        return new RSPolygon[]{new RSPolygon(new int[][]{
                {2816, 10112},
                {2816, 10048},
                {2880, 10048},
                {2880, 10112}
        })};
    }

    @Override
    public void enter(Player player) {
        player.getAchievementDiaries().update(FremennikDiary.ENTER_TROLL_STRONGHOLD);
    }

    @Override
    public void leave(Player player, boolean logout) {

    }

    @Override
    public String name() {
        return "Troll Stronghold";
    }
}
