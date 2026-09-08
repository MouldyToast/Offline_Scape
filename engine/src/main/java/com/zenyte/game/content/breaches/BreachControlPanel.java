package com.zenyte.game.content.breaches;

import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class BreachControlPanel extends Dialogue {

    public BreachControlPanel(Player player) {
        super(player);
    }

    @Override
    public void buildDialogue() {
        options("Breaches Control Panel",
                BreachManager.breachActive() ? "Force STOP breach" : "Force START breach",
                "Boss Quantity ("+BreachSettings.BOSS_QUANTITY+")",
                "Boss Radius ("+BreachSettings.BOSS_RADIUS+")",
                "Bosses allowed at one time ("+BreachSettings.BOSS_AMOUNT+")",
                "Breach Active? ("+BreachSettings.BREACHES_ACTIVE+")").
                onOptionOne(() -> {
                    finish();
                    if(BreachManager.breachActive()) {
                        BreachManager.getInstance().closeBreach(true);
                        player.getDialogueManager().start(new Dialogue(player, -1) {
                            @Override
                            public void buildDialogue() {
                                plain("Breach closed!");
                            }
                        });
                    } else {
                        BreachManager.createNewBreach();
                        player.getDialogueManager().start(new Dialogue(player, -1) {
                            @Override
                            public void buildDialogue() {
                                plain("Breach created: "+BreachManager.getInstance().getBreachLocation().toString());
                            }
                        });
                    }
                }).
                onOptionTwo(() -> {
                    finish();
                    player.sendInputInt("Enter boss quantity to spawn in a breach (default 50)",
                            value -> {
                                BreachSettings.BOSS_QUANTITY = value;
                                player.getDialogueManager().start(new BreachControlPanel(player));
                            });
                }).
                onOptionThree(() -> {
                    finish();
                    player.sendInputInt("Enter boss radius (default 10)",
                            value -> {
                                BreachSettings.BOSS_RADIUS = value;
                                player.getDialogueManager().start(new BreachControlPanel(player));
                            });
                }).
                onOptionFour(() -> {
                    finish();
                    player.sendInputInt("Enter bosses allowed at one time (default 3)",
                            value -> {
                                BreachSettings.BOSS_AMOUNT = value;
                                player.getDialogueManager().start(new BreachControlPanel(player));
                            });
                }).
                onOptionFive(() -> {
                    finish();
                    BreachSettings.BREACHES_ACTIVE = !BreachSettings.BREACHES_ACTIVE;
                    if(BreachSettings.BREACHES_ACTIVE) {
                        player.getDialogueManager().start(new Dialogue(player, -1) {
                            @Override
                            public void buildDialogue() {
                                plain("Breaches now active!");
                            }
                        });
                    } else {
                        player.getDialogueManager().start(new Dialogue(player, -1) {
                            @Override
                            public void buildDialogue() {
                                plain("Breaches now disabled!");
                            }
                        });
                    }
                });
    }
}
