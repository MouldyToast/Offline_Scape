package com.zenyte.game.content.flowerpoker;

import com.google.common.collect.ImmutableSet;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.RSPolygon;

public enum FlowerPokerAreas {
    AREA_1(3097, 3449, 3097, 3450, Direction.EAST),
    AREA_2(3097, 3453, 3097, 3454, Direction.EAST),
    AREA_3(3097, 3457, 3097, 3458, Direction.EAST),

    AREA_4(3082, 3449, 3082, 3450, Direction.WEST),
    AREA_5(3082, 3453, 3082, 3454, Direction.WEST),
    AREA_6(3082, 3457, 3082, 3458, Direction.WEST);
    private final Location one;

    private final Location two;

    private final Direction direction;

    FlowerPokerAreas(int x, int y, int x1, int y1, Direction direction) {
        this.one = new Location(x, y, 0);
        this.two = new Location(x1, y1, 0);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Location getOne() {
        return one;
    }

    public Location getTwo() {
        return two;
    }

    public static final ImmutableSet<FlowerPokerAreas> VALUES = ImmutableSet.copyOf(values());

    public static void resetArea(Location location) {
        for(FlowerPokerAreas area: FlowerPokerAreas.VALUES) {
            if(new RSPolygon(area.getOne(), area.getTwo()).contains(location)) {
                FlowerPokerSession session = FlowerPokerManager.FLOWER_POKER_AREAS.get(area);
                if(session == null)
                    return;

                session.removeFlowers();
                FlowerPokerManager.FLOWER_POKER_AREAS.put(area, null);
            }
        }
    }

    public static boolean correctPosition(Player player, Player other) {
        if(player.getPosition().equals(other.getPosition())) {
            player.sendMessage("Both players must stand next to each other at the entrance of the arena.");
            return false;
        }

        boolean p1 = false;
        boolean p2 = false;

        for(FlowerPokerAreas area : VALUES) {

            if(player.getPosition().equals(area.one) || player.getPosition().equals(area.two)) {
                p1 = true;
            }

            if(other.getPosition().equals(area.one) || other.getPosition().equals(area.two)) {
                p2 = true;
            }
        }

        if(!p1) {
            player.sendMessage("You must be standing at one of the two start positions at the arena!");
            return false;
        }

        if(!p2) {
            player.sendMessage("The other player must be standing at one of the two start positions at the arena!");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "FlowerPokerAreas{" +
                "one=" + one +
                ", two=" + two +
                '}';
    }
}