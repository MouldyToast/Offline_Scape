package org.jesse.api.dao

import org.jesse.api.dao.logs.AdminCPLogs
import org.jesse.api.dao.logs.ClanMessageLogs
import org.jesse.api.dao.logs.CollectionLogClaimLogs
import org.jesse.api.dao.logs.CommandLogs
import org.jesse.api.dao.logs.CreditStoreCheckoutLogs
import org.jesse.api.dao.logs.DropItemLogs
import org.jesse.api.dao.logs.DuelLogs
import org.jesse.api.dao.logs.GameServerLogs
import org.jesse.api.dao.logs.GrandExchangeOfferLogs
import org.jesse.api.dao.logs.GrandExchangeTransactionLogs
import org.jesse.api.dao.logs.KilledByNpcLogs
import org.jesse.api.dao.logs.KilledByPlayerLogs
import org.jesse.api.dao.logs.LoginLogs
import org.jesse.api.dao.logs.LogoutLogs
import org.jesse.api.dao.logs.MiddleManLogs
import org.jesse.api.dao.logs.MiscDeathLogs
import org.jesse.api.dao.logs.PetChuckLogs
import org.jesse.api.dao.logs.PickupItemLogs
import org.jesse.api.dao.logs.PrimalExchangeLogs
import org.jesse.api.dao.logs.PrivateMessageLogs
import org.jesse.api.dao.logs.PublicMessageLogs
import org.jesse.api.dao.logs.RareDropLogs
import org.jesse.api.dao.logs.ShopTransactionLogs
import org.jesse.api.dao.logs.TeleGrabItemLogs
import org.jesse.api.dao.logs.TradeLogs
import org.jesse.api.dao.logs.VoteLogs
import org.jesse.api.dao.logs.YellMessageLogs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionManager
import javax.sql.DataSource

object Db {

    lateinit var mainDatabase: Database
    lateinit var legacyLogsDatabase: Database

    fun initMainDatabase(create: Boolean, dataSource: DataSource) {
        mainDatabase = Database.connect(dataSource).apply {
            transactionManager.apply {
                defaultMaxAttempts = 5
                defaultMinRetryDelay = 5
                defaultMaxRetryDelay = 50
            }
        }
        if (create) {
            transaction(mainDatabase) {
                addLogger(StdOutSqlLogger)
                create(Users)
                create(UserIPs)
                create(ChallengeResults)
                create(RaidCompletions)
                create(AccessTokens)
                create(RefreshTokens)
                create(PasswordResetTokens)
                create(RegistrationTokens)
                create(Votes)
                create(VoteSites)
                create(CreditPackages)
                create(CreditPackageOrders)
                create(CreditStoreProducts)
                create(BundleItems)
                create(UserSkillStats)
                create(AccountSanctions)
                create(IPSanctions)
                create(UUIDSanctions)
                create(HardwareHashSanctions)
                create(NewsArticles)
                create(ItemConfigs)

                create(TradeLogs)
                create(TradeLogItems)
                create(SanctionLogs)
                create(LoginLogs)
                create(LogoutLogs)
                create(GameServerLogs)
                create(KilledByPlayerLogs)
                create(KilledByPlayerLogItems)
                create(KilledByNpcLogs)
                create(KilledByNpcLogItems)
                create(MiscDeathLogs)
                create(MiscDeathLogItems)
                create(CommandLogs)
                create(PickupItemLogs)
                create(TeleGrabItemLogs)
                create(DropItemLogs)
                create(MiddleManLogs)
                create(GrandExchangeOfferLogs)
                create(GrandExchangeTransactionLogs)
                create(DuelLogs)
                create(ClanMessageLogs)
                create(PrivateMessageLogs)
                create(PublicMessageLogs)
                create(RareDropLogs)
                create(CreditStoreCheckoutLogs)
                create(PrimalExchangeLogs)
                create(VoteLogs)
                create(ShopTransactionLogs)
                create(CollectionLogClaimLogs)
                create(AdminCPLogs)
                create(PetChuckLogs)
                create(YellMessageLogs)
            }
        }
    }


    suspend fun <T> dbQueryLogs(block: Transaction.() -> T): T = dbQuery(mainDatabase, block = block)
    suspend fun <T> dbQueryLegacyLogs(block: Transaction.() -> T): T = dbQuery(legacyLogsDatabase, block = block)

    suspend fun <T> dbQueryMain(block: Transaction.() -> T): T = dbQuery(mainDatabase, block = block)

    suspend fun <T> dbQuery(
        database: Database,
        transactionIsolationLevel: Int = database.transactionManager.defaultIsolationLevel,
        readOnly: Boolean = database.transactionManager.defaultReadOnly,
        block: Transaction.() -> T
    ): T = withContext(Dispatchers.IO) {
        transaction(
            transactionIsolationLevel,
            readOnly,
            database
        ) {
            maxAttempts = 5
            block()
        }
    }

    fun initLogsDatabase(create: Boolean, dataSource: DataSource) {
        legacyLogsDatabase = Database.connect(dataSource).apply {
            transactionManager.apply {
                defaultMaxAttempts = 5
                defaultMinRetryDelay = 5
                defaultMaxRetryDelay = 50
            }
        }
    }

}
