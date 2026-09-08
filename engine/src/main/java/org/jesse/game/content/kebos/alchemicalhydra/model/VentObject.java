package org.jesse.game.content.kebos.alchemicalhydra.model;

import org.jesse.game.content.kebos.alchemicalhydra.HydraPhase;
import org.jesse.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import org.jesse.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import org.jesse.game.util.CollisionUtil;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 04/11/2019 | 19:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class VentObject extends WorldObject {
    private final AlchemicalVent vent;

    public VentObject(final AlchemicalVent vent, final AlchemicalHydraInstance instance) {
        super(vent.getObjectId(), 10, 0, instance.getLocation(vent.getLocation()));
        this.vent = vent;
    }

    public boolean isOnVent(final AlchemicalHydra hydra) {
        return CollisionUtil.collides(hydra.getLocation(), hydra.getSize(), this, 1, 0);
    }

    public boolean isCorrectVent(final AlchemicalHydra hydra) {
        final HydraPhase phase = hydra.getPhase();
        return vent.getWeakeningPhase().equals(phase);
    }
}
