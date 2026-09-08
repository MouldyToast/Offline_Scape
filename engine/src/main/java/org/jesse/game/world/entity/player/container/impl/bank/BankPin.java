package org.jesse.game.world.entity.player.container.impl.bank;

import com.google.gson.annotations.Expose;
import org.jesse.game.content.consumables.Consumable;
import org.jesse.game.util.TimeUtils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.collectionlog.CollectionLogRewardManager;

import java.util.Objects;

import static org.jesse.game.GameInterface.BANK_PIN_SETTINGS;
import static org.jesse.game.GameInterface.BANK_PIN_VERIFICATION;

public class BankPin {

    private enum PinRecoveryDelay {
        THREE_DAYS,
        SEVEN_DAYS
    }

    private enum PinLoginSetting {
        ALWAYS_LOCK,
        LOCK_AFTER_FIVE_MINUTES
    }

    private enum PinStatus {
        ENABLED,
        PENDING_ENABLING,
        DISABLED,
        PENDING_DISABLING
    }

    private final transient Player player;

    private transient boolean unlocked;

    @Expose
    private PinRecoveryDelay pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
    @Expose
    private PinLoginSetting pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
    @Expose
    private PinStatus pinStatus = PinStatus.DISABLED;
    @Expose
    private String pin;
    @Expose
    private String lastVerificationIp;

    private String enteredPin = "";

    private int configType = -1;
    private long lockAt;

    public BankPin(Player player) {
        this.player = player;
    }

    public void loggedIn() {
        // if we're set to ALWAYS_LOCK the lock
        if (pinLoginSetting == PinLoginSetting.ALWAYS_LOCK) return;
        // If there is no pin or lock time; STOP
        if ((player.getBankPin().getPin() != null && player.getBankPin().getPin().isBlank()) || lockAt <= 0)  return;
        // If we're not connecting from the last trusted IP, or it has passed the lock time; STOP
        if (!player.getIP().equals(lastVerificationIp) || System.currentTimeMillis() >= lockAt) return;
        // All other situations on login, will be Unlocked
        player.getBankPin().setUnlocked();
    }

    public void loggedOut() {
        // if there is not a lockAt time or the pin is locked; STOP
        if (lockAt == -1 || !player.getBankPin().isUnlocked()) return;
        // Set the lockAt to be 5 minutes from now
        lockAt = System.currentTimeMillis() + TimeUtils.getMinutesToMillis(5);
        // set the last trusted IP as this IP
        lastVerificationIp = player.getIP();
    }

