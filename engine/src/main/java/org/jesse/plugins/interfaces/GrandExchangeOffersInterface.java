package org.jesse.plugins.interfaces;

import org.jesse.game.model.item.ItemValueExtKt;
import org.jesse.game.GameInterface;
import org.jesse.game.content.grandexchange.ExchangeType;
import org.jesse.game.content.grandexchange.GrandExchange;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.UserInterface;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;

/**
 * @author Tommeh | 25 nov. 2017 : 19:53:40
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public final class GrandExchangeOffersInterface implements UserInterface {
    @Override
    public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
        final boolean buying = player.getVarManager().getBitValue(GrandExchange.TYPE_VARPBIT) == 0;
        final int currentItemId = player.getVarManager().getValue(GrandExchange.ITEM_VARP);
        final GrandExchange exchange = player.getGrandExchange();
        if (interfaceId == GrandExchange.INTERFACE) {
            switch (componentId) {
            case 3:
                exchange.openHistoryInterface();
                break;
            case 4:
                exchange.openOffersInterface();
                break;
            case 6:
                exchange.collectAll(optionId == 1, true);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                final int exchangeSlot = componentId - 7;
                if (slotId == 2) {
                    if (optionId == 1) {
                        exchange.viewOffer(exchangeSlot);
                    } else {
                        exchange.abortOffer(exchangeSlot);
                    }
                } else if (slotId == 3) {
                    exchange.buy(exchangeSlot, true);
                } else if (slotId == 4) {
                    exchange.sell(exchangeSlot, true);
                }
                break;
            case 23:
                exchange.abortOffer();
                return;
            case 24:
                exchange.collectItems(optionId, slotId);
                return;
            case 25:
                if (slotId == 0) {
                    player.sendInputItem("What would you like to " + (buying ? "buy?" : "sell?"), exchange::buy);
                } else if (slotId >= 1 && slotId <= 6) {
                    if (currentItemId <= 0) {
                        return;
                    }
                    int amount = (slotId == 2 || slotId == 3 ? 1 : slotId == 4 ? 10 : slotId == 5 ? 100 : slotId == 6 ? 1000 : -1);
                    final int currentQuantity = exchange.getQuantity();
                    if (!buying) {
                        if (slotId == 6) {
                            //All
                            final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                            final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                            amount = player.getInventory().getAmountOf(otherId) + player.getInventory().getAmountOf(currentItemId);
                        }
                    }
                    if (amount < 1 && slotId != 1) {
                        return;
                    }
                    int quantity = (!buying && slotId == 6) ? amount : currentQuantity == 1 && slotId != 2 && slotId != 3 ? amount : (currentQuantity + amount);
                    if (!buying) {
                        final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                        final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                        final int inInventory = player.getInventory().getAmountOf(otherId) + (otherId == currentItemId ? 0 : player.getInventory().getAmountOf(currentItemId));
                        if (inInventory < quantity) {
                            quantity = inInventory;
                            player.sendMessage("You haven't got enough of that item.");
                        }
                    }
                    if (quantity < 1) {
                        quantity = 1;
                    }
                    exchange.modifyQuantity(quantity);
                } else {
                    if (player.getVarManager().getValue(GrandExchange.ITEM_VARP) != -1) {
                        final int price = exchange.getPrice();
                        switch (slotId) {
                        case 7:
                            player.sendInputInt("How many do you wish to " + (buying ? "buy?" : "sell?"), quantity -> {
                                if (!buying) {
                                    final ItemDefinitions def = ItemDefinitions.get(currentItemId);
                                    final int otherId = def.isNoted() ? def.getUnnotedOrDefault() : def.getNotedId();
                                    final int inInventory = player.getInventory().getAmountOf(otherId) + player.getInventory().getAmountOf(currentItemId);
                                    if (inInventory < quantity) {
                                        quantity = inInventory;
                                        player.sendMessage("You haven't got enough of that item.");
                                    }
                                }
                                exchange.modifyQuantity(quantity);
                            });
                            break;
                        case 8:
                            exchange.modifyPrice(price - 1);
                            break;
                        case 9:
                            exchange.modifyPrice(price + 1);
                            break;
                        case 10:
                            exchange.modifyPrice(Math.max(1, Math.min((int) Math.ceil(price * 0.95), price - 1)));
                            break;
                        case 11:
                            exchange.modifyPrice(new Item(player.getVarManager().getValue(GrandExchange.ITEM_VARP)).getSellPrice());
                            break;
                        case 12:
                            player.sendInputInt("Set a price for each item:", exchange::modifyPrice);
                            break;
                        case 13:
                            exchange.modifyPrice(Math.max((int) (price * 1.05), price + 1));
                            break;
                        case 14:// minus X
                        case 15: // plus X
                            if (optionId == 1) {
                                int pct = player.getVarManager().getBitValue(4284);
                                if (slotId == 14) {
                                    exchange.modifyPrice(Math.max(1, Math.min((int) Math.ceil(price * (1 - (double) pct / 100)), price - 1)));
                                } else {
                                    exchange.modifyPrice(Math.max((int)(price * (1 + (double) pct / 100)), price + 1));
                                }
                            } else {
                                player.sendInputInt("Set a percentage to decrease/increase the price by (between 1 and 99):", pct -> {
                                    if (pct > 99) {
                                        pct = 99;
                                    } else if (pct < 1) {
                                        pct = 1;
                                    }
                                    if (slotId == 14) {
                                        exchange.modifyPrice(Math.max(1, Math.min((int) Math.ceil(price * (1 - (double) pct / 100)), price - 1)));
                                    } else {
                                        exchange.modifyPrice(Math.max((int) (price * (1 + (double) pct / 100)), price + 1));
                                    }
                                    player.getVarManager().sendBit(4284, pct);
                                });
                            }
                            break;
                        }
                    }
                }
                break;
            case 29:
                exchange.createOffer();
                break;
            }
        } else if (interfaceId == GrandExchange.INVENTORY_INTERFACE) {
            switch (componentId) {
            case 0:
                final Item item = player.getInventory().getItem(slotId);
                if (item == null) {
                    return;
                }
                if (optionId == 10) {
                    ItemUtil.sendItemExamine(player, item);
                    return;
                }
                if (exchange.sell(item)) {
                    if (player.getVarManager().getBitValue(GrandExchange.SLOT_VARPBIT) == 0) {
                        final int freeSlot = exchange.getFreeSlot();
                        exchange.viewOffer(freeSlot);
                    }
                }
                break;
            }
        }
    }

    @Override
    public int[] getInterfaceIds() {
        return new int[] {GrandExchange.INTERFACE, GrandExchange.INVENTORY_INTERFACE};
    }
}
