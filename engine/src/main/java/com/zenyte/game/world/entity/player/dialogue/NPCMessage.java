package com.zenyte.game.world.entity.player.dialogue;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;
import mgi.types.config.npcs.NPCDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Arham 4 on 2/15/2016.
 * <p>
 * Represents a singular message with an NPC talking.
 */
public class NPCMessage implements Message {
	private static final Logger logger = LoggerFactory.getLogger(NPCMessage.class);

	private static final int INTERFACE_ID = 231;
	private static final int NPC_HEAD_COMPONENT = 2;
	private static final int NPC_NAME_COMPONENT = 4;
	private static final int CONTINUE_COMPONENT = 5;
	private static final int TEXT_COMPONENT = 6;

	private final Expression expression;
	private final int npcId;
	private final String message;
	private final boolean showContinue;
	private final String npcName;
	private Runnable onDisplay;
	private Runnable runnable;

	public NPCMessage(final int npcId, final Expression expression, final String message) {
		this(npcId, expression, message, true, null);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final boolean showContinue) {
		this(npcId, expression, message, showContinue, null);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final String npcName) {
		this(npcId, expression, message, true, npcName);
	}

	public NPCMessage(final int npcId, final Expression expression, final String message, final boolean showContinue, final String npcName) {
		this.npcId = npcId;
		this.expression = expression;
		this.message = message;
		this.showContinue = showContinue;
		this.npcName = npcName;
	}

	@Override
	public void executeAction(final Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void execute(final Player player) {
		if (runnable != null) {
			runnable.run();
		}
	}

	@Override
	public void display(final Player player) {
		final NPCDefinitions baseDefs = NPCDefinitions.get(npcId);
		if (baseDefs == null) {
			logger.warn("Couldn't find base def for NPC ID {}", npcId);
			return;
		}
		final NPCDefinitions transmogrifiedDefs = NPCDefinitions.get(player.getTransmogrifiedId(baseDefs, npcId));

		player.getInterfaceHandler().sendInterface(InterfacePosition.DIALOGUE, INTERFACE_ID);
		player.getPacketDispatcher().sendComponentSettings(INTERFACE_ID, CONTINUE_COMPONENT, -1, -1, AccessMask.CONTINUE);
		player.getPacketDispatcher().sendComponentNPCHead(INTERFACE_ID, NPC_HEAD_COMPONENT, npcId);
		player.getPacketDispatcher().sendComponentText(INTERFACE_ID, NPC_NAME_COMPONENT, npcName == null ? transmogrifiedDefs.getName() : npcName);
		if (showContinue) {
			player.getPacketDispatcher().sendComponentText(INTERFACE_ID, CONTINUE_COMPONENT, continueMessage(player));
		} else {
			player.getPacketDispatcher().sendComponentVisibility(INTERFACE_ID, CONTINUE_COMPONENT, true);
		}
		player.getPacketDispatcher().sendComponentText(INTERFACE_ID, TEXT_COMPONENT, message);
		player.getPacketDispatcher().sendClientScript(600, 1, 1, 16, (INTERFACE_ID << 16) | TEXT_COMPONENT);

		player.getPacketDispatcher().sendComponentAnimation(INTERFACE_ID, NPC_HEAD_COMPONENT, expression.getId());
		final String toString = expression.toString();
		if (toString.startsWith("HIGH_REV")) {
			final String name = (npcName == null ? transmogrifiedDefs.getName() : npcName);
			final int zoom = name.contains("General ") || name.equalsIgnoreCase("Tiny Thom") ? 1000 : 2500;
			player.getPacketDispatcher().sendComponentAngle(INTERFACE_ID, NPC_HEAD_COMPONENT, 0, 1900, zoom);
		} else if (toString.startsWith("EASTER_BUNNY") || toString.startsWith("EASTER_BIRD")) {
			player.getPacketDispatcher().sendComponentAngle(INTERFACE_ID, NPC_HEAD_COMPONENT, 0, 1900, 2500);
		}
		if (onDisplay != null) {
			onDisplay.run();
		}
	}

	public void setOnDisplay(Runnable onDisplay) {
		this.onDisplay = onDisplay;
	}
}
