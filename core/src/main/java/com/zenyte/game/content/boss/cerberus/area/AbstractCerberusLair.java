package com.zenyte.game.content.boss.cerberus.area;

/**
 * @author Tommeh | 12/06/2019 | 18:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface AbstractCerberusLair {
    default CerberusLairInstance asInstanced() {
        if(this instanceof CerberusLairInstance)
            return (CerberusLairInstance) this;
        else throw new IllegalStateException();
    }

    default StaticCerberusLair asStatic() {
        if(this instanceof StaticCerberusLair)
            return (StaticCerberusLair) this;
        else throw new IllegalStateException();
    }


}
