package org.jesse.game.content.alternatetables.impl;

import org.jesse.game.content.elven.obj.NewCrystalChestLoot;
import org.jesse.game.content.alternatetables.AlternateTableDropProvider;
import org.jesse.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class CrystalChestEnhancedTable implements AlternateTableDropProvider {
    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if (entries.size() == 0)
            entries.addAll(NewCrystalChestLoot.toEntries(true));
        return entries;
    }

    @Override
    public String getName() {
        return "C.Key - Enhanced";
    }
}
