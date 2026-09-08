package org.jesse.game.world.entity.player.container.impl

import org.jesse.game.item.Item
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.Container
import org.jesse.game.world.entity.player.container.ContainerResult
import org.jesse.game.world.entity.player.container.impl.bank.Bank
import org.jesse.game.world.entity.player.container.impl.bank.BankSetting
import java.util.function.Predicate

class SinglePlayerBank : Bank {

    @Transient
    private val player: Player

    constructor(player: Player) : super() {
        this.player = player
        container.linkPlayer(player)
    }

    constructor(player: Player, bank: Bank?) : super(bank) {
        this.player = player
        container.linkPlayer(player)
    }

    override fun onOpen(player: Player) = Unit

    override fun onClose(player: Player) = Unit

    override fun refreshQuantity() {
        player.varManager.sendBit(VARBIT_FIRST_OP_AMOUNT, quantity.value)
    }

    override fun testAddPredicate(predicate: Predicate<Player>): Boolean =
        predicate.test(player)

    override fun sendMessage(string: String) {
        player.sendMessage(string)
    }

    override fun removeItemDelegate(item: Item, placeholder: Boolean): ContainerResult =
        container.remove(item, placeholder, player)

    override fun requestContainerRefresh(container: Container) {
        container.refresh(player)
    }

    override fun updateVar(setting: BankSetting) {
        setting.updateVar(player)
    }

    override fun refreshTabSizes() {
        refreshBankSizes(player)
    }
}
