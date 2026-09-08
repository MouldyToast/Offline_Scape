package org.jesse.game.content.skills.cooking.actions;

import org.jesse.game.content.achievementdiary.diaries.*;
import org.jesse.game.content.skills.cooking.CookingDefinitions;
import org.jesse.game.content.skills.cooking.CookingDefinitions.CookingData;
import org.jesse.game.content.treasuretrails.clues.CharlieTask;
import org.jesse.game.content.treasuretrails.clues.SherlockTask;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.SkillcapePerk;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import mgi.types.config.items.ItemDefinitions;

// TODO: Cooking messages differ per fish on OSRS!
public class Cooking extends Action {

    private final CookingData data;

    private final WorldObject object;

    private int amount;

    private int completed;

    public Cooking(final CookingData data, final int amount, final WorldObject object) {
        this.data = data;
        this.amount = amount;
        this.object = object;
    }

    @Override
    protected void onInterruption() {
    }

    @Override
    public boolean initiateOnPacketReceive() {
        return data == CookingData.KARAMBWAN || data == CookingData.POISON_KARAMBWAN;
    }

    @Override
    public boolean start() {
        return check();
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        player.setAnimation(object.getName().toLowerCase().contains("fire") ? CookingDefinitions.FIRE : CookingDefinitions.STOVE);
        player.getInventory().deleteItem(data.getRaw(), 1);
        if (isBurning()) {
            if (data.isRaidsFood()) {
                World.spawnFloorItem(new Item(data.getBurnt()), new Location(player.getLocation()), player, -1, Integer.MAX_VALUE);
            } else {
                player.getInventory().addItem(data.getBurnt(), 1);
            }
            if (data == CookingData.CAKE) {
                // Give cake tin back to the player.
                player.getInventory().addOrDrop(new Item(1887));
            }
            player.sendFilteredMessage("You accidentally burn the " + ItemDefinitions.get(data.getCooked()).getName().toLowerCase() + ".");
        } else {
            final String message = data.getMessage().equals("") ? "You successfully cook the " + ItemDefinitions.get(data.getCooked()).getName().toLowerCase() + "." : data.getMessage();
            if (data.equals(CookingData.WILD_PIE)) {
                if (object.getId() == ObjectId.CLAY_OVEN) {
                    player.getAchievementDiaries().update(DesertDiary.BAKE_WILD_PIE);
                }
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_WILD_PIES);
            } else if (data.equals(CookingData.BREAD)) {
                player.getAchievementDiaries().update(LumbridgeDiary.BAKE_BREAD);
            } else if (data.equals(CookingData.KARAMBWAN)) {
                player.getAchievementDiaries().update(KaramjaDiary.COOK_A_KARAMBWAN);
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_KARAMBWAN);
            } else if (data.equals(CookingData.DARK_CRAB)) {
                player.getAchievementDiaries().update(WildernessDiary.FISH_AND_COOK_DARK_CRAB, 2);
            } else if (data.equals(CookingData.BASS)) {
                player.getAchievementDiaries().update(KandarinDiary.CATCH_AND_COOK_BASS, 2);
            } else if (data.equals(CookingData.SUMMER_PIE)) {
                player.getAchievementDiaries().update(VarrockDiary.BAKE_A_SUMMER_PIE);
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_SUMMER_PIES);
            } else if (data.equals(CookingData.SWORDFISH)) {
                SherlockTask.COOK_A_SWORDFISH.progress(player);
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_SWORDFISH);
            } else if (data.equals(CookingData.TROUT)) {
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_TROUT);
            } else if (data.equals(CookingData.SHARK)) {
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_SHARKS);
            } else if (data.equals(CookingData.MONKFISH)) {
                player.getDailyChallengeManager().update(SkillingChallenge.COOK_MONKFISH);
            } else if (data.equals(CookingData.ANGLERFISH)) {
                player.getAchievementDiaries().update(KourendDiary.CATCH_ANGLERFISH, 2);
            }
            if (data.equals(CookingData.TROUT)) {
                CharlieTask.COOK_A_TROUT.progress(player);
            } else if (data.equals(CookingData.PIKE)) {
                CharlieTask.COOK_A_PIKE.progress(player);
            }
            final Item item = new Item(data.getCooked(), 1);
            if (data.equals(CookingData.PSYKK_BAT)) {
                item.setAttribute("cooker", player.getUsername());
            }
            player.getInventory().addItem(item);
            player.getSkills().addXp(SkillConstants.COOKING, data.getXp());
            player.sendFilteredMessage(message);
        }
        amount--;
        return completed++ == 0 && initiateOnPacketReceive() ? 5 : 3;
    }

    @Override
    public void stop() {
    }

    private boolean check() {
        return (!player.isDead() && !player.isFinished() && CookingData.hasRequirements(player, data) && amount > 0) && World.getRegion(object.getRegionId()).containsObject(object.getId(), object.getType(), object);
    }

    private boolean isBurning() {
        if (SkillcapePerk.COOKING.isEffective(player)) {
            return false;
        }
        final boolean hasGauntlets = player.getEquipment().getId(EquipmentSlot.HANDS) == 775;
        final int burnLevel = data.getBurnLevel();
        // TODO: "Level at which stops burning" differs per food per circumstance.
        final int burnChance = burnLevel - (hasGauntlets ? 5 : 0) - (object.getId() == 21302 ? 5 : 0) - (player.getSkills().getLevel(SkillConstants.COOKING));
        return Utils.random(100) < Math.min(burnChance, 34);
    }
}
