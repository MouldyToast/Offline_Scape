package com.near_reality.api

import cloud.rsps.game.hiscores.HiscoresDatabase
import cloud.rsps.worlds.database.WorldsDatabase
import com.near_reality.api.dao.BundleItemEntity
import com.near_reality.api.dao.ChallengeResultEntity
import com.near_reality.api.dao.CreditPackageOrderEntity
import com.near_reality.api.dao.CreditPackageOrders
import com.near_reality.api.dao.CreditPackages
import com.near_reality.api.dao.CreditStoreProductEntity
import com.near_reality.api.dao.Db
import com.near_reality.api.dao.Db.dbQueryMain
import com.near_reality.api.dao.ItemConfigEntity
import com.near_reality.api.dao.RaidCompletion
import com.near_reality.api.dao.SanctionLogEntity
import com.near_reality.api.dao.UserEntity
import com.near_reality.api.dao.logs.ClanMessageLogEntity
import com.near_reality.api.dao.logs.CollectionLogClaimLogEntity
import com.near_reality.api.dao.logs.CommandLogEntity
import com.near_reality.api.dao.logs.CreditStoreCheckoutLogEntity
import com.near_reality.api.dao.logs.DropItemLogEntity
import com.near_reality.api.dao.logs.DuelLogEntity
import com.near_reality.api.dao.logs.FlowerPokerSessionLogEntity
import com.near_reality.api.dao.logs.GameServerLogEntity
import com.near_reality.api.dao.logs.GrandExchangeOfferLogEntity
import com.near_reality.api.dao.logs.GrandExchangeTransactionLogEntity
import com.near_reality.api.dao.logs.KilledByNpcLogEntity
import com.near_reality.api.dao.logs.KilledByPlayerLogEntity
import com.near_reality.api.dao.logs.LoginLogEntity
import com.near_reality.api.dao.logs.LogoutLogEntity
import com.near_reality.api.dao.logs.MiddleManLogEntity
import com.near_reality.api.dao.logs.MiscDeathLogEntity
import com.near_reality.api.dao.logs.PetChuckEntity
import com.near_reality.api.dao.logs.PickupItemLogEntity
import com.near_reality.api.dao.logs.PrimalExchangeLogEntity
import com.near_reality.api.dao.logs.PrivateMessageLogEntity
import com.near_reality.api.dao.logs.PublicMessageLogEntity
import com.near_reality.api.dao.logs.RareDropLogEntity
import com.near_reality.api.dao.logs.ShopTransactionLogEntity
import com.near_reality.api.dao.logs.TeleGrabItemLogEntity
import com.near_reality.api.dao.logs.TradeLogEntity
import com.near_reality.api.dao.logs.VoteLogEntity
import com.near_reality.api.dao.logs.YellMessageLogEntity
import com.near_reality.api.model.CreditPackageOrder
import com.near_reality.api.model.CreditStoreBundleItem
import com.near_reality.api.model.CreditStoreProduct
import com.near_reality.api.model.DeathResult
import com.near_reality.api.model.GrandExchangeOfferType
import com.near_reality.api.model.Item
import com.near_reality.api.model.ItemConfig
import com.near_reality.api.model.Location
import com.near_reality.api.responses.UserLoginResponse
import com.near_reality.api.util.BCrypt
import com.near_reality.api.util.defaultTimeZone
import com.near_reality.game.world.info.DatabaseProfile
import com.near_reality.game.world.info.WorldProfile
import com.near_reality.security.MFAManager
import com.near_reality.tools.logging.ChallengeLog
import com.near_reality.tools.logging.GameLogMessage
import com.near_reality.tools.logging.SlotItemMap
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import com.zenyte.game.GameConstants
import com.zenyte.game.content.grandexchange.ExchangeType
import com.zenyte.game.item.ItemId
import com.zenyte.game.item._Item
import com.zenyte.game.world.entity._Location
import com.zenyte.game.world.entity.player.login.AuthType
import com.zenyte.game.world.entity.player.login.LoginPacketIn
import com.zenyte.logger.NearRealityLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime
import mgi.types.config.npcs.NPCDefinitions
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.and
import javax.sql.DataSource

@Suppress("unused")
object GameDatabase {

    private val logger = NearRealityLogger.getLogger(this::class.java)
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @JvmStatic
    fun init(profile: WorldProfile) {
        tryInitDatabase("logs", profile.logsDatabase, Db::initLogsDatabase)
        tryInitDatabase("main", profile.mainDatabase, Db::initMainDatabase)
        tryInitDatabase("hiscores", profile.hiscoresDatabase, HiscoresDatabase::init)
        tryInitDatabase("worlds", profile.worldsDatabase) { create, dataSource ->
            WorldsDatabase.init(profile, create, dataSource)
        }
    }

