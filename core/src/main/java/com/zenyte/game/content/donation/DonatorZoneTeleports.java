package com.zenyte.game.content.donation;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TextUtils;

public enum DonatorZoneTeleports {

    SAPPHIRE(MemberRank.SAPPHIRE, "Sapphire Member Zone", create(new Location(1625, 2642, 0))),
    ONYX(MemberRank.ONYX, "Onyx Member Zone", create(new Location(1713, 2607, 0))),
    DIAMOND(MemberRank.DIAMOND, "Diamond Member Zone", create(new Location(1669, 2677, 0))),
    ENCHANTED(MemberRank.ENCHANTED, "Enchanted Member Zone", create(new Location(1663, 2573, 0))),
    DIE(MemberRank.EMERALD,"NR317 DIE Zone (Emerald+)", create(new Location(2340, 9882, 0))),
    OGDI(MemberRank.SAPPHIRE,"NR317 DI Zone (Sapphire+)", create(new Location(2337, 9810, 0))),
    RDI(MemberRank.DRAGONSTONE,"NR317 RDI Zone (Dragonstone+)", create(new Location(2911, 5467, 0))),
    RDI2(MemberRank.ONYX,"NR317 RDI2 Zone (Onyx+)", create(new Location(2910, 5403, 0))),
    DI(MemberRank.NONE,"Donator Island", create(new Location(1663, 2621, 0)));

    private final Location location;
    DonatorZoneTeleports(MemberRank rank, String commandName, Teleport teleport) {
        this.location = teleport.destination();
        String rankName = TextUtils.capitalizeEnum(rank.name());
        new GameCommands.Command(PlayerPrivilege.PLAYER, name().toLowerCase(), rankName+" Member Zone", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            if(p.getMemberRank().equalToOrGreaterThan(rank) || p.isStaff())
            teleport.teleport(p);
            else {
                p.sendMessage("You must be "+rankName+"+ to use this teleport.");
            }
        });
    }

    public Location getLocation() {
        return location;
    }

    @Subscribe
    public static void on(ServerLaunchEvent event) {
    }

    private static Teleport create(Location location) {
        return new Teleport() {
            @Override
            public TeleportType getType() {
                return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
            }

            @Override
            public Location destination() {
                return location;
            }

            @Override
            public int getLevel() {
                return 0;
            }

            @Override
            public double getExperience() {
                return 0;
            }

            @Override
            public int getRandomizationDistance() {
                return 3;
            }

            @Override
            public Item[] getRunes() {
                return null;
            }

            @Override
            public int getWildernessLevel() {
                return 20;
            }

            @Override
            public boolean isCombatRestricted() {
                return false;
            }
        };
    }
}
