import com.near_reality.game.item.CustomItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class GodBowsItemaction : ItemActionScript() {

    init {
        items(CustomItemId.BANDOS_BOW, CustomItemId.ARMADYL_BOW, CustomItemId.ZAMORAK_BOW, CustomItemId.SARADOMIN_BOW)

        death {
            if (!pvp) {
                kept {
                    yield(item)
                }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else {
                lost {
                    yield(item)
                }
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
