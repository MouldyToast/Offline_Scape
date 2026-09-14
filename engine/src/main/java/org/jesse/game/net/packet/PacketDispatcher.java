package org.jesse.game.net.packet;

import cloud.rsps.worlds.WorldSwitchTarget;
import org.jesse.game.GameConstants;
import org.jesse.game.GameInterface;
import org.jesse.game.content.grandexchange.ExchangeOffer;
import org.jesse.game.item.Item;
import org.jesse.game.model.CameraShakeType;
import org.jesse.game.model.HintArrow;
import org.jesse.game.model.HintArrowPosition;
import org.jesse.game.model.LineSpacingType;
import org.jesse.game.model.MinimapState;
import org.jesse.game.model.*;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.packet.PacketSender;
import org.jesse.game.util.AccessMask;
import org.jesse.game.util.MaskBuilder;
import org.jesse.game.util.RSColour;
import org.jesse.game.world.Position;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.LogLevel;
import org.jesse.game.world.entity.player.LogoutType;
import org.jesse.game.world.entity.player.MessageType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.Setting;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.ItemContainer;
import org.jesse.game.world.entity.player.container.impl.ContainerType;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.zone.ZoneManager;
import io.netty.buffer.ByteBuf;
import mgi.types.component.ComponentDefinitions;
import net.rsprot.protocol.game.outgoing.camera.util.CameraEaseFunction;
import net.rsprot.protocol.game.outgoing.clan.ClanChannelFull;
import net.rsprot.protocol.game.outgoing.clan.ClanSettingsFull;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelFull;
import net.rsprot.protocol.game.outgoing.friendchat.UpdateFriendChatChannelSingleUser;
import net.rsprot.protocol.game.outgoing.misc.player.UpdateStockMarketSlot;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A utility class for sending packets.
 *
 * @author Graham Edgecombe
 * @author Tom - modifications and additions.
 * @author Jire
 */
public final class PacketDispatcher {

    /**
     * The private player object used for creating the constructor.
     */
    private final Player player;

    private final PacketSender sender;

    public static final Logger log = LoggerFactory.getLogger(PacketDispatcher.class);

    /**
     * Creates an action sender for the specified player.
     *
     * @param player
     *            The player to create the action sender for.
     */
    public PacketDispatcher(final Player player) {
        this.player = player;
        sender = new PacketSender(player);
    }

    public void sendLogout(final LogoutType logoutType) {
        player.log(LogLevel.INFO, "'" + player.getName() + "' sendLogout call (index: " + player.getIndex() + ").");

        final int reason = logoutType.getLogoutReasonType();
        if (reason == LogoutType.NO_LOGOUT_REASON_TYPE) {
            @Nullable final WorldSwitchTarget worldSwitchTarget = player.getWorldSwitchTarget();
            if (worldSwitchTarget == null) {
                sender.logout();
            } else {
                sender.logoutTransfer(
                        worldSwitchTarget.getHost(),
                        worldSwitchTarget.getId(),
                        worldSwitchTarget.getSettings()
                );
            }
        } else {
            sender.logoutWithReason(reason);
        }
    }

    public void sendURL(final String link) {
        sender.urlOpen(link);
    }

    public void sendPane(final PaneType pane) {
        ifOpenTop(pane.getId());
        player.getInterfaceHandler().setPane(pane);
    }

    public void ifOpenTop(int id) {
        sender.ifOpenTop(id);
    }


    public void sendProjectile(final Position sender, final Position receiver, final Projectile projectile, final int speed, final int offset) {
        ZoneManager.INSTANCE.mapProjAnim(
                sender.getPosition().getChunkHash(),
                projectile.getGraphicsId(),
                projectile.getStartHeight() / 4,
                projectile.getEndHeight() / 4,
                projectile.getDelay(),
                speed,
                projectile.getAngle(),
                offset,
                -1,
                receiver instanceof Entity e ? e.getProjectileIndex() : -1,
                sender.getPosition().getX(),
                sender.getPosition().getY(),
                receiver.getPosition().getX() & 0x7,
                receiver.getPosition().getY() & 0x7
        );
    }

