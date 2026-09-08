package com.zenyte.game.content.skills.hunter.node;

import com.zenyte.game.world.object.WorldObject;

public record NetTrapPair(WorldObject net, WorldObject tree) {

    @Override
    public String toString() {
        return "NetTrapPair(net=" + this.net() + ", tree=" + this.tree() + ")";
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof NetTrapPair)) return false;
        final NetTrapPair other = (NetTrapPair) o;
        final Object this$net = this.net();
        final Object other$net = other.net();
        if (this$net == null ? other$net != null : !this$net.equals(other$net)) return false;
        final Object this$tree = this.tree();
        final Object other$tree = other.tree();
        return this$tree == null ? other$tree == null : this$tree.equals(other$tree);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $net = this.net();
        result = result * PRIME + ($net == null ? 43 : $net.hashCode());
        final Object $tree = this.tree();
        result = result * PRIME + ($tree == null ? 43 : $tree.hashCode());
        return result;
    }
}
