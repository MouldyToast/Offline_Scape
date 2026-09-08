package org.jesse.game.content.scar_essence_mine.action;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.item.Item;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ItemChat;

import static org.jesse.game.world.entity.player.PlayerAttributesKt.getDepositedTaintedEssenceChunks;
import static org.jesse.game.item.ids.ItemId.*;

public class ExtractOrificeAction extends Action {
    private final Player player;
    private final Item item;
    private final int amount;

    private static final int COST_WARPED_EXTRACT = 1250;
    private static final int COST_TWISTED_EXTRACT = 6000;
    private static final int COST_MANGLED_EXTRACT = 12000;
    private static final int COST_SCARRED_EXTRACT = 24000;

    private static final Item COINS_PLACEHOLDER = new Item(1000, 1);
    private static final Item TAINTED_ESSENCE_PLACEHOLDER = new Item(28591, 1);

    public ExtractOrificeAction(Player player, Item item, int amount) {
        this.player = player;
        this.item = item;
        this.amount = amount;
    }

    @Override
    public boolean start() {
        return checkRequirements();
    }

    @Override
    public boolean process() {
        return checkRequirements();
    }

    @Override
    public int processWithDelay() {
        performExtract();
        return -1;  // End the action after processing the extract
    }

    private boolean checkRequirements() {
        int costPerExtract;

        switch (item.getId()) {
            case WARPED_EXTRACT:
                costPerExtract = COST_WARPED_EXTRACT;
                break;
            case TWISTED_EXTRACT:
                costPerExtract = COST_TWISTED_EXTRACT;
                break;
            case MANGLED_EXTRACT:
                costPerExtract = COST_MANGLED_EXTRACT;
                break;
            case SCARRED_EXTRACT:
                if (PlayerAttributesKt.getTotalWrathToHagus(player) < 1000) {
                    player.getDialogueManager().start(new ItemChat(player, TAINTED_ESSENCE_PLACEHOLDER, "You need to give Hagus at least 1000 wrath runes to create a Scarred Extract."));
                    return false;
                }
                costPerExtract = COST_SCARRED_EXTRACT;
                break;
            default:
                return false;
        }

        var depositedTaintedEssenceChunks = getDepositedTaintedEssenceChunks(player);
        if (depositedTaintedEssenceChunks < amount) {
            player.getDialogueManager().start(new ItemChat(player, TAINTED_ESSENCE_PLACEHOLDER, "You need at least " + amount + " tainted essence chunk(s) to create " + amount + " extract(s)."));
            return false;
        }

        var scarEssenceMineCoffer = PlayerAttributesKt.getScarEssenceMineCoffer(player);
        int totalCost = costPerExtract * amount;
        if (scarEssenceMineCoffer < totalCost) {
            player.getDialogueManager().start(new ItemChat(player, COINS_PLACEHOLDER, "You need at least " + totalCost + " coins in your scar essence mine coffer to create " + amount + " extract(s)."));
            return false;
        }

        return true;
    }

    private void performExtract() {
        int costPerExtract = switch (item.getId()) {
            case WARPED_EXTRACT -> COST_WARPED_EXTRACT;
            case TWISTED_EXTRACT -> COST_TWISTED_EXTRACT;
            case MANGLED_EXTRACT -> COST_MANGLED_EXTRACT;
            case SCARRED_EXTRACT -> COST_SCARRED_EXTRACT;
            default -> throw new IllegalStateException("Unexpected value: " + item.getId());
        };


        var currentEssence = PlayerAttributesKt.getDepositedTaintedEssenceChunks(player);
        var currentCoffer = PlayerAttributesKt.getScarEssenceMineCoffer(player);

        int totalEssenceUsed = amount;
        int totalCost = costPerExtract * totalEssenceUsed;
        var cofferAmount = PlayerAttributesKt.getScarEssenceMineCoffer(player);
        int currentTaintedEssence = getDepositedTaintedEssenceChunks(player);

        PlayerAttributesKt.setDepositedTaintedEssenceChunks(player, currentEssence - totalEssenceUsed);
        PlayerAttributesKt.setScarEssenceMineCoffer(player, currentCoffer - totalCost);
        var extraction = new Item(item.getId(), amount);
        player.getInventory().addItem(extraction);
        player.getDialogueManager().start(new ItemChat(player, extraction, "You commune with the Ventriculus and receive " + amount + " " + item.getDefinitions().getName() + "(s)."));
        player.sendMessage("You have " + Colour.RS_RED.wrap(Utils.format(currentTaintedEssence)) + " tainted essence remaining within the Ventriculus.");
        player.sendMessage("Your coffer contains " + Colour.RS_RED.wrap(Utils.format(cofferAmount)) + " coins within it.");
    }
}
