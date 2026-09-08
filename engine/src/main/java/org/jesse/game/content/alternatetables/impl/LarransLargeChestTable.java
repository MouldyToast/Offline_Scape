package org.jesse.game.content.alternatetables.impl;

import org.jesse.game.content.alternatetables.AlternateTableDropProvider;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import org.jesse.game.world.entity.npc.drop.viewerentry.OtherDropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import static org.jesse.game.item.ids.ItemId.PRIMAL_BATTLEAXE;

public class LarransLargeChestTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            buildEntries();
        return entries;
    }

    private void buildEntries() {
        gen(5300, 8, 20, 2, 75);
        gen(22869, 4, 15, 2, 75);
        gen(5316, 4, 14, 2, 75);
        gen(5304, 4, 12, 2, 75);
        gen(5295, 8, 22, 2, 75);

        gen(11232, 200, 900, 2, 135);
        gen(2354, 300, 600, 2, 135);
        gen(1514, 150, 300, 2, 135);
        gen(452, 23, 75, 2, 135);
        gen(3025, 20, 80, 2, 135);
        gen(6686, 20, 80, 2, 135);
        gen(12696, 20, 60, 2, 135);
        gen(13440, 75, 250, 2, 135);
        gen(11935, 100, 350,  2, 135);

        gen(995, 450000, 2400000, 3, 60);
        gen(1128, 6, 14, 3, 60);
        gen(1080, 6, 16, 3, 60);
        gen(1164, 8, 18, 3, 60);
        gen(441, 500, 800, 3, 60);
        gen(7937, 2000, 8000, 3, 60);
        gen(390, 75, 225, 3, 60);

        gen(11237, 100, 700, 2, 75);
        gen(1618, 35, 90, 2, 75);
        gen(445, 250, 4050, 2, 75);
        gen(22636, 20, 40, 2, 75);
        gen(22634, 20, 40, 2, 75);

        gen(454, 500, 1000, 1, 16);
        gen(1618, 70, 180, 1, 16);
        gen(11232, 65, 170, 1, 16);
        gen(1620, 70, 180, 1, 16);
        gen(PRIMAL_BATTLEAXE, 1, 1, 1, 500);

        entries.add(new OtherDropViewerEntry(ItemId.DAGONHAI_HAT, 1, 1, 1, 315, "This item is on a shared drop table that is rolled at a rate of 1/105"));
        entries.add(new OtherDropViewerEntry(ItemId.DAGONHAI_ROBE_TOP, 1, 1, 1, 315, "This item is on a shared drop table that is rolled at a rate of 1/105"));
        entries.add(new OtherDropViewerEntry(ItemId.DAGONHAI_ROBE_BOTTOM, 1, 1, 1, 315, "This item is on a shared drop table that is rolled at a rate of 1/105"));
   }

    private void gen(int id, int min, int max, int top, int bottom) {
        entries.add(new OtherDropViewerEntry(id, min, max, top, bottom, ""));
    }

    @Override
    public String getName() {
        return "Chests - Larran's Lg";
    }
}
