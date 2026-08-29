package com.zenyte.game.content.breaches;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class BreachLoot extends DropProcessor {

    /*
    Reward 15 killers with loot from breach NPC
    Reward MVP loot
     */

    public static void generateLoot(BreachEntity entity, Player MVP) {
        MVP = World.getPlayerByUsername(MVP.getUsername());
        if (MVP != null) { // check MVP is still online
            MVP.sendMessage(Colour.RED.wrap("You were the MVP for killing the " + entity.getDefinitions().getName() + "."));
            generateLoot(entity, MVP, BreachSettings.MVP_REWARD_ITEMS, true);
        }
        /*
        Credit top 15 damage dealers a resource drop
         */
        List<Player> killers = getTopKillersList(entity.getDamageMap());
        if (killers.isEmpty())
            return;
        int credited = 0;
        for (Player killer : killers) {
            if (killer == null || World.getPlayerByUsername(killer.getUsername()) == null)
                continue;
            if (killer.getUsername().equals(MVP.getUsername()))
                continue;
            if (credited < BreachSettings.CREDIT_KILLER_AMOUNT) {
                entity.dropBreachLoot(entity.getLocation(), killer);
                killer.sendMessage("You were credited a drop for your damage to "
                    + entity.getDefinitions().getName() + ".");
                credited++;
            }
        }
    }

    /*
    Gets top 15 killers based on map damage
     */
    private static List<Player> getTopKillersList(HashMap<String, Integer> damageMap) {
        var killers = new ArrayList<Player>();
        var sortedMap = sortByComparator(damageMap);
        sortedMap.forEach((key, value) -> {
            var killer = World.getPlayerByUsername(key);
            if (killer == null) return;
            if (value == 0) return;
            killers.add(killer);
        });
        return killers;
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list.stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    /*
    MVP LOOT
     */
    public static void generateLoot(BreachEntity entity, Player player, int[][] dropTable, boolean MVP) {
        for (int[] dropArray : dropTable) {
            int id = dropArray[0];
            int weight = dropArray[3];
            if (Utils.random(weight) == 1) {
                var drop = new Item(id, MVP ? 1 : Utils.random(dropArray[1], dropArray[2]));
                entity.dropItem(player, drop);
                if (MVP) {
                    player.sendMessage("You were rewarded a %s as the MVP for killing %s."
                        .formatted(
                            Colour.RED.wrap(drop.getDefinitions().getName()),
                            Colour.RED.wrap(entity.getDefinitions().getName())
                        )
                    );
                    return; // only return if MVP so that they only get 1 MVP drop
                }
            }
        }
    }

    /*
    Generate credited killers their loot
     */

    @Override
    public void onDeath(NPC npc, Player killer) {
        super.onDeath(npc, killer);
        if (npc instanceof BreachEntity)
            generateLoot((BreachEntity) npc, killer, BreachSettings.CREDIT_REWARD_ITEMS, false);
    }

    /*
    appends from array[] in BreachSettings
     */

    @Override
    public void attach() {
        for (int[] drop : BreachSettings.MVP_REWARD_ITEMS) {
            appendDrop(new DisplayedDrop(drop[0], drop[1], drop[2], drop[3]));
        }
        for (int[] drop : BreachSettings.CREDIT_REWARD_ITEMS) {
            appendDrop(new DisplayedDrop(drop[0], drop[1], drop[2], drop[3]));
        }
    }

    @Override
    public int[] ids() {
        return BreachSettings.BREACH_NPCS;
    }
}