    public void sendInterface(final int interfaceId, final int targetChild, final PaneType pane, final boolean walkable) {
        sender.ifOpenSub(pane.getId(), targetChild, interfaceId, walkable ? 1 : 0);
    }

    public void sendMoveInterface(final int fromPane, final int fromChild, final int toPane, final int toChild) {
        sender.ifMoveSub(fromPane, fromChild, toPane, toChild);
    }

    public void closeInterface(final int hash) {
        sender.ifCloseSub(hash >> 16, hash & 0xFFFF);
    }

    public void sendLineSpacing(@NotNull final LineSpacingType horizontalType, @NotNull final LineSpacingType verticalType, final int interfaceId, final int componentId) {
        sendLineSpacing(horizontalType, verticalType, 0, interfaceId, componentId);
    }

    public void sendLineSpacing(@NotNull final LineSpacingType horizontalType, @NotNull final LineSpacingType verticalType, final int lineSpacing, final int interfaceId, final int componentId) {
        sender.runClientScript(600, List.of(horizontalType.ordinal(), verticalType.ordinal(), lineSpacing, (interfaceId << 16) | componentId));
    }

    public void sendMessage(final String message, final MessageType type, final String extension) {
        if (message == null || message.isEmpty()) {
            return;
        }
        sender.messageGame(type.getType(), extension, message);
    }

    public void sendGameMessage(final String message, final MessageType type) {
        sendMessage(message, type, null);
    }

    public void sendGameMessage(final String message, final boolean filterable) {
        sendMessage(message, filterable ? MessageType.FILTERABLE : MessageType.UNFILTERABLE, null);
    }

    public void sendGameMessage(final String message, final boolean filterable, final Object... params) {
        sendMessage(params.length > 0 ? String.format(message, params) : message, filterable ? MessageType.FILTERABLE : MessageType.UNFILTERABLE, null);
    }

    public void sendTradeRequest(final String message, final String user) {
        sendMessage(message, MessageType.TRADE_REQUEST, user);
    }

    public void sendChallengeRequest(final String message, final String user) {
        sendMessage(message, MessageType.CHALLENGE_REQUEST, user);
    }

    public void sendGlobalBroadcast(final String message) {
        sendMessage(message, MessageType.GLOBAL_BROADCAST, null);
    }

    public void sendRunEnergy() {
        sender.updateRunEnergy((int) player.getVariables().getRunEnergy() * 100);
    }

    public void sendWeight() {
        sender.updateRunWeight((int) (player.getInventory().getWeight() + player.getEquipment().getWeight()));
    }

    public void sendSkillUpdate(final int skill) {
        int level = Math.min(255, player.getSkills().getLevel(skill));
        sender.updateStat(skill, level, level, (int) player.getSkills().getExperience(skill));
    }

    public void sendSkillUpdateForce(final int skill, int level) {
        sender.updateStat(skill, level, level, (int) player.getSkills().getExperience(skill));
    }

    public void freecam(boolean enabled) {
        sender.camMode(enabled);
    }

    public void rebootTimer(int cycles) {
        sender.updateRebootTimer(cycles);
    }

    public void heatmap(boolean enabled) {
        sender.setHeatmapEnabled(enabled);
    }

    public void updateUID192(byte[] uid) {
        sender.updateUid192(uid);
    }

    public void chatFilterSettings() {
        int trade = player.getNumericAttribute(Setting.TRADE_FILTER.toString()).intValue();
        int pub = player.getNumericAttribute(Setting.PUBLIC_FILTER.toString()).intValue();
        sender.chatFilterSettings(pub, trade);
    }

    public void privateChatFilter() {
        int priv = player.getNumericAttribute(Setting.PRIVATE_FILTER.toString()).intValue();
        sender.chatFilterSettingsPrivateChat(priv);
    }

    public void messagePrivate(String sender, int worldId, int worldMessageCounter, int chatCrownType, String message) {
        this.sender.messagePrivate(sender, worldId, worldMessageCounter, chatCrownType, message);
    }

    public void messagePrivateEcho(String recipient, String message) {
        sender.messagePrivateEcho(recipient, message);
    }

