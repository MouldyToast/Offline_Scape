package org.jesse.game.content.event.easter2020.plugin.npc;

import org.jesse.game.content.event.easter2020.EasterConstants;
import org.jesse.game.content.event.easter2020.SplittingHeirs;
import org.jesse.game.content.event.easter2020.Stage;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.SkipPluginScan;
import org.jesse.plugins.dialogue.PlayerChat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Corey
 * @since 06/04/2020
 */
@SkipPluginScan
public class EasterImpling extends NPCPlugin {

    private static final Animation catchAnimation = new Animation(6606);

    @Override
    public void handle() {
        bind("Catch", ((player, npc) -> {
            if (!SplittingHeirs.progressedAtLeast(player, Stage.GATHER_IMPLINGS)) {
                player.getDialogueManager().start(new PlayerChat(player, "I should speak with the Easter Bunny Jr first and see what he wants me to do."));
                return;
            }
            if (!player.carryingItem(EasterConstants.EasterItem.IMPLING_NET.getItemId())) {
                player.getDialogueManager().start(new PlayerChat(player, "I should probably try and find something to catch these with."));
                return;
            } else if (!player.getEquipment().containsItem(EasterConstants.EasterItem.IMPLING_NET.getItemId())) {
                player.sendMessage("You need to equip the net to catch implings.");
                return;
            }
            if (!player.carryingItem(EasterConstants.EasterItem.IMPLING_JAR.getItemId())) {
                player.sendMessage("You need an impling jar to catch these. Try looking around the Easter Bunny's " + "office.");
                return;
            }
            player.lock(3);
            final EasterImpling.Npc impling = Npc.forNpcId.get(npc.getId());
            player.setAnimation(catchAnimation);
            player.sendMessage("You manage to catch the impling and squeeze it into a jar.");
            player.getInventory().deleteItem(new Item(EasterConstants.EasterItem.IMPLING_JAR.getItemId()));
            player.getInventory().addItem(new Item(EasterConstants.EasterItem.EASTER_IMPLING_JAR.getItemId()));
            WorldTasksManager.schedule(() -> player.getVarManager().sendBit(impling.getVarbitId(), 1), 1);
        }));
    }

    @Override
    public int[] getNPCs() {
        // base npc
        return new int[] { NpcId.EASTER_IMPLING };
    }

    /**
     * @author Corey
     * @since 31/03/2020
     */
    public enum Npc {

        FIRST(15206, 15056, new Location(2220, 4377)), SECOND(15207, 15057, new Location(2231, 4386)), THIRD(15208, 15058, new Location(2223, 4388)), FOURTH(15209, 15059, new Location(2233, 4366)), FIFTH(15210, 15060, new Location(2219, 4360));

        public static final Int2ObjectOpenHashMap<Npc> forNpcId;

        public static final Npc[] values = Npc.values();

        static {
            CollectionUtils.populateMap(values, forNpcId = new Int2ObjectOpenHashMap<>(values.length), Npc::getNpcId);
        }

        private final int npcId;

        private final int varbitId;

        private final Location location;

        Npc(int npcId, int varbitId, Location location) {
            this.npcId = npcId;
            this.varbitId = varbitId;
            this.location = location;
        }

        public int getNpcId() {
            return npcId;
        }

        public int getVarbitId() {
            return varbitId;
        }

        public Location getLocation() {
            return location;
        }
    }
}
