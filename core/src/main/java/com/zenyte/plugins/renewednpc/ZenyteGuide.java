package com.zenyte.plugins.renewednpc;

import com.near_reality.api.service.user.UserPlayerHandler;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.AdventurersLogIcon;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;

import com.zenyte.game.world.entity.player.dailychallenge.challenge.DailyChallenge;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.ExpConfiguration;
import com.zenyte.game.world.entity.player.privilege.ExpConfigurations;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import kotlin.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Kris | 25/11/2018 16:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZenyteGuide extends NPCPlugin {

    public static final int NPC_ID = NpcId.NEARREALITY_GUIDE;

    public static final Item[][] STARTER_ITEMS = {
            { // normal

                    new Item(995, 50000), new Item(841), new Item(882, 250), new Item(558, 250), new Item(556, 250),
                    new Item(557, 250), new Item(555, 250), new Item(554, 250), new Item(1379), new Item(1323),
                    new Item(1171), new Item(303), new Item(362, 50), new Item(1351), new Item(1265), new Item(22711),
                    new Item(4548)},
            { // ironman
                    new Item(12810), new Item(12811), new Item(12812)
            },
            { //ult ironman
                    new Item(12813), new Item(12814), new Item(12815)},
            { //hc ironman
                    new Item(20792), new Item(20794), new Item(20796)
            }, // group ironman
            {
                    new Item(ItemId.GROUP_IRON_HELM),
                    new Item(ItemId.GROUP_IRON_PLATEBODY),
                    new Item(ItemId.GROUP_IRON_PLATELEGS),
                    new Item(ItemId.GROUP_IRON_BRACERS),
            }, // hc group ironman
            {
                    new Item(ItemId.HARDCORE_GROUP_IRON_HELM),
                    new Item(ItemId.HARDCORE_GROUP_IRON_PLATEBODY),
                    new Item(ItemId.HARDCORE_GROUP_IRON_PLATELEGS),
                    new Item(ItemId.HARDCORE_GROUP_IRON_BRACERS),
            }
    };

    public static final Location HOME_ZENYTE_GUIDE = new Location(3089, 3496);
    public static final Location SPAWN_LOCATION = new Location(3090, 3497);
    public static boolean disableJoinAnnouncement = false;

    public static void finishAppearance(final Player player) {
        player.lock();
        takeWeapon(player, ItemId.STARTER_SWORD);
        takeWeapon(player, ItemId.STARTER_BOW);
        takeWeapon(player, ItemId.STARTER_STAFF);
        finishTutorial(player);
    }

    private static void takeWeapon(@NotNull final Player player, final int weaponId) {
        final Item weapon = new Item(weaponId, 1, DegradableItem.getFullCharges(weaponId));
        player.getInventory().addOrDrop(weapon);
    }

    public static void finishTutorial(final Player player) {
        player.getAppearance().setInvisible(false);
        player.unlock();
        if (!disableJoinAnnouncement) {
            WorldBroadcasts.broadcast(player, BroadcastType.NEW_PLAYER);
        }
        final GameMode gameMode = PlayerAttributesKt.getSelectedGameMode(player);
        UserPlayerHandler.INSTANCE.updateGameMode(player, gameMode, (success) -> {
            if (!success) {
                player.log(LogLevel.ERROR, "Failed to update game-mode " + gameMode + " in API, setting anyways.");
                player.setGameMode(gameMode);
            }
            for (final Item item : STARTER_ITEMS[0]) {
                final Item it = new Item(item.getId(), item.getAmount(), DegradableItem.getDefaultCharges(item.getId(), 0));
                player.getInventory().addItem(it);
            }
            if (player.isIronman()) {
                final Item[] items = STARTER_ITEMS[player.getGameMode().ordinal()];
                for (final Item item : items) {
                    final Item it = new Item(item.getId(), item.getAmount(), DegradableItem.getDefaultCharges(item.getId(), 0));
                    player.getInventory().addItem(it);
                }
            }
            player.getTemporaryAttributes().remove("registration");
            player.putBooleanAttribute("registered", true);
            player.getVarManager().sendVar(281, 1000);
            final DailyChallenge challenge = player.getDailyChallengeManager().getRandomChallenge();
            if (challenge != null)
                player.getDailyChallengeManager().assignChallenge(challenge);
            return Unit.INSTANCE;
        });
    }

    private void sendGameModeSetup(Player player, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Before you continue your adventure, could you please select a game mode you see fit?").executeAction(() -> {
                    player.sendMessage("When you are done selecting a game mode, simply close the interface to finalise your decision.");
                    player.getTemporaryAttributes().put("ironman_setup", "register");
                    player.getVarManager().sendVar(281, 0);
                    player.getVarManager().sendBit(1777, 0);
                    player.setExperienceMultiplier(150, 80);
                    GameInterface.GAME_MODE_SETUP.open(player);
                });
            }
        });
    }

    @Override
    public void handle() {
        bind("Talk-to", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                if (!player.getBooleanAttribute("registered")) {
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Greetings! I see you are a new arrival to this world.")
                                .executeAction(() -> sendGameModeSetup(player, npc));
                        }
                    });
                } else {
                    player.getDialogueManager().start(new Dialogue(player, npc) {
                        @Override
                        public void buildDialogue() {
                            npc("Hey! It's good to see you again, " + player.getName() + ". What can I do for you?");
                            setKey(5);
                            ExpConfigurations config = ExpConfigurations.of(player.getGameMode());
                            if (config != null) {
                                int currentIndex = config.getExpConfigurationIndex(player.getCombatXPRate(), player.getSkillingXPRate());
                                ExpConfiguration currentExp = config.getConfigurations()[currentIndex];
                                ExpConfiguration[] easier = config.getEasier(currentIndex);
                                if (easier != null) {
                                    String[] options = Arrays.stream(easier).map(ExpConfiguration::getString).toList().toArray(new String[0]);
                                    npc(5, "I see that you're on the <col=00080>" + currentExp.getString() + " mode</col>. Would you be interested in changing that into an easier experience mode?");
                                    options("Select the experience mode", options)
                                            .onOptionOne(() -> setKey(selectConfigurationMode(player, easier, 0)))
                                            .onOptionTwo(() -> setKey(selectConfigurationMode(player, easier, 1)))
                                            .onOptionThree(() -> setKey(selectConfigurationMode(player, easier, 2)))
                                            .onOptionFour(() -> setKey(selectConfigurationMode(player, easier, 3)))
                                            .onOptionFive(() -> setKey(selectConfigurationMode(player, easier, 4)));
                                } else {
                                    npc(5, "There are no easier modes available for you, perhaps you should change your game mode.");
                                }
                            } else {
                                npc(5, "There are no easier modes available for you, perhaps you should change your game mode.");
                            }
                            npc(35, "Please be aware you will not be able to revert this change if you accept.").executeAction(() -> confirmDialogue(player, npc));
                        }
                    });
                }
            }


            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
                if (npc.getLocation().getPositionHash() != HOME_ZENYTE_GUIDE.getPositionHash()) {
                    npc.setInteractingWith(player);
                }
            }
        });
    }

    private static void confirmDialogue(Player player, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                options("Choose " + (((ExpConfiguration) player.getTemporaryAttributes().get("review_exp")).getString()) + "?",
                        "Yes please", "No thanks").onOptionOne(() -> {
                    setMode(player, (ExpConfiguration) player.getTemporaryAttributes().get("review_exp"));
                    showNewMode(player, npc);
                }).onOptionTwo(() -> setKey(15));
                npc(15, "That's fine, come back if you change your mind!");
            }
        });
    }

    private static void showNewMode(Player player, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("Your new exp mode is " + player.getCombatXPRate() + "x combat, " + player.getSkillingXPRate() + "x skilling.");
            }
        });
    }

    private static int selectConfigurationMode(Player player, ExpConfiguration[] easier, int index) {
        if (index >= easier.length) {
            return 25;
        } else {
            player.addTemporaryAttribute("review_exp", easier[index]);
            return 35;
        }
    }


    private static void setMode(Player player, ExpConfiguration expConfiguration) {
        player.setExperienceMultiplier(expConfiguration);
        player.sendAdventurersEntry(AdventurersLogIcon.OVERALL_SKILLING, player.getName() + " has just changed exp mode - they are now playing under the " + expConfiguration + " mode!");
    }


    @Override
    public int[] getNPCs() {
        return new int[]{NPC_ID};
    }
}
