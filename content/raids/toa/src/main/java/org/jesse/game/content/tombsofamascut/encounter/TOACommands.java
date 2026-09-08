package org.jesse.game.content.tombsofamascut.encounter;

import org.jesse.api.resources.Vote;
import org.jesse.game.content.toa.TOARewardHelper;
import org.jesse.game.content.toa.TOARewards;
import org.jesse.game.GameConstants;
import org.jesse.game.content.tombsofamascut.lobby.TOALobbyParty;
import org.jesse.game.content.tombsofamascut.raid.EncounterType;
import org.jesse.game.content.tombsofamascut.raid.TOARaidParty;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.player.GameCommands;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;

public class TOACommands {

    static {
        TOACommands.register();
    }

    public static void register() {
        new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "toatest", (p, args) -> {
            final TOALobbyParty party = new TOALobbyParty(p);
            TOALobbyParty.addLobbyParty(party);
            p.getTOAManager().enterRaid();
        });
        new GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "settoarewardmx", (p, args) -> {
            if(GameConstants.isOwner(p)) {
                int numerator = Integer.parseInt(args[0]);
                TOARewardHelper.INSTANCE.setRewardMultiplier(numerator);
            } else {
                p.sendMessage("You cannot use this command");
            }
        });
        new GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "carrytoa", (p, args) -> {
            int level = Integer.parseInt(args[0]);
            int points = Integer.parseInt(args[1]);
            boolean overridePurple = Integer.parseInt(args[2]) == 1;
            TOARaidParty party = (TOARaidParty) p.getTOAManager().getRaidParty();
            party.overrideRaidLevel(level);
            p.getTOAManager().setCurrentPoints(points);
            p.putBooleanTemporaryAttribute("overridePurple", overridePurple);
            WorldTasksManager.schedule(() -> {
                for(Player pl: party.getPlayers())
                    pl.getTOAManager().enter(false, EncounterType.REWARD_ROOM);
            }, 1);

        });

        new GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "ctoa", (p, args) -> {
            int level = 550;
            int points = 16000;
            boolean overridePurple = true;
            final TOALobbyParty party = new TOALobbyParty(p);
            TOALobbyParty.addLobbyParty(party);
            p.getTOAManager().enterRaid();
            WorldTasksManager.schedule(() -> {
                var raidParty = (TOARaidParty) p.getTOAManager().getRaidParty();
                raidParty.overrideRaidLevel(level);
                p.getTOAManager().setCurrentPoints(points);
                p.putBooleanTemporaryAttribute("overridePurple", overridePurple);
            }, 4);

            WorldTasksManager.schedule(() -> {
                for(Player pl: party.getPlayers())
                    pl.getTOAManager().enter(false, EncounterType.REWARD_ROOM);
            }, 8);

        });
        new GameCommands.Command(PlayerPrivilege.TRUE_DEVELOPER, "toaclears", (p, args) -> {
            int clears = Integer.parseInt(args[0]);
            p.addAttribute("tombs of amascut: entry mode", clears);
        });
        new GameCommands.Command(PlayerPrivilege.ADMINISTRATOR, "starttoa", (p, args) -> {
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options(
                            new DialogueOption("Akkha", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.HET_BOSS);
                             }),
                            new DialogueOption("Ba-Ba", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.APMEKEN_BOSS);
                            }),
                            new DialogueOption("Zebak", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.CRONDIS_BOSS);
                            }),
                            new DialogueOption("Kephri", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.SCABARIS_BOSS);
                            }),
                            new DialogueOption("Wardens", () -> {
                                final TOALobbyParty party = new TOALobbyParty(p);
                                TOALobbyParty.addLobbyParty(party);
                                p.getTOAManager().enterRaid();
                                p.getTOAManager().enter(false, EncounterType.WARDENS_FIRST_ROOM);
                            })
                    );
                }
            });
        });

    }
}
