package org.jesse.game.content.alternatetables.impl;

import org.jesse.game.content.alternatetables.AlternateTableDropProvider;
import org.jesse.game.world.entity.npc.drop.viewerentry.DropViewerEntry;
import org.jesse.plugins.item.mysteryboxes.RegalMysteryBox;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class MysteryBoxRegalTable implements AlternateTableDropProvider {

    static ObjectArrayList<DropViewerEntry> entries = new ObjectArrayList<>();

    @Override
    public ObjectArrayList<DropViewerEntry> getEntries() {
        if(entries.isEmpty())
            entries.addAll(RegalMysteryBox.toEntries());
        return entries;
    }

    @Override
    public String getName() {
        return "M.Box - Regal";
    }
}
