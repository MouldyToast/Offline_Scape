package com.zenyte.game.content.skills.mining;

import com.near_reality.game.content.crystal.TrahaearnMineRocks;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.skills.mining.PickAxeDefinition;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.ObjectId;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.*;

/**
 * @author Noele | Nov 9, 2017 : 12:22:34 AM
 * @see <a href="https://noeles.life">|| noele@zenyte.com</a>
 */
public class MiningDefinitions {
    public static final Int2ObjectOpenHashMap<OreDefinitions> ores = new Int2ObjectOpenHashMap<>();
    public static final Int2ObjectOpenHashMap<PickAxeDefinition> tools = new Int2ObjectOpenHashMap<>();
    public static final Int2IntOpenHashMap rocks = new Int2IntOpenHashMap();
    public static final Int2ObjectOpenHashMap<PaydirtDefinitions> paydirts = new Int2ObjectOpenHashMap<>();

    public static void load() {
        for (OreDefinitions entry : OreDefinitions.values())
            for (int id : entry.getRocks())
                ores.put(id, entry);
        for (ShapeDefinitions entry : ShapeDefinitions.values())
            for (int id : entry.getOres())
                rocks.put(id, entry.getEmpty());
        for (PickaxeDefinitions entry : PickaxeDefinitions.values())
            tools.put(entry.getId(), entry);
        tools.put(CrystalTool.Pickaxe.INSTANCE.getProductItemId(), CrystalTool.Pickaxe.INSTANCE);
        for (final PaydirtDefinitions value : PaydirtDefinitions.values())
            paydirts.put(value.getOre(), value);
    }




    public enum PaydirtDefinitions {
        COAL(453, 30, 0),
        GOLD(444, 40, 15),
        MITHRIL(447, 55, 30),
        ADAMANTITE(449, 70, 45),
        RUNITE(451, 85, 75);
        public static final Map<Integer, PaydirtDefinitions> entries = new HashMap<>();
        private final int ore;
        private final int level;
        private final int xp;

        PaydirtDefinitions(final int ore, final int level, final int xp) {
            this.ore = ore;
            this.level = level;
            this.xp = xp;
        }

        public int getOre() {
            return ore;
        }

        public int getLevel() {
            return level;
        }

        public int getXp() {
            return xp;
        }
    }


