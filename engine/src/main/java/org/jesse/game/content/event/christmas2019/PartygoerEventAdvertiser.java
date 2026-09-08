package org.jesse.game.content.event.christmas2019;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;

/**
 * @author Corey
 * @since 14/12/2019
 */
public class PartygoerEventAdvertiser extends NPC implements Spawnable {
    
    private static final ForceTalk[] messages = new ForceTalk[]{
            new ForceTalk("Merry Christmas! Merry Christmas, one and all!"),
            new ForceTalk("Good food... Fine wine... Just climb into the cupboard!"),
            new ForceTalk("The Queen of Snow invites you all to her Christmas gathering!"),
            new ForceTalk("Come one, come all, to the Fantastic Festive Feast at the Land of Snow.")
    };

    private static final Animation bellRingAnimation = new Animation(15083);
    
    private long chatDelay;
    
    public PartygoerEventAdvertiser(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
    
    @Override
    public boolean validate(int id, String name) {
        return id == ChristmasConstants.PARTYGOER_EVENT_ADVERTISER;
    }
    
    @Override
    public void processNPC() {
        super.processNPC();
        if (chatDelay < System.currentTimeMillis()) {
            chatDelay = System.currentTimeMillis() + Utils.random(8000, 14000);
            setForceTalk(messages[Utils.random(messages.length - 1)]);
            setInvalidAnimation(bellRingAnimation);
        }
    }
    
}
