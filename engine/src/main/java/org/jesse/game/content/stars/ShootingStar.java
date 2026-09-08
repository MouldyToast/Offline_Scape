package org.jesse.game.content.stars;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.content.skills.mining.actions.Mining;
import org.jesse.game.item.Item;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.UpdateFlag;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.CharacterLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jesse.game.world.broadcasts.BroadcastType.LOTTERY;

/**
 * @author Andys1814
 */
public final class ShootingStar extends WorldObject  {
    private static final Logger log = LoggerFactory.getLogger(ShootingStar.class);

    // Your Celestial ring has 673 charges.

    private ShootingStarLocation location;

    private ShootingStarLevel level;

    private int stardust;

    private NPC progressBarNpc;

    private final ShootingStarProgressBar progressBar = new ShootingStarProgressBar(0);

    private boolean undiscovered;

    public ShootingStar(ShootingStarLevel level, ShootingStarLocation location, boolean undiscovered) {
        super(level.getObjectId(), 10, WorldObject.DEFAULT_ROTATION, location.getX(), location.getY(), location.getZ());
        this.location = location;
        this.level = level;
        this.stardust = level.getStardust();
        try {
            this.progressBarNpc = new NPC(10629, getLocation(), Direction.SOUTH, 0);
            this.progressBarNpc.spawn();
        } catch (Exception e) {
            log.error("Failed to spawn progress bar", e);
            this.progressBarNpc = null;
        }

        this.undiscovered = undiscovered;
    }

    public ShootingStar(ShootingStarLevel level, ShootingStarLocation location) {
        super(level.getObjectId(), 10, WorldObject.DEFAULT_ROTATION, location.getX(), location.getY(), location.getZ());
        this.location = location;
        this.level = level;
        this.stardust = level.getStardust();
        try {
            this.progressBarNpc = new NPC(10629, getLocation(), Direction.SOUTH, 0);
            this.progressBarNpc.spawn();
        } catch (Exception e) {
            log.error("Failed to spawn progress bar", e);
        }
        this.undiscovered = false;
    }

    public int getStardust() {
        return stardust;
    }

    public void onHarvest() {
        stardust--;

        progressBar.setPercentage(percentRemaining());
        try {
            progressBarNpc.getUpdateFlags().flag(UpdateFlag.HIT);
            progressBarNpc.getHitBars().add(progressBar);
        } catch (Exception e) {
            log.error("Failed to update progress bar", e);
        }


        if (stardust <= 0) {
            try {
                progressBarNpc.remove();
            } catch (Exception e) {
                log.error("Failed to remove progress bar", e);
            }
            World.removeObject(this);

            World.sendSoundEffect(getLocation(), new SoundEffect(4923, 10));

            if (level == ShootingStarLevel.ONE) {
                CharacterLoop.forEach(getLocation(), 5, Player.class, (player) -> {
                    player.incrementNumericAttribute("shooting_stars_mined", 1);
                    if (player.getActionManager().getAction() instanceof Mining) {
                        player.sendMessage("The star disintegrates into dust.");
                    }
                    if(Utils.random(750) == 0) {
                        Item ppx = new Item(ItemId.PRIMAL_PICKAXE);
                        player.getCollectionLog().add(ppx);
                        player.getInventory().addOrDrop(ppx);
                        WorldBroadcasts.sendMessage("<img=51><col=2980B9><shad=000000>" + player.getUsername() + " received a Primal Pickaxe from the shooting star!", LOTTERY, false);
                    }
                });
                WorldBroadcasts.sendMessage("<img=51><col=2980B9><shad=000000>The shooting star has been fully mined - keep an eye out for the next one!", LOTTERY, false);
                ShootingStars.setCurrent(null);
                return;
            }

            ShootingStarLevel next = level.getNextLevel();

            ShootingStar star = new ShootingStar(next, location);
            World.spawnObject(star);
            ShootingStars.setCurrent(star);
        }
    }

    public void remove() {
        try {
            progressBarNpc.remove();
        } catch (Exception e) {
            log.error("Failed to remove progress bar", e);
        }
        World.removeObject(this);
        CharacterLoop.forEach(getLocation(), 5, Player.class, (player) -> {
            if (player.getActionManager().getAction() instanceof Mining) {
                player.sendMessage("The star disintegrates into dust.");
            }
        });
    }

    public int percentRemaining() {
        return (int) (((double) stardust / level.getStardust()) * 100.0);
    }

    public int percentMined() {
        return 100 - percentRemaining();
    }

    public ShootingStarLevel getLevel() {
        return level;
    }

    public ShootingStarLocation getStarLocation() {
        return location;
    }

    public boolean isUndiscovered() {
        return undiscovered;
    }

    public void setUndiscovered(boolean undiscovered) {
        this.undiscovered = undiscovered;
    }
}
