package org.jesse.game.world.entity.npc.impl.blastfurnace;

import org.jesse.game.content.minigame.blastfurnace.BlastFurnaceArea;
import org.jesse.game.content.minigame.blastfurnace.BlastFurnaceOre;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class ConveyerBeltOreNPC extends NPC {

    private final static Location END_TILE = new Location(1942, 4963, 0);

    private boolean ORE_DEPOSIT;

    private final Player player;
    private final Item ore;

    public ConveyerBeltOreNPC(final int id, final Location tile, final Direction facing, final int radius, final Player player, final Item ore) {
        super(id, tile, facing, radius);

        player.getBlastFurnace().addOre(BlastFurnaceOre.getOre(ore.getId()), ore.getAmount());

        this.player = player;
        this.ore = ore;
        this.ORE_DEPOSIT = false;

        if(ore == null) {
            finish();
        }
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void processNPC() {
        super.processNPC();
    
        if (player == null) {
            finish();
            return;
        }

        if(getY() == 4963 && !ORE_DEPOSIT) {
            ORE_DEPOSIT = true;
            setInvalidAnimation(BlastFurnaceArea.ORE_FALL);
            WorldTasksManager.schedule(() -> {
                player.getBlastFurnace().setOresOnBelt(player.getBlastFurnace().getOresOnBelt()-1);

                if(player.getBlastFurnace().getOresOnBelt() == 0)
                    player.getBlastFurnace().processBars();

                finish();
            }, 1);
        }

        if(BlastFurnaceArea.FURNACE_ACTIVE)
            addWalkSteps(END_TILE.getX(), END_TILE.getY(), -1, false);
        else
            resetWalkSteps();
    }

}
