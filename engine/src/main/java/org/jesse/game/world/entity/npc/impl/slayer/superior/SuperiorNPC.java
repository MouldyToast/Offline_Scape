package org.jesse.game.world.entity.npc.impl.slayer.superior;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.item.Item;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.NPCCombat;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import org.jesse.game.world.entity.npc.drop.matrix.NPCDrops;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.jesse.game.item.ids.ItemId.*;

/**
 * @author Kris | 27/05/2019 23:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SuperiorNPC extends NPC {
    private final NPC root;
    private final Player owner;
    private int ticks;

    public SuperiorNPC(@NotNull final Player owner, @NotNull final NPC root, final int id, final Location tile) {
        super(id, tile, Direction.SOUTH, 5);
        this.root = root;
        this.owner = owner;
        this.spawned = true;
        this.setForceAttackable(true);
        this.combat = new NPCCombat(this) {
            @Override
            protected boolean attackable(Entity target, TargetSwitchCause cause, boolean debug) {
                return true;
            }
        };
    }

    @Override
    public NPC spawn() {
        owner.getTemporaryAttributes().put("superior monster", true);
        return super.spawn();
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        return entity == owner;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (isUnderCombat()) {
            ticks = 0;
        } else {
            if (owner.getLocation().withinDistance(getLocation(), 15)) {
                if (ticks++ == 200) {
                    finish();
                }
            } else if (ticks++ == 50) {
                finish();
            }
        }
    }

    @Override
    public void onFinish(final Entity source) {
        owner.getTemporaryAttributes().remove("superior monster");
        super.onFinish(source);
    }

    @Override
    public boolean canAttack(final Player source) {
        if (source != owner) {
            source.sendMessage("This is not your superior foe.");
            return false;
        }
        return super.canAttack(source);
    }

    private void executeOnDeathPlugins(final Player killer) {
        final List<DropProcessor> processors = DropProcessorLoader.get(getId());
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }
    }

    @Override
    public void drop(final Location tile) {
        final Player killer = getDropRecipient();
        if (killer == null) {
            return;
        }
        executeOnDeathPlugins(killer);
        final List<DropProcessor> processors = DropProcessorLoader.get(root.getId());
        final NPCDrops.DropTable drops = NPCDrops.getTable(root.getId());
        for (int i = 0; i < 3; i++) {
            root.onDrop(killer);
            if (processors != null) {
                for (final DropProcessor processor : processors) {
                    processor.onDeath(root, killer);
                }
            }
            if (drops == null) {
                continue;
            }
            final int index = i;
            NPCDrops.rollTable(killer, drops, drop -> {
                if (index != 0 && drop.isAlways()) {
                    return;
                }
                dropItem(killer, drop, tile);
            });
        }
        final int req = getCombatDefinitions().getSlayerLevel();
        double probability = 1.0F / (200.0F - (Math.pow(req + 55.0F, 2) / 125.0F));

        if (Utils.randomDouble() < probability) {
            final int roll = Utils.random(7);
            if (roll < 3) {
                dropItem(killer, new Item(DUST_BATTLESTAFF));
            } else if (roll < 6) {
                dropItem(killer, new Item(MIST_BATTLESTAFF));
            } else if (roll == 6) {
                dropItem(killer, new Item(IMBUED_HEART));
            } else {
                dropItem(killer, new Item(ETERNAL_GEM));
            }
        }
    }
}
