package com.near_reality.content.donatestore;

import com.near_reality.api.model.*;
import com.near_reality.api.service.user.UserPlayerAttributesKt;
import com.near_reality.api.service.store.StorePlayerHandler;
import com.near_reality.cache_tool.packing.custom.NearRealityStoreInterfacePacker;
import com.near_reality.game.content.storebundle.BundleChest;
import com.near_reality.game.content.storebundle.BundleUpdater;
import com.near_reality.game.model.ui.credit_store.*;
import com.near_reality.game.model.ui.credit_store.CreditPackage;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.item._Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.world.entity.player.Player;
import kotlinx.datetime.Clock;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.enums.IntEnum;
import mgi.types.config.items.ItemDefinitions;
import org.slf4j.event.Level;

import java.util.*;

@SuppressWarnings("unused")
public class DonationStoreInterface extends Interface {

	//Re-used cs2 from helper client!
	private static final int TRANSMIT_ITEMS_CS2 = 4713;
	private static final int TRANSMIT_LIMITED_ITEMS_CS2 = 46143;

	private static final int TAB_VAR = 261;
	private static final int PURCHASE_TYPE_VAR = 262;
	private static final int CATEGORY_VAR = 263;
	private static final int DONATED_TOTAL_VAR = 264;
	private static final int CREDITS_VAR = 265;
	private static final int CLAIMED_LOYALTY_REWARDS_VAR = 266;
	private static final int DONATED_TOTAL_AFTER_LAUNCH_VAR = 1601;

	public static void transmitOffers(Player player) {
		CreditStoreType type = player.isIronman() ? CreditStoreType.Ironman : CreditStoreType.Regular;

		List<CreditStoreProduct> products = new ArrayList<>();
		StringBuilder data = new StringBuilder();

		int index = 0;
		Map<CreditStoreCategory, List<CreditStoreProduct>> map = CreditStoreModel.storeProductsByCategory;
		for (CreditStoreCategory category : CreditStoreCategory.getEntries()) {
			data.setLength(0);

			List<CreditStoreProduct> list = map.get(category);
			if (list != null) {
				products.clear();
				for (CreditStoreProduct product : list) {
					if (product.getTypes().contains(type)) {
						products.add(product);
					}
				}

				if (category == CreditStoreCategory.LimitedTime) {
					products.sort(Comparator.comparingInt(CreditStoreProduct::getQuantity).reversed().thenComparingInt(CreditStoreProduct::getSortPriority));
				} else {
					products.sort(Comparator.comparingInt(CreditStoreProduct::getSortPriority));
				}

				encodeList(data, products, player.getStoreLimitedStockPurchases());
			}

			player.getPacketDispatcher().sendClientScript(TRANSMIT_ITEMS_CS2, index, data.toString());
			index++;
		}
	}

	private static void transmitPlayerIndividualOffers(Player player) {
		List<CreditStoreProduct> products = new ArrayList<>();
		StringBuilder data = new StringBuilder();

		for (CreditStoreProduct product : CreditStoreModel.storeProducts) {
			if (!product.getTypes().contains(CreditStoreType.LimitedPerPlayer)) continue;
			products.add(product);
		}

		encodeList(data, products, player.getStoreLimitedStockPurchases());
		player.getPacketDispatcher().sendClientScript(TRANSMIT_LIMITED_ITEMS_CS2, data.toString());
	}

	@SuppressWarnings({"DataFlowIssue"})
	private static void encodeList(StringBuilder data, List<CreditStoreProduct> products, Map<Integer, Integer> limitedStockPurchases) {
		int len = products.size();
		data.append(len);
		data.append("|");
		for (int i = 0; i < len; i++) {
			CreditStoreProduct product = products.get(i);
			final int productId = product.getId();
			final int itemId = product.getItemId();
			data.append(productId);
			data.append("|");
			data.append(ItemDefinitions.nameOf(itemId));
			data.append("|");
			data.append(product.getPrice());
			data.append("|");
			data.append(product.getQuantity() - limitedStockPurchases.getOrDefault(productId, 0));
			data.append("|");
			List<CreditStoreBundleItem> bundleItems = BundleUpdater.getBundleItemsByBundleChest().get(BundleChest.getForItemOrNull(itemId));
			if (bundleItems != null) {
				int itemsLen = bundleItems.size();
				data.append(itemsLen);
				data.append("|");
				for (int j = 0; j < itemsLen; j++) {
					CreditStoreBundleItem bundleItem = bundleItems.get(j);
					data.append(bundleItem.getItemId());
					data.append("|");
					data.append(bundleItem.getAmount());
					if (j < itemsLen - 1) {
						data.append("|");
					}
				}
			} else {
				data.append("1");
				data.append("|");
				data.append(itemId);
				data.append("|");
				data.append(product.getItemAmount());
			}
			data.append("|");
		}
	}

