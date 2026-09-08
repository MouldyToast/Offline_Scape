package org.jesse.game.model.music;

import org.jesse.cores.ScheduledExternalizable;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.MusicHandler;
import org.jesse.logger.NearRealityLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.util.List;

/**
 * @author Kris | 27. juuli 2018 : 23:42:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class MusicLoader implements ScheduledExternalizable {

	private static final Logger log = NearRealityLogger.getLogger(MusicLoader.class);

    public static final int[] DEFAULT_VARP_VALUES = new int[MusicHandler.VARP_IDS.length];
    private static final int[] excludedVarpValues = new int[]{51200, 536871936, 1210056705, 805306384, 1075840064,
            201871616, -1073741543, -2035273693, 186794284, -703027092, 923736283, -1173808644, -1107383168, -193028019,
            2130705371, -423099905, -678336849, 265464435, 3592};

    public static int getExcludedVarpValue(final int index) {
        if (index < 0 || index >= excludedVarpValues.length) {
            return 0;
        }
        return excludedVarpValues[index];
    }

    @Override
    public Logger getLog() {
        return log;
    }

    @Override
    public int writeInterval() {
        return 0;
    }

    @Override
    public void read(final @NotNull BufferedReader reader) {
        final Music[] music = getGSON().fromJson(reader, Music[].class);
        for (final Music track : music) {
            Music.map.put(track.getName(), track);
            Music.lowercaseMap.put(track.getName().toLowerCase(), track);
            final List<Integer> regions = track.getRegionIds();
            if (regions == null) {
                continue;
            }
            for (final int region : regions) {
                World.getRegion(region).getMusicTracks().add(track);
            }
        }

        MusicHandler.buildMusicEnums();
    }

    @Override
    public void write() {
    }

    @Override
    public String path() {
        return "data/music.json";
    }
}
