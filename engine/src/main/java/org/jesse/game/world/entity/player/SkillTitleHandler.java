package org.jesse.game.world.entity.player;

import org.jesse.game.model.ui.loyaltytitles.LoyaltyTitleShop;

public class SkillTitleHandler {

    private static final int LEVEL_99_XP = 13_034_431; // XP required for Level 99
    private static final int XP_200M = 200_000_000;    // XP required for 200M milestone
    private static final int INTERVAL_TICKS = 100;     // Check every 100 game ticks (~60 seconds)

    public static void unlockLevel99Title(Player player, String skillName) {
        switch (skillName) {
            case "Agility" -> LoyaltyTitleShop.Companion.unlockTitle(player, "balance baron");
            case "Farming" -> LoyaltyTitleShop.Companion.unlockTitle(player, "the gardener");
            case "Hunter" -> LoyaltyTitleShop.Companion.unlockTitle(player, "apprentice baiter");
            case "Fishing" -> LoyaltyTitleShop.Companion.unlockTitle(player, "the master fisherman");
            case "Mining" -> LoyaltyTitleShop.Companion.unlockTitle(player, "mine over matter");
            case "Woodcutting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "log lord");
            case "Cooking" -> LoyaltyTitleShop.Companion.unlockTitle(player, "chef");
            case "Crafting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "the journeyman");
            case "Fletching" -> LoyaltyTitleShop.Companion.unlockTitle(player, "quiver quipper");
            case "Herblore" -> LoyaltyTitleShop.Companion.unlockTitle(player, "brewmaster");
            case "Runecrafting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "mystic misfit");
            case "Smithing" -> LoyaltyTitleShop.Companion.unlockTitle(player, "iron innovator");
            case "Firemaking" -> LoyaltyTitleShop.Companion.unlockTitle(player, "pyro");
            case "Slayer" -> LoyaltyTitleShop.Companion.unlockTitle(player, "vannaka's vanguard");
            case "Thieving" -> LoyaltyTitleShop.Companion.unlockTitle(player, "thief");
            default -> player.sendDeveloperMessage("No Level 99 title found for skill: " + skillName);
        }
    }

    public static void unlock200MTitle(Player player, String skillName) {
        switch (skillName) {
            case "Agility" -> LoyaltyTitleShop.Companion.unlockTitle(player, "rooftop racer");
            case "Farming" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Master Farmer");
            case "Hunter" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Master Baiter");
            case "Fishing" -> LoyaltyTitleShop.Companion.unlockTitle(player, "The Codfather");
            case "Mining" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Rockhard");
            case "Woodcutting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Paul Bunyan");
            case "Cooking" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Gourmet Guru");
            case "Crafting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Chisel Chieftain");
            case "Fletching" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Bolt Boss");
            case "Herblore" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Mixologist");
            case "Runecrafting" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Rune Sage");
            case "Smithing" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Alloy Alchemist");
            case "Firemaking" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Kindled");
            case "Slayer" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Duradel's Disciple");
            case "Thieving" -> LoyaltyTitleShop.Companion.unlockTitle(player, "Safecracker");
            default -> player.sendDeveloperMessage("No 200M XP title found for skill: " + skillName);
        }
    }
}