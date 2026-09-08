package org.jesse.game.model.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemOnItemExtension;
import org.jesse.plugins.Plugin;

/**
 * @author Kris | 10. nov 2017 : 23:58.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public interface ItemOnItemAction extends Plugin, ItemOnItemExtension {

	int[] getItems();

	default boolean allItems() { return false; }

	default ItemPair[] getMatchingPairs() {
		return null;
	}

    /**
     * @return Whether or not you want the script to include same item id usage; by default it's set to false.
     * Ex. using id of 900 on another item with id of 900 will not launch the script unless this
     * is true.
     */
    default boolean includeEquivalentItems() {
        return false;
    }

    final class ItemPair {
        final int left;
        final int right;

        public ItemPair(int left, int right) {
            this.left = left;
            this.right = right;
        }

		public static ItemPair of(final Item left, final Item right) {
			return of(left.getId(), right.getId());
		}

        public static ItemPair of(final int left, final int right) {
            return new ItemPair(left, right);
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }
    }

}
