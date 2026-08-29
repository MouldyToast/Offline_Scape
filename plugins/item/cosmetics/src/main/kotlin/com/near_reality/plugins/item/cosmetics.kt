package com.near_reality.plugins.item

import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import mgi.types.config.items.WearableDefinition
import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*
import com.zenyte.game.world.entity.player.SkillConstants.*
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.*
import com.zenyte.game.world.entity.player.Bonuses.Bonus.*

class CosmeticsItems : ItemDefinitionsScript() {

    fun Int.makeTradable() = invoke(this) {
        tradable = true
    }

    fun Int.cosmetic(stand: Int, walk: Int? = null, run: Int? = null, block: WearableDefinition.() -> Unit = {
        bonuses(
            -100, -100, -50, 0, 0,
            0, 0, 0, 0, 0,
            -10, 0, 0, 0,
        )
    }) {
        invoke {
            tradable = true
            equipment {
                slot = EquipmentSlot.WEAPON.slot
                weapon {
                    standAnimation = stand
                    if (walk != null)
                        walkAnimation = walk
                    if (run != null)
                        runAnimation = run
                    standTurnAnimation = stand
                    rotate90Animation = stand
                    rotate180Animation = stand
                    rotate270Animation = stand
                    attackSpeed = 6
                }
                block()
            }
        }
    }

    init {
        CRATE_WITH_ZANIK.cosmetic(4193, 4194, 7274)

        CURSED_BANANA.cosmetic(4646, 4682, 6277)

        // https://oldschool.runescape.wiki/w/Skis
        26649.cosmetic(9341, 9342, 9346)

        CANDY_CANE.cosmetic(2911, 2912)
        CANDY_CANE_30105.cosmetic(2911, 2912)

        RING_OF_LEVITATION {
            tradable = true
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
        }

        SLED {
            tradable = true
            equipment {
                slot = EquipmentSlot.WEAPON.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                weapon {
                    standAnimation = 1461
                    walkAnimation = 1468
                    runAnimation = 1467
                    standTurnAnimation = 1461
                    rotate90Animation = 1461
                    rotate180Animation = 1461
                    rotate270Animation = 1461
                    attackSpeed = 6
                }
            }
        }

        CAT_EARS {
            tradable = true
            equipment {
                slot = EquipmentSlot.HELMET.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
        }

        HELL_CAT_EARS {
            tradable = true
            equipment {
                slot = EquipmentSlot.HELMET.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
        }

        PROPELLER_HAT {
            tradable = true
            equipment {
                slot = EquipmentSlot.HELMET.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
        }

        BANANA_HAT {
            tradable = true
            equipment {
                slot = EquipmentSlot.HELMET.slot
                bonuses(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            }
        }

        HEALER_ICON.makeTradable()
        ATTACKER_ICON.makeTradable()
        DEFENDER_ICON.makeTradable()
        COLLECTOR_ICON.makeTradable()

        GRIM_REAPER_HOOD.makeTradable()
        JACK_LANTERN_MASK.makeTradable()

        SKELETON_MASK.makeTradable()
        SKELETON_BOOTS.makeTradable()
        SKELETON_GLOVES.makeTradable()
        SKELETON_LEGGINGS.makeTradable()
        SKELETON_SHIRT.makeTradable()

        CHICKEN_HEAD.makeTradable()
        CHICKEN_FEET.makeTradable()
        CHICKEN_LEGS.makeTradable()
        CHICKEN_WINGS.makeTradable()

        EVIL_CHICKEN_HEAD.makeTradable()
        EVIL_CHICKEN_FEET.makeTradable()
        EVIL_CHICKEN_LEGS.makeTradable()
        EVIL_CHICKEN_WINGS.makeTradable()

        MIME_MASK.makeTradable()
        MIME_BOOTS.makeTradable()
        MIME_GLOVES.makeTradable()
        MIME_LEGS.makeTradable()
        MIME_TOP.makeTradable()
    }
}
