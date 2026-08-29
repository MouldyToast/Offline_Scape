package com.zenyte.game.world.entity.player;

import com.google.gson.annotations.Expose;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.world.World;
import com.zenyte.utils.TextUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.utilities.StringFormatUtil;
import net.rsprot.protocol.game.outgoing.social.UpdateFriendList;
import net.rsprot.protocol.game.outgoing.social.UpdateIgnoreList;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tommeh | 2 dec. 2017 : 22:56:04
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class SocialManager {
    private static final int MAX_FRIENDS_COUNT = 200;
    private static final int MAX_IGNORES_COUNT = 100;
    private static final AtomicInteger messageCounter = new AtomicInteger((int) System.currentTimeMillis());
    private final transient Player player;
    @Expose
    private List<String> friends;
    @Expose
    private List<String> ignores;

    SocialManager(final Player player) {
        this.player = player;
        friends = new ObjectArrayList<>();
        ignores = new ObjectArrayList<>();
    }

    public final void initalize(final SocialManager manager) {
        friends.clear();
        friends.addAll(manager.friends.stream().map(TextUtils::formatNameForProtocol).toList());
        ignores.clear();
        ignores.addAll(manager.ignores.stream().map(TextUtils::formatNameForProtocol).toList());
    }

    void loadFriends() {
        player.getPacketDispatcher().initFriendsList();
    }

    void loadIgnores() {
        player.getPacketDispatcher().initIgnoreList();
    }

    public boolean isOffline() {
        return player.getTemporaryAttributes().containsKey("private_status_offline");
    }

    public static String formatName(String name) {
        return TextUtils.formatNameForProtocol(name);
    }

    public static String formatName(Player player) {
        return formatName(player.getPlayerInformation().getUsername());
    }

    public void updateStatus() {
        final SocialManager.PrivateStatus privateStatus =
                PrivateStatus.fromId(player.getSettings().valueOf(Setting.PRIVATE_FILTER));
        if (PrivateStatus.OFF.equals(privateStatus)) {
            player.putBooleanTemporaryAttribute("private_status_offline", true);
        } else {
            player.getTemporaryAttributes().remove("private_status_offline");
        }
        final String username = formatName(player);
        var displayName = username;
        for (final Player p : World.getPlayers()) {
            if (p == null || p == player) continue;
            if (p.getSocialManager().containsFriend(username)) {
                int world = 0;
                if (!player.isFinished() && player.getSocialManager().isVisible(player)) {
                    world = GameConstants.WORLD_PROFILE.getNumber();
                }
                displayName = player.getPlayerInformation().getDisplayname();
                UpdateFriendList.Friend entry;
                if (world == 0) {
                    entry = new UpdateFriendList.OfflineFriend(false, displayName, null, 0, 0, "");
                } else {
                    entry = new UpdateFriendList.OnlineFriend(false, displayName, null, world, 0, 0, "", GameConstants.WORLD_PROFILE.getActivity(), 0, 0);
                }
                p.getPacketDispatcher().updateFriendStatus(entry);
            }
        }
    }

    public void addFriend(@NotNull String name) {
        @NotNull final String finalName = formatName(name);
        if (finalName.equalsIgnoreCase(TextUtils.formatNameForProtocol(player.getUsername()))) {
            player.sendMessage("You can't add yourself to your own friends list.");
            return;
        }
        CoresManager.getLoginManager().load(true, finalName, optional -> {
            if (optional.isEmpty()) {
                player.sendMessage("Could not find player.");
                return;
            }
            final Player target = optional.get();
            if (friends.size() >= MAX_FRIENDS_COUNT) {
                player.sendMessage("Your friend list is currently full.");
                return;
            }
            if (friends.contains(finalName)) {
                player.sendMessage(target.getName() + " is already on your friend list.");
                return;
            }
            friends.add(finalName);
            refreshForSinglePlayer(player, finalName, true);
            final Optional<Player> loggedInPlayer = World.getPlayer(finalName);
            loggedInPlayer.ifPresent(otherPlayer -> {
                refreshForSinglePlayer(otherPlayer, player.getUsername(), false);
                final Optional<ClanChannel> opChannel = ClanManager.getChannel(player.getUsername());
                opChannel.ifPresent(channel -> {
                    if (channel.getMembers().contains(otherPlayer)) {
                        ClanManager.refreshPartial(channel, otherPlayer, true, false);
                    }
                });
            });
        });
    }

    public boolean isVisible(final String username) {
        return World.getPlayer(username).filter(this::isVisible).isPresent();
    }

    public boolean isVisible(final Player otherPlayer) {
        if (otherPlayer == null) {
            return false;
        }
        final SocialManager.PrivateStatus privateStatus =
                PrivateStatus.fromId(otherPlayer.getSettings().valueOf(Setting.PRIVATE_FILTER));
        if (privateStatus.equals(PrivateStatus.OFF)) {
            return false;
        } else return !privateStatus.equals(PrivateStatus.FRIENDS)
                || otherPlayer.getSocialManager().containsFriend(formatName(player));
    }

    public PrivateStatus getStatus() {
        return PrivateStatus.fromId(player.getSettings().valueOf(Setting.PRIVATE_FILTER));
    }

    private void refreshForSinglePlayer(final Player playerToRefresh, String userToRefresh, final boolean added) {
        userToRefresh = formatName(userToRefresh);
        if (!playerToRefresh.getSocialManager().containsFriend(userToRefresh)) {
            return;
        }
        int world = 0;
        final Optional<Player> friend = World.getPlayer(userToRefresh);
        var displayName = userToRefresh;
        if (friend.isPresent()) {
            Player friendPlayer = friend.get();
            displayName = friendPlayer.getPlayerInformation().getDisplayname();
            if (!friendPlayer.isFinished() && player.getSocialManager().isVisible(friendPlayer)) {
                world = GameConstants.WORLD_PROFILE.getNumber();
            }
        }
        UpdateFriendList.Friend entry;
        if (world == 0) {
            entry = new UpdateFriendList.OfflineFriend(added, displayName, null, 0, 0, "");
        } else {
            entry = new UpdateFriendList.OnlineFriend(added, displayName, null, world, 0, 0, "", "", 0, 0);
        }
        playerToRefresh.getPacketDispatcher().updateFriendStatus(entry);
    }

    public void removeFriend(@NotNull final String requestedName) {
        final String name = formatName(requestedName);
        if (!friends.remove(name)) {
            return;
        }
        World.getPlayer(name).ifPresent(target -> {
            refreshForSinglePlayer(target, player.getUsername(), false);
            ClanManager.getChannel(player.getUsername()).ifPresent(channel -> {
                if (channel.getMembers().contains(target)) {
                    ClanManager.refreshPartial(channel, target, true, false);
                }
            });
        });
    }

    public void addIgnore(@NotNull final String requestedName) {
        if (ignores.size() >= MAX_IGNORES_COUNT) {
            player.sendMessage("Your ignore list is currently full.");
            return;
        }
        final String name = formatName(requestedName);
        if (containsFriend(name)) {
            player.sendMessage("Remove them from your friend list first!");
            return;
        }
        if (containsIgnore(name)) {
            player.sendMessage(StringFormatUtil.formatString(name) + " is already on your ignore list.");
            return;
        }
        CoresManager.getLoginManager().load(true, name, optional -> {
            if (optional.isEmpty()) {
                player.sendMessage("Player could not be found.");
                return;
            }
            final Player target = optional.get();
            ignores.add(name);

            UpdateIgnoreList.IgnoredPlayer ignoredPlayer = new UpdateIgnoreList.AddedIgnoredEntry(formatName(target), null, "", true);
            player.getPacketDispatcher().updateIgnoreStatus(ignoredPlayer);
        });
    }

    public void removeIgnore(@NotNull final String requestedName) {
        ignores.remove(formatName(requestedName));
    }

    public void sendMessage(@NotNull final String name, @NotNull final String message) {
        final Optional<Player> friend = World.getPlayer(name);
        if (friend.isPresent() && isVisible(friend.get())) {
            if (isOffline()) {
                updateStatus(); // if the client sets private to Friends from Off
            }
            //final int icon = player.getRankIcon();
            final Player friendPlayer = friend.get();
            player.getPacketDispatcher().messagePrivateEcho(friendPlayer.getPlayerInformation().getDisplayname(), message);
            friendPlayer.getPacketDispatcher().messagePrivate(player.getTitleName(),
                    1,
                    getNextUniqueId(),
                    0,
                    message);
        } else {
            player.sendMessage("That player is currently offline.");
        }
    }

    public boolean containsFriend(final String name) {
        return friends.contains(name);
    }

    boolean containsIgnore(final String name) {
        return ignores.contains(name);
    }

    public int getNextUniqueId() {
        return messageCounter.getAndIncrement();
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getIgnores() {
        return ignores;
    }

    public void setIgnores(List<String> ignores) {
        this.ignores = ignores;
    }

    public enum PrivateStatus {
        ALL(0),
        FRIENDS(1),
        OFF(2);
        private static final PrivateStatus[] values = values();
        private static final Int2ObjectOpenHashMap<PrivateStatus> byIds = new Int2ObjectOpenHashMap<>(values.length);

        static {
            for (PrivateStatus status : values) {
                byIds.put(status.getId(), status);
            }
        }

        private final int id;

        public static PrivateStatus fromId(final int option) {
            return byIds.get(option);
        }

        PrivateStatus(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
