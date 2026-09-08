package org.jesse.game.world.entity.npc.impl.motherlode;

import org.jesse.game.content.minigame.motherlode.MotherlodeArea;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ProspectorPercy extends NPC implements Spawnable {

    private static int skip = 0;

    private static final List<ForceTalk> SINGLE_WHEEL = Arrays.asList(
            new ForceTalk("Ye'd better fix that wheel."),
            new ForceTalk("We got us a jammed wheel!"),
            new ForceTalk("Git yer hammer an' fix that wheel!"));

    private static final List<ForceTalk> BOTH_WHEELS = Arrays.asList(
            new ForceTalk("Git yer hammer an' fix them wheels!"),
            new ForceTalk("Both them wheels be jammed!"),
            new ForceTalk("That water ain't flowing!"));

    public ProspectorPercy(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void processNPC() {
        super.processNPC();

        if(skip == 0 && (!MotherlodeArea.WATER_WHEELS.get(true) || !MotherlodeArea.WATER_WHEELS.get(false))) {
            /** Both wheels aren't working! */
            if (!MotherlodeArea.WATER_WHEELS.get(true) && !MotherlodeArea.WATER_WHEELS.get(false)) {
                setForceTalk(BOTH_WHEELS.get(Utils.random(0, 2)));
                skip = 5;
            } else {
                /** One wheel isnt working */
                if (!MotherlodeArea.WATER_WHEELS.get(true) || !MotherlodeArea.WATER_WHEELS.get(false)) {
                    setForceTalk(SINGLE_WHEEL.get(Utils.random(0, 2)));
                    skip = 5;
                }
            }
        } else
            if(!MotherlodeArea.WATER_WHEELS.get(true) || !MotherlodeArea.WATER_WHEELS.get(false))
                skip--;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 6562;
    }
}