    public void updateFriendStatus(UpdateFriendList.Friend friend) {
        sender.updateFriendList$engine(List.of(friend));
    }

    public void updateIgnoreStatus(UpdateIgnoreList.IgnoredPlayer ignore) {
        sender.updateIgnoreList$engine(List.of(ignore));
    }

    public void initFriendsList() {
        final List<String> friends = player.getSocialManager().getFriends();
        final ArrayList<UpdateFriendList.Friend> list = new ArrayList<>(friends.size());
        for (final String username : friends) {
            final Optional<Player> friend = World.getPlayer(username);
            var displayName = username;
            int world = 0;
            if (friend.isPresent()) {
                Player friendPlayer = friend.get();
                displayName = friendPlayer.getPlayerInformation().getDisplayname();
                if (!friendPlayer.isFinished() && player.getSocialManager().isVisible(friendPlayer)) {
                    world = GameConstants.WORLD_PROFILE.getNumber();
                }
            }
            if (world == 0) {
                list.add(new UpdateFriendList.OfflineFriend(false, displayName, null, 0, 0, ""));
            } else {
                list.add(new UpdateFriendList.OnlineFriend(false, displayName, null, world, 0, 0, "", "", 0, 0));
            }
        }
        sender.updateFriendList$engine(list);
    }

    public void initIgnoreList() {
        final List<String> ignores = player.getSocialManager().getIgnores();
        final ArrayList<UpdateIgnoreList.IgnoredPlayer> list = new ArrayList<>(ignores.size());
        for (final String ignore : ignores) {
            list.add(new UpdateIgnoreList.AddedIgnoredEntry(ignore, null, "", false));
        }
        sender.updateIgnoreList$engine(list);
    }

    public void sendStaticMapRegion() {
        sender.rebuildNormal(player.getLocation().getChunkX(), player.getLocation().getChunkY(), -1);
    }

    public void sendDynamicMapRegion() {
        sender.rebuildRegion$engine(player.getLocation().getChunkX(), player.getLocation().getChunkY(), true);
    }

    public void sendComponentVisibility(final int interfaceId, final int componentId, final boolean hidden) {
        sender.ifSetHideHidden(interfaceId, componentId, hidden);
    }

    public void sendComponentVisibility(final GameInterface inter, final int componentId, final boolean hidden) {
        sender.ifSetHideHidden(inter.getId(), componentId, hidden);
    }

