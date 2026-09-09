package org.jesse.game.content.middleman

import com.google.gson.GsonBuilder
import org.jesse.game.content.middleman.trade.MiddleManTrade
import org.jesse.util.gson.LocalDateTimeTypeAdapter
import java.time.LocalDateTime

object MiddleManConstants {

    val donatorPinItemIds = intArrayOf()

    /**
     * A gson instance for handling serialisation of [middle man trades][MiddleManTrade].
     */
    internal val gson = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .registerTypeAdapter(MiddleManStaffOption::class.java, MiddleManStaffOption.GsonAdapter)
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter)
        .create()

    internal const val MM_CONTROLLER = "mm-controller"
    internal const val MM_OFFER_ITEM_AMOUNT_ATTRIBUTE_KEY = "mm-offer-item-amount"
    internal const val MM_OFFER_ITEM_ID_ATTRIBUTE_KEY = "mm-offer-item-id"
    internal const val MM_TARGET_NAME_ATTRIBUTE_KEY = "mm-target-name"
    internal const val MM_STAFF_OPTION_ATTRIBUTE_KEY = "mm-staff-option"
    internal const val MM_STAFF_OPTIONS_ATTRIBUTE_KEY = "mm-staff-options"
    internal const val MM_VIEW_TRADE_LIST_ATTRIBUTE_KEY = "mm-view-trade-list"
    internal const val MM_VIEW_TRADE_HISTORY_LIST_ATTRIBUTE_KEY = "mm-view-trade-history-list"

    internal const val CONFIRMED_MM_TRADE_PATH = "data/middleman/confirmed_middleman_trades.json"
    internal const val HANDLED_MM_TRADE_PATH = "data/middleman/handled_middleman_trades.json"
}
