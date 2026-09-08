package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.content.skills.fishing.FishingLocations;
import org.jesse.game.content.skills.fishing.FishingLocations.SpotLocations;
import org.jesse.game.content.skills.fishing.SpotDefinitions;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

public class FishingSpot extends NPC implements Spawnable {

    private int ticks;
    private SpotLocations area;
    private Location current;

    public FishingSpot(final int id, final Location location, final Direction direction, final int radius) {
        super(id, location, direction, radius);
        if (location == null)
            return;
        current = location;
        setRadius(0);
        area = SpotLocations.getArea(location.getRegionId());
		ticks = !hasOtherAreas() ? 1500 : Utils.random(200, 600);
		FishingLocations.occupied.add(location);
	}

	@Override
    public boolean isPathfindingEventAffected() {
        return false;
    }
	
	@Override
	public void processNPC() {
		super.processNPC();
		processSpot();
	}

	protected void processSpot() {
        if(--ticks == 0) {
            //Needs to be executed thru a task cus otherwise u will face where the npc goes.
            //WorldTasksManager.schedule(() -> {
            if (area != null && area.getLocations() != null && area.getLocations().length > 0) {
                Location location = area.getLocations()[Utils.random(area.getLocations().length - 1)];
                int tryCount = 100;
                while (--tryCount > 0 && FishingLocations.occupied.contains(location)) {
                    location = area.getLocations()[Utils.random(area.getLocations().length - 1)];
                }
                FishingLocations.occupied.remove(current);
                FishingLocations.occupied.add(location);

                setLocation(location);
                current = location;
            }
            ticks = !hasOtherAreas() ? 1500 : Utils.random(200, 600);
            //});
        }
    }

    public boolean hasOtherAreas() {
        return area != null && area.getLocations() != null && area.getLocations().length > 0;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return (id < 7730 || id > 7733) && SpotDefinitions.getNpcs().contains(id);
    }

    public int getTicks() {
        return ticks;
    }
}
