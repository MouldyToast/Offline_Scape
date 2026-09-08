package org.jesse.game.content.flooritems;

import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.content.achievementdiary.diaries.FaladorDiary;
import org.jesse.game.item.Item;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.Flags;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import org.jesse.game.world.flooritem.FloorItem;
import org.jesse.game.world.region.CharacterLoop;
import org.jesse.plugins.flooritem.FloorItemPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WineOfZamorakPlugin implements FloorItemPlugin {
    private static final Location WINE_OF_ZAMORAK_LOCATION = new Location(2950, 3824, 0);
    private static final Location FALADOR_WINE_OF_ZAMORAK_LOCATION = new Location(2930, 3515, 0);
    private static final Location FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION = new Location(2938, 3517, 1);

    {
        World.getRegion(FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION.getRegionId(), true).setMask(1, (2938 & 63), (3517 & 63), Flags.OBJECT);
    }

    @Override
    public void telegrab(@NotNull final Player player, @NotNull final FloorItem item) {
        if (!canTelegrab(player, item)) {
            return;
        }
        World.destroyFloorItem(item);
        final Item addedItem = new Item(item);
        player.getAchievementDiaries().update(FaladorDiary.TELEGRAB_WINE_OF_ZAMORAK);
        if (WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) && DiaryUtil.eligibleFor(DiaryReward.WILDERNESS_SWORD3, player)) {
            addedItem.setId(246);
        } else if (FALADOR_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) || FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation())) {
            final List<NPC> monks = CharacterLoop.find(player.getLocation(), 10, NPC.class, npc -> !npc.isDead() && (npc.getId() == 8400 || npc.getId() == 8401));
            if (!monks.isEmpty()) {
                if (!FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) || !EquipmentUtils.wearingZamorakRobes(player)) {
                    monks.forEach(monk -> monk.getCombat().setTarget(player));
                    damage(player, true);
                }
            }
        }
        player.getInventory().addItem(addedItem).onFailure(it -> World.spawnFloorItem(it, player, 100, 200));
    }

    @Override
    public void handle(final Player player, final FloorItem item, final int optionId, final String option) {
        if (option.equals("Take")) {
            if (WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) || FALADOR_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) || FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation())) {
                if (FALADOR_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation()) || FALADOR_UPSTAIRS_WINE_OF_ZAMORAK_LOCATION.matches(item.getLocation())) {
                    final List<NPC> monks = CharacterLoop.find(player.getLocation(), 10, NPC.class, npc -> !npc.isDead() && (npc.getId() == 8400 || npc.getId() == 8401));
                    if (monks.isEmpty()) {
                        World.takeFloorItem(player, item);
                        damage(player, false);
                        return;
                    }
                    monks.forEach(monk -> monk.getCombat().setTarget(player));
                }
                damage(player, true);
                return;
            }
            World.takeFloorItem(player, item);
        }
    }


    @Override
    public boolean overrideTake() {
        return true;
    }

    private static final int[] skills = new int[] {SkillConstants.ATTACK, SkillConstants.STRENGTH, SkillConstants.DEFENCE, SkillConstants.RANGED, SkillConstants.MAGIC};

    private void damage(final Player player, final boolean message) {
        player.setGraphics(CombatSpell.FLAMES_OF_ZAMORAK.getHitGfx());
        if (message) {
            player.sendMessage("STOP STEALING MY WINE! GAH!");
        }
        WorldTasksManager.schedule(() -> {
            for (final int skill : skills) {
                player.drainSkill(skill, 4);
            }
            final int hitpoints = player.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            final int damage = 1 + (hitpoints / 20);
            player.applyHit(new Hit(damage, HitType.DEFAULT));
        });
    }

    @Override
    public int[] getItems() {
        return new int[] { 245 };
    }
}

