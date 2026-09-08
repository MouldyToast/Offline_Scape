package org.jesse.game.content.skills.crafting.actions;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 23 dec. 2017 : 18:56:41
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class MoltenGlassCrafting extends Action {

	private static final Animation ANIMATION = Animation.SMITH;
	private static final double EXPERIENCE = 20;
	public static final Item MOLTEN_GLASS = new Item(1775);
	private static final Item SODA_ASH = new Item(1781);
	private static final Item BUCKET_OF_SAND = new Item(1783);

	private final int amount;
	private int cycle;

	@Override
	public boolean start() {
		return player.getInventory().containsItems(SODA_ASH, BUCKET_OF_SAND);
	}

    public MoltenGlassCrafting(int amount) {
        this.amount = amount;
    }

	@Override
    public boolean process() {
        if (!player.getInventory().containsItems(SODA_ASH, BUCKET_OF_SAND)) {
            return false;
        }
        return cycle < amount;
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(ANIMATION);
        player.getInventory().deleteItemsIfContains(new Item[]{SODA_ASH, BUCKET_OF_SAND}, () -> {
            player.getInventory().addItem(MOLTEN_GLASS);
            player.getSkills().addXp(SkillConstants.CRAFTING, EXPERIENCE);
            player.sendFilteredMessage("You heat the sand and soda ash in the furnace to make glass.");
        });
        cycle++;
        return 1;
    }

}
