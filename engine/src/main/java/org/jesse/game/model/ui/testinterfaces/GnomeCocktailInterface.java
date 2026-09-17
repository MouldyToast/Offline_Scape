package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.content.skills.cooking.CocktailShaker;
import org.jesse.game.content.skills.cooking.GnomeCocktail;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.plugins.dialogue.PlainChat;
import mgi.types.config.items.ItemDefinitions;

public class GnomeCocktailInterface extends Interface {
    private static final String SELECTED_COCKTAIL_SLOT = "selected gnome slot";

    @Override
    protected void attach() {
        put(9, "Create");
        for (GnomeCocktail cocktail : GnomeCocktail.VALUES) {
            put(GnomeCocktail.COMPONENT_ID, cocktail.getSlotId(), cocktail.name());
        }
    }

    @Override
    public void open(Player player) {
        player.getVarManager().sendBit(698, 0);
        player.getInterfaceHandler().sendInterface(getInterface());
        player.getPacketDispatcher().sendComponentSettings(getInterface(), GnomeCocktail.COMPONENT_ID, 0, 6, AccessMask.CLICK_OP1);
    }

    @Override
    protected void build() {
        for (GnomeCocktail cocktail : GnomeCocktail.VALUES) {
            bind(cocktail.name(), player -> player.getAttributes().put(SELECTED_COCKTAIL_SLOT, cocktail.getSlotId()));
        }
        bind("Create", player -> {
            final GnomeCocktail cocktail = GnomeCocktail.getBySlot((Integer) player.getAttributes().get(SELECTED_COCKTAIL_SLOT));
            if (player.getInventory().containsItems(cocktail.getIngredients()) && player.getSkills().getLevel(SkillConstants.COOKING) >= cocktail.getLevel()) {
                final CocktailShaker shaker = CocktailShaker.SHAKERS.get(cocktail.getShaker());
                final String heat = (shaker.equals(CocktailShaker.CHOCOLATE_SATURDAY) || shaker.equals(CocktailShaker.DRUNK_DRAGON)) ? ", (heat it)," : "";
                StringBuilder query = new StringBuilder();
                for (int index = 0; index < shaker.getGarnish().length; index++) {
                    String name = ItemDefinitions.get(shaker.getGarnish()[index]).getName().toLowerCase();
                    name = name.contains("pot") ? "cream" : name;
                    if (shaker.getGarnish().length != 1) {
                        query.append(index == shaker.getGarnish().length - 1 ? "and " + name : name + ", ");
                    } else {
                        query = new StringBuilder(name);
                    }
                }
                player.getInventory().deleteItems(cocktail.getIngredients());
                player.getInventory().deleteItem(ItemId.COCKTAIL_SHAKER, 1);
                player.getInventory().addItem(cocktail.getShaker(), 1);
                player.getSkills().addXp(SkillConstants.COOKING, cocktail.getExperience());
                player.getDialogueManager().start(new PlainChat(player, "You just need to pour this into an empty cocktail glass" + heat + " and garnish it with " + query + " before serving."));
            } else {
                player.getDialogueManager().start(new PlainChat(player, "You either don't have all the ingredients, or the cooking level required (or both) to make this."));
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GNOME_COCKTAIL;
    }
}