    private fun tryInitDatabase(name: String, databaseProfile: DatabaseProfile?, init: (Boolean, DataSource) -> Unit) {
        try {
            if (databaseProfile == null) {
                logger.warn("No $name database settings found in world profile, not initializing db service")
            } else if (databaseProfile.enabled) {
                val dataSource = hikari(databaseProfile)
                init(databaseProfile.create, dataSource)
            }
        } catch (e: Exception) {
            logger.error("Failed to initialize $name database service", e)
        }
    }

    private fun hikari(settings: DatabaseProfile): HikariDataSource {
        val hikariConfig = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "${settings.databaseUrl}:${settings.databasePort}/${settings.databaseName}?tcpKeepAlive=true"
            username = settings.databaseUser
            password = settings.databasePassword
            maximumPoolSize = 15
            isAutoCommit = true // TODO: change later and schedule commits every x minutes
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        hikariConfig.validate()
        return HikariDataSource(hikariConfig)
    }

    suspend fun retrieveItemConfigs(): List<ItemConfig> {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return emptyList()
        return Db.dbQueryMain { ItemConfigEntity.all().map { it.toModel() } }
    }

    suspend fun retrieveCreditStoreProducts(): List<CreditStoreProduct> {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return emptyList()
        return Db.dbQueryMain { CreditStoreProductEntity.all().map { it.toModel() } }
    }

    suspend fun retrieveBundleItems(): List<CreditStoreBundleItem> {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return emptyList()
        return Db.dbQueryMain { BundleItemEntity.all().map { it.toApiModel() } }
    }

    suspend fun evalContest(log: ChallengeLog) {
        if (!GameConstants.WORLD_PROFILE.isMainDatabaseEnabled())
            return
        Db.dbQueryMain {
            val results = ChallengeResultEntity.all().firstOrNull() ?: ChallengeResultEntity.new {}
            when(log) {
                is ChallengeLog.DroppedItem -> {
                    when(log.itemId){
                        ItemId.TWISTED_BOW ->  if(results.firstTwistedBow == null) results.firstTwistedBow = log.username
                        ItemId.SANGUINE_DUST -> if(results.firstSanguineDust == null) results.firstSanguineDust = log.username
                        ItemId.OLMLET -> if(results.firstOlmlet == null) results.firstOlmlet = log.username
                        ItemId.METAMORPHIC_DUST -> if(results.firstMetamorphicDust == null) results.firstMetamorphicDust = log.username
                        ItemId.SCYTHE_OF_VITUR_UNCHARGED -> if(results.firstScytheOfVitur == null) results.firstScytheOfVitur = log.username
                        ItemId.LIL_ZIK -> if(results.firstLilZik == null) results.firstLilZik = log.username
                    }
                }
                is ChallengeLog.AchievementCape -> {
                    if(results.firstAchievementCape == null)
                        results.firstAchievementCape = log.username
                }
                is ChallengeLog.MaxedAccount -> {
                    if(log.ironman && results.firstMaxIronmanAccount == null)
                        results.firstMaxIronmanAccount = log.username
                    if(log.gamemode.equals("realist", true) && results.firstMaxRealistAccount == null)
                        results.firstMaxRealistAccount = log.username
                    if(log.ironman && log.gamemode.equals("realist", true) && results.firstMaxRealistIMAccount == null)
                        results.firstMaxRealistIMAccount = log.username
                    if(log.gamemode.equals("uim", true) && results.firstMaxUIMAccount == null)
                        results.firstMaxUIMAccount = log.username
                }
                is ChallengeLog.SlayerStatue -> {
                    if(results.firstSlayerStatue == null)
                        results.firstSlayerStatue = log.username
                }
                is ChallengeLog.SoloCOXCMClear ->  {
                    if(results.fastestSoloCMTimeRecordCOX == null) {
                        results.fastestSoloCMTimeRecordCOX = log.username
                        results.fastestTimeInSecondsCSCM = log.clearTimeInSeconds
                    } else {
                        val record = results.fastestTimeInSecondsCSCM
                        if(record != null && record > log.clearTimeInSeconds) {
                            results.fastestTimeInSecondsCSCM = log.clearTimeInSeconds
                            results.fastestSoloCMTimeRecordCOX = log.username
                        }
                    }
                }
            }
        }
    }