	@Override
	public void open(Player player) {
		transmitOffers(player);
		transmitTab(player);
		setPurchaseType(player, CreditPackageOrder.PaymentMethod.PAYPAL);
		transmitCategory(player);
		player.getVarManager().sendVarInstant(DONATED_TOTAL_VAR, UserPlayerAttributesKt.getStoreTotalSpent(player));
		transmitCredits(player);
		transmitClaimedLoyaltyRewards(player);
		player.getVarManager().sendVarInstant(DONATED_TOTAL_AFTER_LAUNCH_VAR, PlayerAttributesKt.getTotalDonatedAfterLaunch(player));

		super.open(player);

		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("list"), 0, 20, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("stock_transmit"), 0, 100, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("loyalty_footer_transmit"), 0, 20, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("popup_transmit"), 0, 10, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("loyalty_side_transmit"), 0, 10, AccessMask.CLICK_OP1);
		player.getPacketDispatcher().sendComponentSettings(getInterface().getId(), getComponent("loyalty_content_transmit"), 0, 190, AccessMask.CLICK_OP1);
		transmitPlayerIndividualOffers(player);
	}

	@Override
	protected void attach() {
		put(5, "tab_1");
		put(10, "tab_2");
		put(15, "tab_3");
		put(20, "tab_4");
		put(33, "list");
		put(36, "stock_transmit");
		put(49, "loyalty_side_transmit");
		put(52, "loyalty_content_transmit");
		put(57, "loyalty_footer_transmit");
		put(66, "popup_transmit");
	}

	@Override
	protected void build() {
		bind("tab_1", player -> setTab(player, 0));
		bind("tab_2", player -> setTab(player, 1));
		bind("tab_3", player -> setTab(player, 2));
		bind("tab_4", player -> setTab(player, 3));
		bind("list", (player, slotId, itemId, option) -> {
			PlayerAttributesKt.setStoreCategory(player, slotId);
			transmitCategory(player);
		});
		bind("stock_transmit", (player, slotId, productId, option) -> {
			player.lock(1);
			if (option != 1) {
				return;
			}

			CreditStoreProduct product = CreditStoreModel.storeProductsById.get(productId);
			if (product == null) {
				player.sendMessage("This product is no longer available.");
				return;
			}

			CreditStoreCart cart = CreditStoreInterfaceKt.getStoreCart(player);
			cart.clear();
			cart.add(product, 1);
			CreditStoreModel.checkout(player, false, null);
		});
		bind("loyalty_side_transmit", (player, slotId, itemId, option) -> {
			player.lock(1);
			if (option != 1) {
				return;
			}

			transmitPlayerIndividualOffers(player);
		});
		bind("loyalty_content_transmit", (player, slotId, productId, option) -> {
			player.lock(1);
			if (option != 1) {
				return;
			}

			CreditStoreProduct product = CreditStoreModel.storeProductsById.get(productId);
			if (product == null) {
				player.sendMessage("This product is no longer available.");
				return;
			}

			int purchasedCount = player.getStoreLimitedStockPurchases().getOrDefault(productId, 0);
			int maxPurchases = product.getQuantity();
			if (maxPurchases != Integer.MAX_VALUE && purchasedCount >= maxPurchases) {
				player.sendMessage("You have already purchased the maximum amount of this product.");
				return;
			}

			CreditStoreCart cart = CreditStoreInterfaceKt.getStoreCart(player);
			cart.clear();
			cart.add(product, 1);
			CreditStoreModel.checkout(player, false, () -> player.getStoreLimitedStockPurchases().put(productId, purchasedCount + 1));
		});
		bind("loyalty_footer_transmit", (player, slotId, itemId, option) -> {
			player.lock(1);
			IntEnum required = EnumDefinitions.getIntEnum(NearRealityStoreInterfacePacker.LOYALTY_DONATED_ENUM);
			IntEnum rewards = EnumDefinitions.getIntEnum(NearRealityStoreInterfacePacker.LOYALTY_REWARDS_ENUM);
			int rewardId = rewards.getValueOrDefault(slotId);
			if (rewardId == -1) {
				return;
			}

			if (option == 10) {
				ItemUtil.sendItemExamine(player, rewardId);
				return;
			}

			int dollarsRequired = required.getValueOrDefault(slotId);
			int donatedAfterLaunch = PlayerAttributesKt.getTotalDonatedAfterLaunch(player);
			if (donatedAfterLaunch < dollarsRequired) {
				player.sendMessage("Your donated amount is insufficient to claim this reward.");
				return;
			}

			int mask = 1 << slotId;
			int claimed = PlayerAttributesKt.getStoreLoyaltyRewardsClaimed(player);
			if ((claimed & mask) != 0) {
				player.sendMessage("You have already claimed this reward.");
				return;
			}

			IntEnum rewardsCount = EnumDefinitions.getIntEnum(NearRealityStoreInterfacePacker.LOYALTY_REWARDS_COUNT_ENUM);
			Item item = new Item(rewardId, rewardsCount.getValueOrDefault(slotId));
			if (!player.getInventory().hasSpaceFor(item)) {
				player.sendMessage("You do not have enough inventory space to claim this item.");
				return;
			}

			player.getInventory().addItem(item);
			PlayerAttributesKt.setStoreLoyaltyRewardsClaimed(player, claimed | mask);
			transmitClaimedLoyaltyRewards(player);
			GameLogger.log(Level.INFO, () -> new GameLogMessage.ShopTransaction.Purchase(Clock.System.INSTANCE.now(), player.getDbUsername(), "Donator Shop Loyalty", new _Item(rewardId, 1), dollarsRequired, "USD spent"));
			player.sendMessage("You have claimed your loyalty reward: " + ItemDefinitions.nameOf(rewardId) + ".");
		});
		bind("popup_transmit", (player, slotId, itemId, option) -> {
			player.lock(1);
			if (option != 1) {
				return;
			}

			switch (slotId) {
				case 6:
					setPurchaseType(player, CreditPackageOrder.PaymentMethod.PAYPAL);
					break;
				case 7:
					setPurchaseType(player, CreditPackageOrder.PaymentMethod.COINBASE);
					break;
				default:
					StorePlayerHandler.startNewOrder(player, CreditPackage.getValues()[slotId], CreditPackageOrder.PaymentMethod.getValues()[player.getVarManager().getValue(PURCHASE_TYPE_VAR)]);
					break;
			}
		});
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.DONATION_STORE;
	}

	private static void setTab(Player player, int tab) {
		PlayerAttributesKt.setStoreTab(player, tab);
		transmitTab(player);
	}

	private static void transmitTab(Player player) {
		player.getVarManager().sendVarInstant(TAB_VAR, PlayerAttributesKt.getStoreTab(player));
	}

	private static void setPurchaseType(Player player, CreditPackageOrder.PaymentMethod type) {
		player.getVarManager().sendVarInstant(PURCHASE_TYPE_VAR, type.ordinal());
	}

	private static void transmitCategory(Player player) {
		player.getVarManager().sendVarInstant(CATEGORY_VAR, PlayerAttributesKt.getStoreCategory(player));
	}

	private static void transmitCredits(Player player) {
		player.getVarManager().sendVarInstant(CREDITS_VAR, UserPlayerAttributesKt.getStoreCredits(player));
	}

	private static void transmitClaimedLoyaltyRewards(Player player) {
		player.getVarManager().sendVarInstant(CLAIMED_LOYALTY_REWARDS_VAR, PlayerAttributesKt.getStoreLoyaltyRewardsClaimed(player));
	}

}
