package org.jesse.game.content.breaches.entity;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.content.breaches.BreachLoot;
import org.jesse.game.content.breaches.BreachManager;
import org.jesse.game.content.breaches.BreachSettings;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.broadcasts.BroadcastType;
import org.jesse.game.world.broadcasts.WorldBroadcasts;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combatdefs.AggressionType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Objects;

public class BreachEntity extends NPC {

    private final BreachManager breachManager;
    private final WorldObject breachPortal;
    private final HashMap<String, Integer> damageMap;

    /*
    Remove entity upon death
    Spawn another random boss to replace
    Send loot to 15 killers
    Remove breach portal object
     */

    @Override
    protected void onDeath(Entity source) {
        if (Objects.isNull(BreachManager.getInstance())) {
            super.onDeath(source);
            return;
        }
        breachManager.getBreachEntities().remove(this); // remove this entity from the entities list
        super.onDeath(source);
        breachManager.spawnRandomBoss(30);
        BreachLoot.generateLoot(this, (Player) source);
        // If there are only X left, then announce that the portal will close soon
        var bossesRemaining = BreachSettings.BOSS_QUANTITY - breachManager.getSpawnedBosses();
        if (Objects.nonNull(BreachManager.getInstance()) && bossesRemaining <= 6) {
            WorldBroadcasts.broadcast(
                null,
                BroadcastType.BREACHES, "The breach at %s is coming to a finish! The portal will soon close, for now..."
                .formatted(
                    BreachManager.getInstance().getBreachLocation().broadcastLocation));
        }
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        if (hit.getSource() instanceof Player player) {
            if (!PlayerAttributesKt.getBlackSkulled(player) || !player.getVariables().isSkulled()) {
                PlayerAttributesKt.setBlackSkulled(player, true);
                player.getVariables().setSkull(true);
            }
        }
    }

    /*
    Adds damage credit for attacker
     */

    @Override
    public void addReceivedDamage(Entity source, int amount, HitType type) {
        super.addReceivedDamage(source, amount, type);
        if(source instanceof Player player) {
            Integer damage = damageMap.get(player.getUsername());
            if(damage == null) {
                damageMap.put(player.getUsername(), amount);
            } else {
                damage += amount;
                damageMap.put(player.getUsername(), damage);
            }
        }
    }

    /*
    Remove portal upon finish
     */

    @Override
    public void finish() {
        super.finish();
        World.removeObject(breachPortal);
    }

    public BreachEntity(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius, true);
        this.breachManager = BreachManager.getInstance();
        this.damageMap = new HashMap<>();
        this.breachPortal = new WorldObject(
            BreachSettings.BREACH_SPAWN_BOSS_PORTAL_OBJECT_ID,
            WorldObject.DEFAULT_TYPE,
            WorldObject.DEFAULT_ROTATION,
            (tile == null) ? new Location(0, 0) : tile);
    }

    public HashMap<String, Integer> getDamageMap() {
        return damageMap;
    }

    @Override
    public int getRadius() {
        return BreachSettings.BOSS_RADIUS;
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    @Override
    public boolean checkAggressivity() {
        return true;
    }

    @Override
    public int getAggressionDistance() {
        return 25;
    }

    @Override
    protected boolean superCheckAggressivity() {
        return true;
    }

    @Override
    public boolean isForceAggressive() {
        return true;
    }

    @Override
    public NPC spawn() {
        this.combatDefinitions.setAggressionType(AggressionType.ALWAYS_AGGRESSIVE);
        return super.spawn();
    }

    @Override
    public void processNPC() {
        if (!isUnderCombat()) {
            var targets = getPossibleTargets(EntityType.PLAYER, getAggressionDistance());
            if (!targets.isEmpty())
                setTarget(Utils.random(targets));
        }
        super.processNPC();
    }

    @Override
    protected void sendNotifications(Player player) {}
}