    suspend fun append(log: GameLogMessage) {
        if (!GameConstants.WORLD_PROFILE.isLogsDatabaseEnabled())
            return
        Db.dbQueryLogs {
            when (log) {
                is GameLogMessage.CreditStoreCheckout -> CreditStoreCheckoutLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    cart = log.cart
                }
                is GameLogMessage.MiddleManTrade -> MiddleManLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    requester = log.requester
                    accepter = log.accepter
                    middleman = log.middleman
                    requesterDonatorPin = log.requesterDonatorPin.toItemAdminCp()
                    accepterItems = log.accepterItems.toSlotItemMapAdminCp()
                    accepterOSRSMillions = log.accepterOSRSMillions
                }
                is GameLogMessage.ClaimedVotes -> VoteLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    ip = log.ipAddress
                    votesClaimed = log.votesClaimed
                    votesBonus = log.votesBonus
                }
                is GameLogMessage.Command -> CommandLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    commandName = log.commandName
                    commandParameters = log.commandParameters
                }
                is GameLogMessage.Death.Killed.ByNpc -> KilledByNpcLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    npcId = log.npcId
                    npc = NPCDefinitions.get(log.npcId)?.name?:"null"
                    username = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Death.Killed.ByPlayer -> KilledByPlayerLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    killer = log.otherUsername
                    victim = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Death.Misc -> MiscDeathLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    location = log.location.toLocationAdminCp()
                    deathResult = toDeathResultAdminCp(log)
                }
                is GameLogMessage.Duel -> DuelLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    winner = log.winnerUsername
                    player1 = log.username
                    player2 = log.otherUsername
                    player1Items = log.items.toSlotItemMapAdminCp()
                    player2Items = log.otherItems.toSlotItemMapAdminCp()
                }
                is GameLogMessage.FlowerPokerSession -> FlowerPokerSessionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    player1 = log.username
                    player2 = log.otherUsername
                    player1Items = log.items.toSlotItemMapAdminCp()
                    player2Items = log.otherItems.toSlotItemMapAdminCp()
                    winner = log.winnerUsername
                }
                is GameLogMessage.GrandExchangeOffer -> GrandExchangeOfferLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    creator = log.username
                    offerItem = log.offer.item.toItemAdminCp()
                    offerPrice = log.offer.price
                    offerType = log.offer.type.toExchangeTypeAdminCp()
                }
                is GameLogMessage.GrandExchangeTransaction.Purchase -> GrandExchangeTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    accepter = log.username
                    creator = log.otherUsername
                    offerItem = log.item.toItemAdminCp()
                    offerPrice = log.priceEach
                    offerType = GrandExchangeOfferType.BUY
                }
                is GameLogMessage.GrandExchangeTransaction.Sell -> GrandExchangeTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    creator = log.username
                    accepter = log.otherUsername
                    offerItem = log.item.toItemAdminCp()
                    offerPrice = log.priceEach
                    offerType = GrandExchangeOfferType.SELL
                }
                is GameLogMessage.GroundItem.Drop -> DropItemLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.GroundItem.Pickup -> PickupItemLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.GroundItem.TeleGrab -> TeleGrabItemLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.Login -> LoginLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    ip = log.ip
                }
                is GameLogMessage.Logout -> LogoutLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    ip = log.ip
                }
                is GameLogMessage.Message.Clan -> ClanMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    message = log.contents
                    channelName = log.channelName
                    channelOwner = log.channelOwner
                }
                is GameLogMessage.ServerError -> GameServerLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    severity = log.severity
                    message = log.log
                }
                is GameLogMessage.Message.Private -> PrivateMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    receiver = log.otherUsername
                    message = log.contents
                }
                is GameLogMessage.Message.Public -> PublicMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    message = log.contents
                }
                is GameLogMessage.Message.Yell -> YellMessageLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    sender = log.username
                    message = log.contents
                }
                is GameLogMessage.RareDrop -> RareDropLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    item = log.item.toItemAdminCp()
                    itemSource = log.source
                }
                is GameLogMessage.Sanction -> SanctionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    reporter = log.username
                    offender = log.otherUsername
                    kind = log.type
                    reason = log.reason
                    expiresAt = log.expiresAt?.toLocalDateTime(defaultTimeZone)
                }
                is GameLogMessage.Trade -> TradeLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    user1 = log.username
                    user2 = log.otherUsername
                    items1 = log.items.toSlotItemMapAdminCp()
                    items2 = log.otherItems.toSlotItemMapAdminCp()
                    location = log.location.toLocationAdminCp()
                }
                is GameLogMessage.PrimalExchange -> PrimalExchangeLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    itemId = log.item.id
                    amount = log.item.amount
                    value = log.value
                }
                is GameLogMessage.ShopTransaction.Sell -> ShopTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    shopName = log.shopName
                    txType = 'S'
                    item = log.item.toItemAdminCp()
                    price = log.priceEach
                    currencyName = log.currencyName
                }
                is GameLogMessage.ShopTransaction.Purchase -> ShopTransactionLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    shopName = log.shopName
                    txType = 'B'
                    item = log.item.toItemAdminCp()
                    price = log.priceEach
                    currencyName = log.currencyName
                }
                is GameLogMessage.CollectionLogClaim -> CollectionLogClaimLogEntity.new {
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    logName = log.logName
                }
                is GameLogMessage.RaidCompletion -> RaidCompletion.new {
                    clearTime = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    raidNumber = log.raidNumber
                }
                is GameLogMessage.PetChuck -> PetChuckEntity.new{
                    time = log.time.toLocalDateTime(defaultTimeZone)
                    username = log.username
                    petName = log.petName
                    success = log.success
                }
            }
        }
    }

    private fun toDeathResultAdminCp(log: GameLogMessage.Death) = DeathResult(
        inventory = log.inventory.toSlotItemMapAdminCp(),
        equipment = log.equipment.toSlotItemMapAdminCp(),
        lootingBag = log.lootingBag?.toSlotItemMapAdminCp() ?: emptyMap(),
        runePouch1 = log.runePouch?.toSlotItemMapAdminCp() ?: emptyMap(),
        runePouch2 = log.secondaryRunePouch?.toSlotItemMapAdminCp() ?: emptyMap(),
        kept = log.kept.toSlotItemMapAdminCp(),
        lost = log.lost.toSlotItemMapAdminCp(),
        lostToKiller = if (log is GameLogMessage.Death.Killed) log.lostToKiller.map { it.toItemAdminCp() } else emptyList(),
        grave = log.graveStone.map { it.toItemAdminCp() },
    )

    private fun SlotItemMap.toSlotItemMapAdminCp(): com.near_reality.api.model.SlotItemMap {
        return buildMap {
            this@toSlotItemMapAdminCp.forEach { (slot, item) -> put(slot, item.toItemAdminCp()) }
        }
    }

    private fun _Item.toItemAdminCp(): Item {
        return Item(
            id = id,
            amount = amount,
        )
    }

    private fun _Location.toLocationAdminCp(): Location {
        return Location(
            hash = positionHash
        )
    }

    private fun ExchangeType.toExchangeTypeAdminCp(): GrandExchangeOfferType {
        return when (this) {
            ExchangeType.BUYING -> GrandExchangeOfferType.BUY
            ExchangeType.SELLING -> GrandExchangeOfferType.SELL
        }
    }


    suspend fun validateUserLogin(request: LoginPacketIn, beta: Boolean): UserLoginResponse = dbQueryMain {
        val username = request.username
        val password = request.password

        val authType = request.authInfo?.type ?: AuthType.NORMAL
        val authCode = when(authType) {
            AuthType.NORMAL -> 0
            AuthType.TRUSTED_AUTHENTICATION,
            AuthType.UNTRUSTED_AUTHENTICATION -> request.authInfo!!.code
            AuthType.TRUSTED_COMPUTER -> request.authInfo!!.identifier
        }


        /* We have a valid user */
        val userEntity = UserEntity.findByUsername(username) ?: return@dbQueryMain UserLoginResponse.UserNotExist

        /* We have a valid password */
        if (!BCrypt.checkpw(password, userEntity.password)) return@dbQueryMain UserLoginResponse.InvalidPassword

        /* They have permission to login if beta */
        if (beta && !userEntity.isAuthorizedForBeta()) return@dbQueryMain UserLoginResponse.NotAuthorizedForBeta

        /* MFA is enabled globally, the user has it enabled, and they are logging in without a trust established */
        if (MFAManager.ENABLED && userEntity.hasValidMFA() && authType == AuthType.NORMAL)
            return@dbQueryMain  UserLoginResponse.MFARequired

        if (MFAManager.ENABLED && userEntity.hasValidMFA() && authType != AuthType.NORMAL)
            return@dbQueryMain UserLoginResponse.MFAValidationRequired(userEntity.toApiModel(false), authType.id, userEntity.twoFactorSecret!!, authCode)

        return@dbQueryMain UserLoginResponse.Success(userEntity.toApiModel(false))
    }

    suspend fun getCreditPurchaseSinceRelaunch(): Map<Long, Int> = dbQueryMain {
        val cutoff = Instant.parse("2025-07-24T00:00:00Z")

        val query = CreditPackageOrders
            .join(CreditPackages, JoinType.INNER, onColumn = CreditPackageOrders.product, otherColumn = CreditPackages.id)
            .select(CreditPackageOrders.columns + CreditPackages.columns)
            .where{ CreditPackageOrders.time greaterEq cutoff and (CreditPackageOrders.status eq CreditPackageOrder.Status.PAID)}

        val daoList: List<CreditPackageOrderEntity> =
            CreditPackageOrderEntity.wrapRows(query)
                .with(CreditPackageOrderEntity::user, CreditPackageOrderEntity::product)
                .toList()

        val byUserId = daoList.groupBy { it.user.id.value }
            .mapValues { (_, list) -> list.sumOf { Math.round(it.cost) } }

        println("Database: ${daoList.size} user orders parsed.")
        byUserId
    }
}
