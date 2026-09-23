package org.jesse.game.content.ZemouregalsFort;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Zei | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Z
 * @social Github: <a href="https://github.com/Zeighe">Zeighe</a>
 * @since 2/8/2025
 */
public class ZombieAxeD extends Dialogue {

    private static final Item BROKEN_AXE_PLACEHOLDER = new Item(28813, 1);

    public ZombieAxeD(final Player player) {super(player); }

    @Override
    public void buildDialogue() {
        if (player.getSkills().getLevel(SkillConstants.SMITHING) < 70) {
            item(BROKEN_AXE_PLACEHOLDER, "The axe isn't in a great state, but it should be possible to repair it. However, you unfortunately don't feel you have the skills needed to do so yourself. To repair the axe, you'll need 70 Smithing.");
        } else {
            item(BROKEN_AXE_PLACEHOLDER, "The axe isn't in a great state, but it should be possible to repair it. You reckon you have the skills needed to do so yourself. You'll just need a hammer and an anvil.");
        }
    }
}