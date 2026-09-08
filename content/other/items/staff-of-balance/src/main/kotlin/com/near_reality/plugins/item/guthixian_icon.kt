import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.GUTHIXIAN_ICON

class GuthixianIconItemaction : ItemActionScript() {

    init {
        items(GUTHIXIAN_ICON)

        "Scrutinise" {
            player.dialogue {
                plain(Colour.RED.wrap(
                    "The icon honours Guthix with a depiction of the god's loyal servant, " +
                        "Juna, whom you met deep below Lumbridge. Perhaps she can do something with it."))
            }
        }
    }
}