    private void openPinEntryUI() {
        player.getInterfaceHandler().sendInterface(BANK_PIN_VERIFICATION);
        var dispatcher = player.getPacketDispatcher();
        dispatcher.sendClientScript(917, "ii", -1, -1);
        dispatcher.sendComponentVisibility(BANK_PIN_VERIFICATION.getId(), 14, true);
        if (enteredPin == null || enteredPin.isBlank()) {
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 2, "Enter your PIN");
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 7, "Please choose a new FOUR DIGIT PIN using the buttons below.");
        }
        else {
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 2, "Confirm your PIN");
            dispatcher.sendComponentText(BANK_PIN_VERIFICATION.getId(), 7, "Now please enter that number again.");
        }
    }

    public boolean requiresVerification(Player player, Runnable callback) {
        // if the PIN is not enabled, then no verification needed
        if (!isPinEnabled()) return false;
        // if the account is unlocked, then no verification needed
        if (isUnlocked())
            return false;
        // verify the account
        openPinEntryUI();
        player.awaitInputInt(tempPin -> attemptUnlock(player, tempPin, callback));
        return !player.getBankPin().isUnlocked();
    }

    private void attemptUnlock(Player player, int tempPin, Runnable callback) {
        player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
        if (Objects.equals(player.getBankPin().getPin(), formatInput(tempPin))) {
            player.getBankPin().setUnlocked();
            if (callback != null)
                callback.run();
        }
    }

    // Used when creating an initial / new PIN
    protected void promptPinEntry(Player player) {
        // if we're Deleting this PIN
        if (configType == 3) {
            handleDeleteInput(player);
            return;
        }
        // else we're waiting for an input
        openPinEntryUI();
        player.awaitInputInt(tempPin -> {
            var value = formatInput(tempPin);
            if (Objects.equals(value, "12345")) { // exit
                player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
                return;
            }
            if (enteredPin != null && !enteredPin.isBlank()) {
                if (Objects.equals(value, enteredPin))
                    setNewPin(player, value);
                else
                    sendDoesNotMatchMessage(player);
                player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
                return;
            }
            enteredPin = value;
            // prompt the player to verify what they just entered
            promptPinEntry(player);
        });
    }

    /**
     * Updates the player's bank PIN to the specified new value, unlocks the bank pin, and notifies the player.
     * Closes any open interface after setting the new PIN.
     *
     * @param player The player whose bank PIN is being updated.
     * @param value  The new PIN value to be set for the player.
     */
    private void setNewPin(Player player, String value) {
        player.getBankPin().setPin(value);
        player.getBankPin().setUnlocked();
        player.sendMessage("Your new PIN has been set, please don't forget it!");
        player.getInterfaceHandler().closeInterfaces();
        pinStatus = PinStatus.ENABLED;
    }

    /**
     * Handles the process of deleting a player's bank PIN. This includes prompting the player to enter their PIN,
     * validating the entered PIN, and removing the PIN if validation succeeds. Also manages UI updates
     * and user notifications based on the validation result.
     *
     * @param player The player attempting to delete their bank PIN.
     */
    private void handleDeleteInput(Player player) {
        openPinEntryUI();
        player.awaitInputInt(tempPin -> {
            var value = formatInput(tempPin);
            if (Objects.equals(value, "12345")) { // exit
                player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
                return;
            }
            if (!player.getBankPin().enteredPin.isBlank())
                if (Objects.equals(value, player.getBankPin().enteredPin)) {
                    resetBankPinDefaults();
                    player.sendMessage("Your PIN has been removed.");
                }
                else
                    sendDoesNotMatchMessage(player);
            player.getInterfaceHandler().closeInterface(BANK_PIN_VERIFICATION);
        });
    }

    /**
     * Formats the entered PIN by ensuring it is padded with leading zeros to always have at least four digits.
     * <p>
     * <l>Special cases:
     *     <li>Returns an empty string if the entered PIN is 12345 or -1.</li>
     *     <li>Keeps the original format if the entered PIN has four or more digits.</li>
     * </l>
     *
     * @param enteredPin The PIN entered by the user, represented as an integer.
     * @return A formatted string representation of the entered PIN, padded with leading zeros if necessary, or an empty string for special cases.
     */
    private String formatInput(int enteredPin) {
        if (enteredPin == 12345 || enteredPin == -1)
            return "";
        if (enteredPin < 10)
            return "000%d".formatted(enteredPin);
        else if (enteredPin < 100)
            return "00%d".formatted(enteredPin);
        else if (enteredPin < 1_000)
            return "0%d".formatted(enteredPin);
        else
            return String.valueOf(enteredPin);
    }

    /**
     * Sends a confirmation screen to the player for setting up, changing, or deleting a bank PIN.
     * Based on the provided type, the method updates the interface text and options appropriately.
     *
     * @param player The player to whom the confirmation screen is being sent.
     * @param type   The type of confirmation screen to display:
     *               <l>
     *                  <li>1 for setting up a new PIN</li>
     *                  <li>2 for changing an existing PIN</li>
     *                  <li>3 for deleting the PIN</li>
     *               </l>
     */
    protected void sendConfirmationScreen(Player player, int type) {
        // Type: 1 = setup | 2 = change | 3 = delete
        player.getInterfaceHandler().sendInterface(BANK_PIN_SETTINGS);
        configType = type;
        var dispatcher = player.getPacketDispatcher();
            dispatcher.sendClientScript(917, "ii", -1, -1);
            dispatcher.sendComponentVisibility(BANK_PIN_SETTINGS.getId(), 0, true);
            dispatcher.sendComponentVisibility(BANK_PIN_SETTINGS.getId(), 28, false);
        if (type == 1) {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to set a PIN to protect your bank?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want a PIN. I will never forget it!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, I might forget it!");
        }
        else if (type == 2) {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to change your PIN?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want to change my PIN!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, I want to keep my current PIN!");
        }
        else {
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 30, "Do you really wish to delete your PIN?");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 33, "Yes, I really want to delete my PIN!");
            dispatcher.sendComponentText(BANK_PIN_SETTINGS.getId(), 36, "No, keep my PIN, I want to be safe!");
        }
    }

    /**
     * Resets the bank PIN settings to their default values.
     * <p>
     * This method is used to initialize or reset the state of the bank PIN system
     * to its default configuration. It updates various fields such as recovery
     * delay, login settings, and status. It also resets temporary attributes like
     * entered PIN and lock states, and calls the `resetPin` method to clear the
     * stored pin value.
     * <p>
     * <l>The following default values are applied:
     *  <li>`pinRecoveryDelay` is set to a duration of three days.</li>
     *  <li>`pinLoginSetting` is configured to always require a PIN lock.</li>
     *  <li>`pinStatus` is set to `DISABLED`.</li>
     *  <li>`delay` is reset to `-1`.</li>
     *  <li>`unlocked` is set to `false`.</li>
     *  <li>`enteredPin` is reset to an empty string.</li>
     *  <li>`configType` is reset to `-1`.</li>
     * </l>
     */
    public void resetBankPinDefaults() {
        pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
        pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
        pinStatus = PinStatus.DISABLED;
        unlocked = false;
        enteredPin = "";
        pin = "";
        configType = -1;
    }

    private void sendDoesNotMatchMessage(Player player) {
        player.sendMessage("Those numbers did not match, please be sure the PIN you setup is something you can remember!");
    }

    public void initialize(final BankPin dto) {
        if (dto != null) {
            this.pinRecoveryDelay = dto.pinRecoveryDelay;
            this.pinLoginSetting = dto.pinLoginSetting;
            this.pinStatus = dto.pinStatus;
            this.pin = dto.pin;
            this.enteredPin = dto.enteredPin;
            this.configType = dto.configType;
            this.lockAt = dto.lockAt;
            this.lastVerificationIp = dto.lastVerificationIp;
            this.unlocked = false;
        }
    }

    // Getters / Setters below
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isUnlocked() {
        return unlocked;
    }
    public void setUnlocked() {
        unlocked = true;
    }
    public void lock() {
        unlocked = false;
    }

    public boolean isAlwaysLocked() {
        return pinLoginSetting == PinLoginSetting.ALWAYS_LOCK;
    }
    public boolean isPinEnabled() {
        return pinStatus == PinStatus.ENABLED;
    }
    public boolean isShortDelay() {
        return pinRecoveryDelay == PinRecoveryDelay.THREE_DAYS;
    }

    public void toggleShortDelay() {
        if (pinRecoveryDelay == PinRecoveryDelay.THREE_DAYS)
            pinRecoveryDelay = PinRecoveryDelay.SEVEN_DAYS;
        else
            pinRecoveryDelay = PinRecoveryDelay.THREE_DAYS;
    }
    public void toggleAlwaysSecure() {
        if (pinLoginSetting == PinLoginSetting.ALWAYS_LOCK)
            pinLoginSetting = PinLoginSetting.LOCK_AFTER_FIVE_MINUTES;
        else
            pinLoginSetting = PinLoginSetting.ALWAYS_LOCK;
    }
}
