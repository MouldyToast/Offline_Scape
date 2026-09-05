package com.zenyte.game.content.clans;

import com.google.gson.annotations.Expose;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Kris | 22. march 2018 : 23:40.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ClanChannel {
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClanChannel.class);

	/**
	 * The owner of the clan channel, this variable can never change.
	 */
	@Expose
	private final String owner;
	/**
	 * A map of ranked members' usernames and their respective ranks.
	 */
	@Expose
	private final Map<String, ClanRank> rankedMembers = new HashMap<>(25);
	/**
	 * Whether the clan is currently disabled or not.
	 */
	@Expose
	private boolean disabled;
	/**
	 * The prefix AKA name of the clan channel.
	 */
	@Expose
	private String prefix;
	/**
	 * The rank requirments for entering, talking and kicking.
	 */
	@Expose
	private ClanRank enterRank;
	@Expose
	private ClanRank talkRank;
	@Expose
	private ClanRank kickRank;
	/**
	 * A set of players who are currently in this clan channel.
	 */
	private transient Object2LongOpenHashMap<String> bannedMembers = new Object2LongOpenHashMap<>();
	private Set<String> permBannedMembers = new ObjectOpenHashSet<>();
	private transient Set<Player> members = new ObjectOpenHashSet<>(25);

	public ClanChannel(final String owner) {
		this.owner = owner;
		disabled = true;
		enterRank = ClanRank.ANYONE;
		talkRank = ClanRank.ANYONE;
		kickRank = ClanRank.OWNER;
	}

	@Listener(type = ListenerType.LOGOUT)
	public static void onLogout(final Player player) {
		final ClanChannel channel = player.getSettings().getChannel();
		if (channel == null) {
			return;
		}
		channel.members.remove(player);
	}

	public void setTransientVariables() {
		members = new ObjectOpenHashSet<>(25);
		bannedMembers = new Object2LongOpenHashMap<>();
	}

	/**
	 * The raid party of the clan.
	 */
	private transient RaidParty raidParty;

	/**
	 * Resolves the owner as a Player and passes them to the consumer.
	 * Checks online players first, falls back to the save file.
	 * If neither is available, passes {@code null} — callers must
	 * handle a null owner gracefully.
	 *
	 * @param consumer the consumer that accepts the owner, or null.
	 */
	void loadOwner(@NotNull final Consumer<Player> consumer) {
		final Optional<Player> online = World.getPlayer(owner);
		if (online.isPresent() && !online.get().isNulled()) {
			consumer.accept(online.get());
			return;
		}
		CoresManager.getLoginManager().load(true, owner, optionalPlayer -> {
			if (optionalPlayer.isPresent()) {
				consumer.accept(optionalPlayer.get());
			} else {
				log.debug("Clan channel owner '{}' not online and no save file — passing null.", owner);
				consumer.accept(null);
			}
		});
	}

	public Set<String> getPermBannedMembers() {
		if (permBannedMembers == null) {
			permBannedMembers = new ObjectOpenHashSet<>();
		}
		return permBannedMembers;
	}

	public String getOwner() {
		return owner;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(final boolean value) {
		this.disabled = value;
		if (value) {
			if (!bannedMembers.isEmpty()) {
				bannedMembers.clear();
			}
		}
	}

	public ClanRank getEnterRank() {
		return enterRank;
	}

	public void setEnterRank(ClanRank enterRank) {
		this.enterRank = enterRank;
	}

	public ClanRank getTalkRank() {
		return talkRank;
	}

	public void setTalkRank(ClanRank talkRank) {
		this.talkRank = talkRank;
	}

	public ClanRank getKickRank() {
		return kickRank;
	}

	public void setKickRank(ClanRank kickRank) {
		this.kickRank = kickRank;
	}

	public Map<String, ClanRank> getRankedMembers() {
		return rankedMembers;
	}

	public Object2LongOpenHashMap<String> getBannedMembers() {
		return bannedMembers;
	}

	public Set<Player> getMembers() {
		return members;
	}

	public RaidParty getRaidParty() {
		return raidParty;
	}

	public void setRaidParty(RaidParty raidParty) {
		this.raidParty = raidParty;
	}
}
