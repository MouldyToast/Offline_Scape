package org.jesse.game.content.boss.giantmole;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Position;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.impl.GiantMoleInstanced;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.region.DynamicArea;
import org.jesse.game.world.region.area.plugins.DeathPlugin;
import org.jesse.game.world.region.area.plugins.EntityAttackPlugin;
import org.jesse.game.world.region.area.plugins.LogoutPlugin;
import org.jesse.game.world.region.area.plugins.LootBroadcastPlugin;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.logger.NearRealityPrintStream;
import org.jetbrains.annotations.NotNull;

public class GiantMoleInstance extends DynamicArea implements EntityAttackPlugin, DeathPlugin, LogoutPlugin, LootBroadcastPlugin {

    public static final Location OUTSIDE_TILE = new Location(3005, 3380, 0);
    public static final Location INSIDE_TILE = new Location(1759, 5186, 0);
    private final Player player;

    public GiantMoleInstance(final Player player, final AllocatedArea allocatedArea, final int copiedChunkX, final int copiedChunkY) {
        super(allocatedArea, copiedChunkX, copiedChunkY);
        this.player = player;
    }

    @Override
    public void constructed() {
        player.setLocation(getLocation(INSIDE_TILE));
        new GiantMoleInstanced(NpcId.GIANT_MOLE, getLocation(INSIDE_TILE), Direction.SOUTH, 64, this).spawn();
    }

    @Override
    public void enter(Player player) {
    }

    @Override
    public void leave(Player player, boolean logout) {
        if (logout) {
            player.forceLocation(OUTSIDE_TILE);
        }
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(OUTSIDE_TILE);
    }

    @Override
    public Location onLoginLocation() {
        return OUTSIDE_TILE;
    }

    @Override
    public String name() {
        return "'Falador Mole Lair";
    }

    @Override
    public boolean attack(Player player, Entity entity, PlayerCombat combat) {
        return true;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    private static final int COST = 500_000;

    /**
     * Entry point from the vanilla spade dig on the Falador Park mole hills.
     */
    public static void enterDialogue(final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Would you like to enter the public or private instance?",
                        new DialogueOption("Public", () -> player.teleport(new Location(1752, 5235, 0))),
                        new DialogueOption("Private (500k)", () -> startPrivateDialogue(player))
                );
            }
        });
    }

    private static void startPrivateDialogue(final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Would you like to create a personal instance for 500,000 GP?",
                        new DialogueOption("Yes", () -> {
                            final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                            final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                            if ((long) amountInBank + amountInInventory >= COST) {
                                player.lock(1);
                                player.getInventory().deleteItem(new Item(ItemId.COINS_995, COST)).onFailure(remainder -> player.getBank().remove(remainder));
                                player.sendMessage("Please wait a few moments as your instance is being constructed.");
                                try {
                                    final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(8, 16);
                                    final GiantMoleInstance instance = new GiantMoleInstance(player, allocatedArea, (6992 >> 8) << 3, (6992 & 0xFF) << 3);
                                    instance.constructRegion();
                                    player.setLocation(instance.getLocation(INSIDE_TILE));
                                } catch (OutOfSpaceException e) {
                                    e.printStackTrace(NearRealityPrintStream.getErrorStream());
                                }
                                return;
                            }
                            setKey(50);
                        }), new DialogueOption("No."));
                plain(50, "You don't have enough coins with you or in your bank.");
            }
        });
    }


}