    public enum PickaxeDefinitions implements PickAxeDefinition {
        BRONZE(1265, 1, new Animation(625), new Animation(6753), 6),
        IRON(1267, 1, new Animation(626), new Animation(6754), 6),
        STEEL(1269, 6, new Animation(627), new Animation(6755), 5),
        BLACK(12297, 11, new Animation(3873), new Animation(6109), 5),
        MITHRIL(1273, 21, new Animation(629), new Animation(6757), 4),
        ADAMANT(1271, 31, new Animation(628), new Animation(6756), 3),
        RUNE(1275, 41, new Animation(624), new Animation(6752), 2),
        GILDED(23276, 41, new Animation(8313), new Animation(8312), 2),
        DRAGON(11920, 61, new Animation(7139), new Animation(6758), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        DRAGON_OR(12797, 61, new Animation(643), new Animation(335), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        THIRD_AGE(20014, 61, new Animation(7283), new Animation(7282), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        INFERNAL(13243, 61, new Animation(4482), new Animation(4481), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        UNCHARGED_INFERNAL(13244, 61, new Animation(4482), new Animation(4481), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        INACTIVE_CRYSTAL(
                CrystalTool.Pickaxe.INSTANCE.getInactiveId(),
                CrystalTool.Pickaxe.INSTANCE.getLevel(),
                CrystalTool.Pickaxe.INSTANCE.getAnim(),
                CrystalTool.Pickaxe.INSTANCE.getAlternateAnimation(),
                -1
        ) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        },
        ECHO_PICKAXE(ItemId.ECHO_PICKAXE, 1, new Animation(8788), new Animation(8786), -1) {
            @Override
            public int getMineTime() {
                return Utils.random(1, 2);
            }
        };

        public static final PickaxeDefinitions[] VALUES = values();
        private final int mineTime;
        private final int id;
        private final int level;
        private final Animation anim;
        private final Animation alternateAnimation;

        PickaxeDefinitions(int id, int level, Animation anim, Animation alternateAnimation, int mineTime) {
            this.id = id;
            this.level = level;
            this.anim = anim;
            this.alternateAnimation = alternateAnimation;
            this.mineTime = mineTime;
        }

        public static PickAxeDefinition getDef(int id) {
            return tools.get(id);
        }

        public static PickaxeDefinitions getToolDef(int id) {
            for (PickaxeDefinitions def : VALUES) {
                if (def.getId() == id) {
                    return def;
                }
            }
            return null;
        }

        public static Optional<PickaxeResult> get(final Player player, final boolean checkInventory) {
            final int level = player.getSkills().getLevel(SkillConstants.MINING);
            final Container inventory = player.getInventory().getContainer();
            final int weapon = player.getEquipment().getId(EquipmentSlot.WEAPON);
            for (PickAxeDefinition def : tools.values()){
                if (level < def.getLevel()) continue;
                if (weapon == def.getId()) {
                    return Optional.of(new PickaxeResult(def, player.getEquipment().getContainer(), 3,
                            player.getWeapon()));
                }
                if (checkInventory) {
                    for (int slot = 0; slot < 28; slot++) {
                        final Item item = inventory.get(slot);
                        if (item == null || item.getId() != def.getId()) {
                            continue;
                        }
                        return Optional.of(new PickaxeResult(def, player.getInventory().getContainer(), slot, item));
                    }
                }
            }
            return Optional.empty();
        }



        @Override
        public int getId() {
            return id;
        }

        @Override
        public int getLevel() {
            return level;
        }

        @Override
        public Animation getAnim() {
            return anim;
        }

        @Override
        public Animation getAlternateAnimation() {
            return alternateAnimation;
        }

        @Override
        public int getMineTime() {
            return mineTime;
        }


        public static final class PickaxeResult {
            private final PickAxeDefinition definition;
            private final Container container;
            private final int slot;
            private final Item item;

            public PickaxeResult(PickAxeDefinition definition, Container container, int slot, Item item) {
                this.definition = definition;
                this.container = container;
                this.slot = slot;
                this.item = item;
            }

            public PickAxeDefinition getDefinition() {
                return definition;
            }

            public Container getContainer() {
                return container;
            }

            public int getSlot() {
                return slot;
            }

            public Item getItem() {
                return item;
            }
        }
    }


    public enum ShapeDefinitions {
        THREE(ObjectId.ROCKS_11391),
        FOUR(ObjectId.ROCKS_11390, ObjectId.ROCKS_11364, ObjectId.ROCKS_11362, ObjectId.ROCKS_10943, ObjectId.ROCKS_11366, ObjectId.ROCKS_11372, ObjectId.ROCKS_11374, ObjectId.ROCKS_11370, ObjectId.ROCKS_11378, ObjectId.ROCKS_11380, ObjectId.ROCKS_11386, ObjectId.ROCKS_28596),
        ROCKSLIDE(27063, 27062),
        VOLCANIC_ASH(30986, 30985),
        WALL(ObjectId.EMPTY_WALL, ObjectId.CRYSTALS, ObjectId.CRYSTALS_11389),
        ANCIENT_ESSENCE_WALL(46702, 46701),
        SALT_ROCK(33253, 33254, 33255, 33256, 33257),
        TRAHAEARN(ObjectId.ROCKS_36202, TrahaearnMineRocks.Companion.getAllRockObjectIds()),
        SMALL_AMALGAMATION(49915, 49912),
        MEDIUM_AMALGAMATION(49916, 49913),
        LARGE_AMALGAMATION(49917, 49914)
        ;

        private final int empty;
        private final int[] ores;

        ShapeDefinitions(int empty, int... ores) {
            this.empty = empty;
            this.ores = ores;
        }

        public static int getEmpty(int id) {
            return rocks.getOrDefault(id, ObjectId.ROCKS_11391);
        }

        public int getEmpty() {
            return empty;
        }

        public int[] getOres() {
            return ores;
        }
    }
}
