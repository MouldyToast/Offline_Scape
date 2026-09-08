package org.jesse.game.world.flooritem;

import org.jesse.game.item.Item;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.InteractableEntity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 11. march 2018 : 22:55.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class FloorItem extends Item implements InteractableEntity {

	protected final Location location;
	protected transient String ownerName;
	protected transient String receiverName;
	transient protected int invisibleTicks, visibleTicks;
	private transient boolean visibleToIronmenOnly;
    private transient boolean visibleToIronmen;
	
	public FloorItem(final Item item, final Location location, final Player owner,
                     final Player receiver, final int invisibleTicks, final int visibleTicks) {
		super(item.getId(), item.getAmount(), item.getAttributesCopy());
		this.location = new Location(location);
		if (owner != null) {
			ownerName = owner.getUsername();
		}
		if (receiver != null) {
		    receiverName = receiver.getUsername();
        }
		this.invisibleTicks = invisibleTicks;
		this.visibleTicks = visibleTicks;
	}
	
	public final boolean isVisibleTo(final Player player) {
		if (invisibleTicks <= 0) {
			return true;
		}
		if (receiverName == null) {
			if (visibleToIronmenOnly) {
				return player != null && player.isIronman();
			}
			return true;
		}
		if (player == null) {
			return false;
		}
		return receiverName.equals(player.getUsername());
	}

    public boolean isReceiver(final Player player) {
        if (receiverName == null || player == null) {
            return false;
        }
        return receiverName.equals(player.getUsername());
    }
	
	public boolean isOwner(final Player player) {
		if (ownerName == null || player == null) {
			return false;
		}
		return ownerName.equals(player.getUsername());
	}

	public boolean hasOwner() {
		return ownerName != null;
	}
	
	@Override
	public String toString() {
		return getName() + " [id: " + getId() + ", amount: " + getAmount() + "]" + " - " + location.toString();
	}

	public boolean isPresent() {
        return World.getRegion(location.getRegionId(), true).getFloorItem(this.getId(), location) != null;
    }
	public Location getLocation() {
	    return location;
	}
	
	public String getOwnerName() {
	    return ownerName;
	}
	
	public String getReceiverName() {
	    return receiverName;
	}
	
	public int getInvisibleTicks() {
	    return invisibleTicks;
	}
	
	public int getVisibleTicks() {
	    return visibleTicks;
	}
	
	public void setInvisibleTicks(int invisibleTicks) {
	    this.invisibleTicks = invisibleTicks;
	}
	
	public void setVisibleTicks(int visibleTicks) {
	    this.visibleTicks = visibleTicks;
	}
	
	public boolean isVisibleToIronmenOnly() {
	    return visibleToIronmenOnly;
	}
	
	public void setVisibleToIronmenOnly(boolean visibleToIronmenOnly) {
	    this.visibleToIronmenOnly = visibleToIronmenOnly;
	}

    public boolean isVisibleToIronmen() {
        return visibleToIronmen;
    }

    public void setVisibleToIronmen(boolean visibleToIronmen) {
        this.visibleToIronmen = visibleToIronmen;
    }

    @Override
	public int getWidth() {
		return 1;
	}

	@Override
	public int getLength() {
		return 1;
	}
}
