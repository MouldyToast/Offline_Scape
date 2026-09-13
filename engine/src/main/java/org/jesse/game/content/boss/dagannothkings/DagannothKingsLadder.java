package org.jesse.game.content.boss.dagannothkings;

import org.jesse.game.content.clans.ClanChannel;
import org.jesse.game.content.clans.ClanManager;
import org.jesse.game.content.clans.ClanRank;
import org.jesse.game.content.slayer.Assignment;
import org.jesse.game.content.slayer.BossTask;
import org.jesse.game.content.slayer.RegularTask;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.GlobalAreaManager;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.game.world.region.dynamicregion.OutOfSpaceException;
import org.jesse.logger.NearRealityLogger;
import org.jesse.plugins.dialogue.PlainChat;
import mgi.utilities.StringFormatUtil;
import org.slf4j.Logger;

import java.util.Optional;

import static org.jesse.game.content.boss.dagannothkings.DagannothKingsInstanceConstants.entranceCoordinates;

public class DagannothKingsLadder implements ObjectAction {

    private static final Logger log = NearRealityLogger.getLogger(DagannothKingsLadder.class);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (option) {
        case "Standard": 
            player.useStairs(828, new Location(2900, 4449, 0), 1, 2);
            break;
        case "Slayer": 
            final Assignment assignment = player.getSlayer().getAssignment();
            if (assignment == null || (assignment.getTask() != RegularTask.DAGANNOTH && assignment.getTask() != BossTask.DAGANNOTH_KINGS)) {
                player.getDialogueManager().start(new PlainChat(player, "You need to be on a dagannoths slayer task to access the slayer-only dungeon."));
                return;
            }
            player.useStairs(828, new Location(2899, 4385, 0), 1, 2);
            break;
        case "Peek": 
            player.sendMessage("You peek through the crack...");
            WorldTasksManager.schedule(() -> {
                final int playerCount = GlobalAreaManager.get("Dagannoth Kings Lair").getPlayers().size();
                final int slayerPlayerCount = GlobalAreaManager.get("Dagannoth Kings Slayer-Only Lair").getPlayers().size();
                player.sendMessage("Standard cave: " + (playerCount == 0 ? "No adventurers." : playerCount + (playerCount == 1 ? " adventurer." : " adventurers.")));
                player.sendMessage("Slayer cave: " + (slayerPlayerCount == 0 ? "No adventurers." : slayerPlayerCount + (slayerPlayerCount == 1 ? " adventurer." : " adventurers.")));
            }, 2);
            break;
        case "Private": 
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Which instance would you like to enter/create?", "Clan", "Personal")
                            .onOptionOne(() -> handleSelection(player, true))
                            .onOptionTwo(() -> handleSelection(player, false));
                }
            });
            break;
        }
    }

    private void handleSelection(Player player, boolean isClan) {
        ClanChannel channel;
        if(isClan) {
            channel = player.getSettings().getChannel();
            if (channel == null) {
                player.sendMessage("You need to be in a clan chat channel to start or join an instance.");
                return;
            }
        } else {
            channel = null;
        }

        if(isClan) {
            final Optional<DagannothKingInstance> instance = DagannothKingsInstanceManager.getManager().findInstance(player);
            if (instance.isPresent() && !instance.get().solo) {
                player.lock(1);
                player.setLocation(instance.get().getLocation(entranceCoordinates));
                return;
            }
            final ClanRank rank = ClanManager.getRank(player, channel);
            if (rank.getId() < channel.getKickRank().getId()) {
                player.sendMessage("Clan members ranked as " + StringFormatUtil.formatString(channel.getKickRank().toString()) + " or above can only start a clan instance.");
                return;
            }

            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Pay " + StringFormatUtil.format(DagannothKingsInstanceConstants.COST) + " to start an instance for your clan?");
                    options(new DialogueOption("Yes.", () -> {
                        final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                        final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                        if ((long) amountInBank + amountInInventory >= DagannothKingsInstanceConstants.COST) {
                            if (DagannothKingsInstanceManager.getManager().findInstance(player).isPresent()) {
                                setKey(75);
                                return;
                            }
                            player.lock(1);
                            player.getInventory().deleteItem(new Item(ItemId.COINS_995, DagannothKingsInstanceConstants.COST)).onFailure(remainder -> player.getBank().remove(remainder));
                            try {
                                final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(6, 6);
                                final DagannothKingInstance area = new DagannothKingInstance(channel.getOwner(), allocatedArea, 361, 553, false);
                                area.constructRegion();
                                player.setLocation(area.getLocation(entranceCoordinates));
                            } catch (OutOfSpaceException e) {
                                log.error("", e);
                            }
                            return;
                        }
                        setKey(50);
                    }), new DialogueOption("No."));
                    plain(50, "You don't have enough coins with you or in your bank.");
                    plain(75, "Someone in your clan has already initiated an instance.");
                }
            });
        } else {
            final Optional<DagannothKingInstance> instance = DagannothKingsInstanceManager.getManager().findInstanceSolo(player);
            if (instance.isPresent()) {
                player.lock(1);
                player.setLocation(instance.get().getLocation(entranceCoordinates));
                return;
            }
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    plain("Pay " + StringFormatUtil.format(DagannothKingsInstanceConstants.SOLO_COST) + " to start an instance for yourself?");
                    options(new DialogueOption("Yes.", () -> {
                        final int amountInInventory = player.getInventory().getAmountOf(ItemId.COINS_995);
                        final int amountInBank = player.getBank().getAmountOf(ItemId.COINS_995);
                        if ((long) amountInBank + amountInInventory >= DagannothKingsInstanceConstants.SOLO_COST) {
                            if (DagannothKingsInstanceManager.getManager().findInstanceSolo(player).isPresent()) {
                                setKey(75);
                                return;
                            }
                            player.lock(1);
                            player.getInventory().deleteItem(new Item(ItemId.COINS_995, DagannothKingsInstanceConstants.SOLO_COST)).onFailure(remainder -> player.getBank().remove(remainder));
                            try {
                                final AllocatedArea allocatedArea = MapBuilder.findEmptyChunk(6, 6);
                                final DagannothKingInstance area = new DagannothKingInstance(player.getUsername(), allocatedArea, 361, 553, true);
                                area.constructRegion();
                                player.setLocation(area.getLocation(entranceCoordinates));
                            } catch (OutOfSpaceException e) {
                                log.error("", e);
                            }
                            return;
                        }
                        setKey(50);
                    }), new DialogueOption("No."));
                    plain(50, "You don't have enough coins with you or in your bank.");
                    plain(75, "You already have an instance open? Contact a developer.");
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {3831};
    }
}
