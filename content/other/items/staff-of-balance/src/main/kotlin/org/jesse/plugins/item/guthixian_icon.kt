import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
