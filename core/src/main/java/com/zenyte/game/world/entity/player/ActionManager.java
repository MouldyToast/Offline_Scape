package com.zenyte.game.world.entity.player;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.logger.NearRealityLogger;
import com.zenyte.logger.NearRealityPrintStream;
import org.slf4j.Logger;

public final class ActionManager {
	private static final Logger log = NearRealityLogger.getLogger(ActionManager.class);
	private final Player player;
	private Action action;
	private int actionDelay;
	private Class<?> lastActionClass;
	private long lastActionCancelTick;
	private transient long lastAction = System.currentTimeMillis();
	private transient boolean processed = false;
	private transient boolean preventDelaySet;


	public ActionManager(final Player player) {
		this.player = player;
	}

	public void addActionDelay(final int skillDelay) {
		actionDelay += skillDelay;
	}

	public void forceStop() {
		if (action == null) {
			return;
		}
		this.lastActionClass = action.getClass();
		this.lastActionCancelTick = WorldThread.getCurrentCycle();
		action.stop();
		action = null;
	}

	public boolean wasInCombatThisTick() {
		if (lastActionClass == null) {
			return false;
		}
		return lastActionCancelTick == WorldThread.getCurrentCycle() && PlayerCombat.class.isAssignableFrom(lastActionClass);
	}

	public Action getAction() {
		return action;
	}

	public int getActionDelay() {
		return actionDelay;
	}

	public void setActionDelay(final int skillDelay) {
		actionDelay = skillDelay;
	}

	public boolean hasSkillWorking() {
		return action != null;
	}

	public void interrupt(final boolean interrupt) {
		if (action == null) {
			return;
		}
		final boolean interruption = action.setInterrupted(interrupt);
		if (!interruption) {
			forceStop();
		}
	}

	public void process() {
		processed = false;
		if (action != null) {
			if (player.isDead()) {
				forceStop();
			}
		}
		if (action != null && action.interruptedByDialogue() && player.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE)) {
			if (actionDelay > 0) {
				actionDelay--;
			}
			return;
		} else if (action != null && action.isInterrupted()) {
			interrupt(false);
		}
		if (action != null) {
			if (!action.process()) {
				forceStop();
			}
		}
		boolean isCombat =
				action instanceof MeleeCombat
						|| action instanceof RangedCombat
						|| action instanceof MagicCombat;

		if (actionDelay > 0) {
			actionDelay--;

			if (isCombat && player.getTemporaryAttributes().containsKey("combat debug")) {
				player.sendMessage("Total Action Delay: " + actionDelay +
						"  Player Tick: " + player.getTicker());
			}

			if (!isCombat || actionDelay > 0) {
				return;
			}
		}
		if (action == null) {
			return;
		}
		try {
			final int delay = action.processWithDelay();
			if (delay == -1) {
				forceStop();
				return;
			}
			processed = true;
			if (preventDelaySet) {
				preventDelaySet = false;
			} else {
				actionDelay += delay;
			}
		} catch (Exception e) {
			e.printStackTrace(NearRealityPrintStream.getErrorStream());
			forceStop();
		}
	}

	public void forceProcessAfterMovement() {
		this.processed = true;
	}

	public void processAfterMovement() {
		if (action != null) {
			if (player.isDead()) {
				forceStop();
			}
		}
		if (action != null && action.interruptedByDialogue() && player.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE)) {
			return;
		}
		if (action != null) {
			if (action.postMovementProcess()) {
				return;
			}
		}
		if (action == null || !processed) {
			return;
		}
		try {
			final int delay = action.processAfterMovement();
			if (delay == -1) {
				forceStop();
				return;
			}
			if (preventDelaySet) {
				preventDelaySet = false;
			} else {
				actionDelay += delay;
				if (player.getTemporaryAttributes().containsKey("combat debug")) {
					String combatType = action instanceof MeleeCombat ? "Melee" : action instanceof RangedCombat ? "Range" : "Magic";
					player.sendMessage(combatType + " Combat Delay: " + delay + " Total Action Delay: " + actionDelay + " " + player.getTicker());
				}
			}
		} catch (Exception e) {
			e.printStackTrace(NearRealityPrintStream.getErrorStream());
			forceStop();
		}
	}

	public boolean setAction(final Action action) {
		forceStop();
		action.setPlayer(player);
		if (!action.start()) {
			action.stop();
			return false;
		}
		this.lastAction = System.currentTimeMillis();
		this.action = action;
		if (action.initiateOnPacketReceive()) process();
		return true;
	}

	public Class<?> getLastActionClass() {
		return lastActionClass;
	}

	public long getLastActionCancelTick() {
		return lastActionCancelTick;
	}

	public long getLastAction() {
		return lastAction;
	}

	public void preventDelaySet() { preventDelaySet = true; }

}
