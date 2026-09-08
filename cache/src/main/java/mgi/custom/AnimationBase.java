package mgi.custom;

import org.jesse.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;
import org.apache.commons.compress.utils.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Kris | 08/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum AnimationBase {
    PLAYER(5000, "assets/animations/bases/player base.dat"),
    TRICK_HALLOWEEN_EMOTE(5002, "assets/animations/bases/trick halloween emote base.dat"),
    THANKSGIVING_TURKEY(5003, "assets/animations/bases/thanksgiving 2019 turkey base.dat"),
    THANKSGIVING_POOF(5004, "assets/animations/bases/thanksgiving 2019 poof base.dat"),
    PLAYER_ALT(5005, "assets/animations/bases/player base.dat"),
    TELEPORT_BASE_5187(5187, "assets/teleportation/animations/bases/Base 5187.dat"),
    TELEPORT_BASE_5188(5188, "assets/teleportation/animations/bases/Base 5188.dat"),
    TELEPORT_BASE_5189(5189, "assets/teleportation/animations/bases/Base 5189.dat"),
    TELEPORT_BASE_5190(5190, "assets/teleportation/animations/bases/Base 5190.dat"),
    ;
    private final int baseId;
    private final String path;

    /**
     * Packs all defined animation bases into the cache. Does not actually write on its own though.
     *
     * @throws IOException the exception thrown if a necessary data file is missing.
     */
    public static final void pack() throws IOException {
        for (final AnimationBase value : values()) {
            pack(value.baseId, IOUtils.toByteArray(new FileInputStream(value.path)));
        }
    }

    public static void pack(int baseID, byte[] bytes) {
        Archive archive = CacheManager.getCache().getArchive(ArchiveType.BASES);
        archive.addGroup(new Group(baseID,
                new File(new ByteBuffer(bytes))));
    }

    AnimationBase(int baseId, String path) {
        this.baseId = baseId;
        this.path = path;
    }

    public int getBaseId() {
        return baseId;
    }

    public String getPath() {
        return path;
    }
}