    public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final int set) {
        sender.ifSetEvents(interfaceId, componentId, start, end, set);
    }

    public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final AccessMask... masks) {
        sender.ifSetEvents(interfaceId, componentId, start, end, MaskBuilder.getValue(masks));
    }

    public void sendComponentSettings(final GameInterface gameInterface, final int componentId, final int start, final int end, final AccessMask... masks) {
        sender.ifSetEvents(gameInterface.getId(), componentId, start, end, MaskBuilder.getValue(masks));
    }

    public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final MaskBuilder builder) {
        sender.ifSetEvents(interfaceId, componentId, start, end, builder.getValue());
    }

    public void sendComponentPosition(int interfaceId, int componentId, int x, int y) {
        sender.ifSetPosition(interfaceId, componentId, x, y);
    }

    public void sendComponentText(final int interfaceId, final int componentId, final Object text) {
        if (!ComponentDefinitions.containsInterface(interfaceId)) {
            return;
        }
        sender.ifSetText(interfaceId, componentId, Objects.toString(text));
    }

    public void sendComponentText(final GameInterface gameInterface, final int componentId, final Object text) {
        sender.ifSetText(gameInterface.getId(), componentId, Objects.toString(text));
    }

    public void sendComponentItem(GameInterface gameInterface, final int componentId, final int itemId, final int zoom) {
        sendComponentItem(gameInterface.getId(), componentId, itemId, zoom);
    }

    public void sendComponentItem(final int interfaceId, final int componentId, final int itemId, final int zoom) {
        sender.ifSetItem(interfaceId, componentId, itemId, zoom);
    }

    public void sendComponentPlayerHead(final int interfaceId, final int componentId) {
        sender.ifSetPlayerHead(interfaceId, componentId);
    }

    public void sendComponentNPCHead(final int interfaceId, final int componentId, final int npcId) {
        sender.ifSetNpcHead(interfaceId, componentId, npcId);
    }

    public void sendComponentModel(final GameInterface inter, final int componentId, final int modelId) {
        sender.ifSetModel(inter.getId(), componentId, modelId);
    }

    public void sendComponentModel(final int interfaceId, final int componentId, final int modelId) {
        sender.ifSetModel(interfaceId, componentId, modelId);
    }

    public void sendComponentAngle(final int interfaceId, final int componentId, final int angleX, final int angleY, final int zoom) {
        sender.ifSetAngle(interfaceId, componentId, angleX, angleY, zoom);
    }

    public void sendComponentAnimation(final int interfaceId, final int componentId, final int animationId) {
        sender.ifSetAnim(interfaceId, componentId, animationId);
    }

    public void sendComponentAnimation(final GameInterface inter, final int componentId, final int animationId) {
        sender.ifSetAnim(inter.getId(), componentId, animationId);
    }

    public void sendComponentSpriteColour(final int interfaceId, final int componentId, final RSColour colour) {
        sender.ifSetColour(interfaceId, componentId, colour.getRed(), colour.getGreen(), colour.getBlue());
    }

    public void sendComponentSpriteColour(final int interfaceId, final int componentId, final int colour) {
        sender.ifSetColour(interfaceId, componentId, colour);
    }

    public void sendClientScript(final int scriptId, final Object... arguments) {
        sender.runClientScript(scriptId, arguments);
    }

    public void sendClientScript(final int scriptId) {
        sender.runClientScript(scriptId);
    }

    public void sendConfig(final int config, final int value) {
//        System.err.println("SENDING VARP ID " + config + ", " + value);
        sender.varp(config, value);
    }
    public void sendConfigLowPriority(final int config, final int value) {
//        System.err.println("SENDING VARP ID " + config + ", " + value);
        sender.varpLowPriority(config, value);
    }
    public void sendPlayerOption(final int index, final String option, final boolean top) {
        sender.setPlayerOp(index, top, option);
    }

    public void sendHintArrow(final HintArrow arrow) {
        player.getTemporaryAttributes().put("last hint arrow", arrow);
        if (arrow == null) {
            sender.resetHintArrow();
            return;
        }
        if (HintArrowPosition.ENTITY.equals(arrow.getPosition())) {
            final Entity target = arrow.getTarget();
            if (Entity.EntityType.NPC.equals(target.getEntityType())) {
                sender.npcHintArrow(target.getIndex());
            } else {
                sender.playerHintArrow(target.getIndex());
            }
        } else {
            sender.tileHintArrow(arrow.getX(), arrow.getY(), arrow.getHeight(), arrow.getPosition());
        }
    }

    public void resetHintArrow() {
        sender.resetHintArrow();
        player.getTemporaryAttributes().remove("last hint arrow");
    }

    public void resetFriendChannel() {
        sender.leaveFriendChatChannel();
    }

    public void messageFriendChannel(
            String sender,
            String channelName,
            int worldId,
            int worldMessageCounter,
            int chatCrownType,
            String message
    ) {
        this.sender.messageFriendChannel(sender, channelName, worldId, worldMessageCounter, chatCrownType, message);
    }

    public void friendChannelFull(
            String channelOwner,
            String channelName,
            int kickRank,
            List<UpdateFriendChatChannelFull.FriendChatEntry> entries
    ) {
        sender.updateFriendChatChannelFull$engine(channelOwner, channelName.replace(' ', '_'), kickRank, entries);
    }

    public void updateFriendChatChannelSingleUser(UpdateFriendChatChannelSingleUser.FriendChatUser user) {
        sender.updateFriendChatChannelSingleUser$engine(user);
    }


    public void sendFriendServer() {
        sender.friendListLoaded$engine();
    }

    public void sendGrandExchangeOffer(final ExchangeOffer offer) {
        sender.updateStockMarketSlot$engine(
                offer.getSlot(),
                new UpdateStockMarketSlot.SetStockMarketSlot(
                        offer.getStage(),
                        offer.getItem().getId(),
                        offer.getPrice(),
                        offer.getItem().getAmount(),
                        offer.getAmount(),
                        offer.getTotalPrice()
                )
        );
    }

    public void sendUpdateItemContainer(final Container container) {
        sender.updateInvFull(container);
    }

    public void sendUpdateItemContainer(final Container container, final ContainerType type) {
        sender.updateInvFull(container, type);
    }

    public void sendUpdateItemsPartial(final Container items) {
        sender.updateInvPartial(items);
    }

    public void sendUpdateItemContainer(final int key, final ItemContainer items) {
        sendUpdateItemContainer(key, -1, 0, items);
    }

    public void sendClearItemContainer(final int interfaceId, final int componentId) {
        sender.ifClearInv(interfaceId, componentId);
    }

    public void sendUpdateItemContainer(final int key, final int interfaceId, final int componentId, final ItemContainer items) {
        sender.updateInvFull(interfaceId, componentId, key, items.getItems());
    }

    public void sendUpdateItemContainer(final int key, final int interfaceId, final int componentId, final Container items) {
        sender.updateInvFull(interfaceId, componentId, key, items.getItems().values().toArray(Item[]::new));
    }

    public void sendSoundEffect(final SoundEffect sound) {
        sender.synthSound(sound.getId(), sound.getRepetitions(), sound.getDelay());
    }

    public void sendServerTickEnd() {
        sender.serverTickEnd();
    }

    public void sendMusic(final int song) {
        sender.midiSong(song, 0, 60, 60, 0);
    }

    public void playJingle(final int song) {
        sender.midiJingle(song);
    }

    public void sendMapFlag(final int x, final int y) {
        sender.setMapFlag(x, y);
    }

    public void resetMapFlag() {
        sendMapFlag(255, 255);
    }

    public void sendCamRotateTo(final int xAngle, final int yAngle, final int cycles, final CameraEaseFunction easing) {
        sender.camRotateTo(xAngle, yAngle, cycles, easing);
    }

    public void sendCameraLook(final int viewLocalX, final int viewLocalY, final int cameraHeight, final int speed, final int acceleration) {
        sender.camLookAt(viewLocalX, viewLocalY, cameraHeight, speed, acceleration);
    }

    public void sendCameraPosition(final int viewLocalX, final int viewLocalY, final int cameraHeight, final int speed, final int acceleration) {
        sender.camMoveTo(viewLocalX, viewLocalY, cameraHeight, speed, acceleration);
    }

    public void sendCameraShake(final CameraShakeType type, final int shakeIntensity, final int movementIntensity, final int speed) {
        sender.camShake(type.getType(), shakeIntensity, movementIntensity, speed);
        player.getTemporaryAttributes().put("cameraShake", true);
    }

    public void resetCamera() {
        sender.camReset();
        player.getTemporaryAttributes().remove("cameraShake");
    }

    public void sendMinimapState(final MinimapState state) {
        sender.minimapToggle(state.getState());
    }

    public void syncClientVarCache() {
        sender.varpSync$engine();
    }


    public void updateZonePartialEnclosed(int zoneX, int zoneY, int level, ByteBuf payload) {
        sender.updateZonePartialEnclosed$engine(zoneX, zoneY, level, payload);
    }

    public void updateZoneFullFollows(int zoneX, int zoneY, int level) {
        sender.updateZoneFullFollows$engine(zoneX, zoneY, level);
    }

    public void updateZonePartialFollows(int zoneX, int zoneY, int level) {
        sender.updateZonePartialFollows$engine(zoneX, zoneY, level);
    }

    public void locDel(int xInZone, int zInZone, int shape, int rotation) {
        sender.locDel$engine(xInZone, zInZone, shape, rotation);
    }

    public void locAddChange(int id, int xInZone, int zInZone, int shape, int rotation, int opFlags) {
        sender.locAddChange$engine(id, xInZone, zInZone, shape, rotation, (byte) opFlags);
    }

    public void objAdd(int id, int quantity, int xInZone, int zInZone, int opFlags, int timeUntilPublic, int timeUntilDespawn, int ownershipType, boolean neverBecomesPublic) {
        sender.objAdd$engine(id, quantity, xInZone, zInZone, (byte) opFlags, timeUntilPublic, timeUntilDespawn, ownershipType, neverBecomesPublic);
    }

    public void objDel(int id, int quantity, int xInZone, int zInZone) {
        sender.objDel$engine(id, quantity, xInZone, zInZone);
    }

    public void objCount(int id, int oldQuantity, int newQuantity, int xInZone, int zInZone) {
        sender.objCount$engine(id, oldQuantity, newQuantity, xInZone, zInZone);
    }

    public void mapAnimSpecific(int id, int delay, int height, int xInBuildArea, int yInBuildArea) {
        sender.spotAnimSpecific$engine(id, delay, height, xInBuildArea, yInBuildArea);
    }

    public void setActiveWorld(int worldId, int level) {
        sender.setActiveWorld(worldId, level);
    }

    public void setNpcUpdateOrigin(int originX, int originY) {
        sender.setNpcUpdateOrigin$engine(originX, originY);
    }

    public void sendGraphics(final Graphics graphics, final Location location) {
        final Location lastLoaded = player.getLastLoadedMapRegionTile();
        sender.spotAnimSpecific$engine(
                graphics.getId(),
                graphics.getDelay(),
                graphics.getHeight(),
                location.getLocalX(lastLoaded),
                location.getLocalY(lastLoaded)
        );
    }

    public void sendAnimSpecific(int animation, final WorldObject obj) {
        final Location lastLoaded = player.getLastLoadedMapRegionTile();
        sender.locAnimSpecific$engine(
                animation,
                obj.getLocalX(lastLoaded),
                obj.getLocalY(lastLoaded),
                obj.getType(),
                obj.getRotation()
        );
    }

    public void sendAnimSpecific(final Animation animation, final WorldObject obj) {
        sendAnimSpecific(animation.getId(), obj);
    }

    public void playerinfo() {
        try {
            sender.playerInfo$engine(player.getPlayerInfo());
        } catch (Throwable t) {
            log.error("Player info error, destroying " + player.getIndex(), t);
            System.out.println("Player info error, destroying " + player.getIndex());
            player.logout(true);
        }
    }

    public void npcinfo() {
        try {
            sender.npcInfo$engine(player.getWorldEntityId(), player.getNpcInfo());
        } catch (Throwable t) {
            log.error("NPC info error, destroying " + player.getIndex(), t);
            System.out.println("NPC info error, destroying " + player.getIndex());
            player.logout(true);
        }
    }

    public void worldentityinfo() {
        try {
            sender.worldEntityInfo$engine(player.getWorldEntityInfo());
        } catch (Throwable t) {
            log.error("Worldentity info error, destroying " + player.getIndex(), t);
            System.out.println("Worldentity info error, destroying " + player.getIndex());
            player.logout(true);
        }
    }

    public void sendClanChannelMessage(@NotNull ChatChannelType gim, @NotNull String formatName, @NotNull String message) {
        sender.messageClanChannel(
                gim.getPacketIdentifier(),
                formatName,
                GameConstants.WORLD_PROFILE.getNumber(),
                0,
                0,
                message
        );
    }

    public void sendClanChannelUpdateFull(@NotNull ChatChannelType type, @NotNull ClanChannelFull.Update toUpdatePacket) {
        sender.clanChannelFull$engine(type.getPacketIdentifier(), toUpdatePacket);
    }

    public void sendClanChannelSettingsFull(@NotNull ChatChannelType gim, @NotNull ClanSettingsFull.Update toSettingsPacket) {
        sender.clanSettingsFull$engine(gim.getPacketIdentifier(), toSettingsPacket);
    }

    public void rebuildLogin() {
        sender.rebuildLogin$engine(player.getLocation().getChunkX(), player.getLocation().getChunkY(), -1, player.getPlayerInfo());
    }

    public void playerCamTarget() {
        sender.highPriorityPlayerCamTarget(player.getIndex());
    }

    public void syncBuildArea() {
        sender.syncBuildArea$engine();
    }

    public PacketSender getSender() {
        return sender;
    }

}
