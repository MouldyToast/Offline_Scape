package com.zenyte.game.content.tombsofamascut;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;


public abstract class AbstractTOAManager {



    public AbstractTOAManager(Player player) {
    }


    public void onLogin() {

    }

    public int damageDone;
    public int damageTaken;
    public int points;


    public int getDamageDone() { return damageDone; }

    public void setDamageDone(int damageDone) { this.damageDone = damageDone; }

    public int getDamageTaken() { return damageTaken; }

    public void setDamageTaken(int damageTaken) { this.damageTaken = damageTaken; }


    public void setRewardContainer(Container container) {

    }

    public abstract void triggerTOAFailure(boolean b);

    public abstract int getCurrentPoints();

    public abstract void setCurrentPoints(int currentPoints);

    public void sendHud() {

    }

    public abstract void refreshHudStates();

    public abstract void sendEmptyPartyList();

    protected abstract Object getCurrentEncounter();

    public abstract Object getViewingParty();

    public abstract void setViewingParty(Object toaRaidParty);

    public abstract Object getCurrentParty();

    public abstract void setCurrentParty(Object toaRaidParty);

    public abstract Object getAppliedParty();

    public abstract void setAppliedParty(Object toaRaidParty);

    public abstract boolean needsAbandonRequest();

    public abstract void startAbandonDialogue(String s, Runnable confirmRunnable);

    public abstract boolean enter(boolean b, Object firstEncounter);

    public abstract void refreshPathLevel(int i);

    public abstract void setCanClaimSupplies(boolean b);

    public abstract void removeTOAItems();

    public abstract void setToaPlayerLogoutState(Object toaPlayerLogoutState);

    public abstract void setCurrentEncounter(Object currentEncounter);

    public abstract int getIndividualDeaths();

    public abstract void setIndividualDeaths(int i);

    public abstract Container getSuppliesContainer();

    public abstract void withdrawSpecificSupplies(int slotId, int option);

    public abstract Container getRewardContainer();

    public abstract void setSuppliesContainer(Container suppliesContainer);

    public abstract boolean isCanClaimSupplies();

    public abstract Integer getCurrentInterfaceTab();

    public abstract void setCurrentInterfaceTab(int currentInterfaceTab);

    public abstract int getViewingValue();

    public abstract void setViewingValue(int viewingValue);
    public abstract Object getPartySettings();

    public abstract void setPartySettings(Object partySettings);

    protected abstract void updatePreset(int[] preset, int index);

    public abstract int[] getInvocationPreset(int currentPresetSlot);

    public abstract boolean viewingManagementInterface(String leaderDisplayName);

    public abstract void toggleInvocation(Object value, Player player);

    public abstract void clearInvocationPreset(int slotId);

    public abstract void saveInvocationPreset(int currentPresetSlot);

    public abstract boolean isPresetEmpty(int currentPresetSlot);

    protected abstract int getPresetBaseVarId(int index);

    public abstract void enterRaid();

    public abstract boolean storeSupply(int i, Item item);

    public abstract void withdrawSupplies(int i);

    public abstract void reSupply();

    public abstract void resetSessionAttributes();

    public abstract void sendRaidLevel();

    public abstract void refreshHudPlayers();

    public abstract void refreshTimer();

    public abstract void startLeaveDialogue();

    public abstract void initialize(AbstractTOAManager toaManager);

    public abstract Object getToaPlayerLogoutState();

    public abstract void advanceRaid(boolean b);

    public abstract void leaveTombs(String s);

    public abstract Object getRaidParty();

    public abstract void setRaidParty(Object toaRaidParty);
}
