package org.jesse.game.model.ui;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.config.enums.EnumDefinitions;

public enum InterfacePosition {
	CHATBOX(96, 162, true),                  // was 94, now CHATBOX = 96
	PRIVATE_CHAT(93, 163, true),              // was 91, updated to SPLIT_CHAT's value (93)
	WILDERNESS_OVERLAY(3, true),              // remains 3 (FULL_OVERLAY_2)
	ORBS(33, true),                         // was 32, now updated to 33
	SKILLS_TAB(77, 320, true),                // was 76, now 77
	JOURNAL_TAB_HEADER(78, 629, true),        // was 77, now ACTIVE_JOURNAL_TAB = 28
	CHAT_TAB_HEADER(83, true),                // no mapping provided, remains 82
	INVENTORY_TAB(79, 149, true),             // was 78, now 79
	EQUIPMENT_TAB(80, 387, true),             // was 79, now 80
	PRAYER_TAB(81, 541, true),                // was 80, now 81
	SPELLBOOK_TAB(82, 218, true),             // was 81, now 82
	ACCOUNT_MANAGEMENT(84, 109, true),        // was 83, now 84
	FRIENDS_TAB(85, 429, true),               // was 84, now updated to 85 (SOCIAL_TAB)
	LOGOUT_TAB(86, 182, true),                // was 85, now 86
	SETTINGS_TAB(87, 116, true),              // was 86, now 87
	EMOTE_TAB(88, 216, true),                 // was 87, now 88
	MUSIC_TAB(89, 239, true),                 // was 88, now 89
	COMBAT_TAB(76, 593, true),                // was 75, now 76
	CENTRAL(16, false),                       // remains 16 (MODAL)
    DIALOGUE(567, false),                     // rev 240: chatmodal (567), was chatoverlay (566)
	MINIGAME_OVERLAY(8, true),                // remains 8 (PARTIAL_OVERLAY)
	OVERLAY(1, true),                         // remains 1 (FULL_OVERLAY)
	SINGLE_TAB(74, false),                    // was 73, now 74
	WORLD_MAP(18, false),                     // was 17, now 40
	FLOATER(18, true),
	UNKNOWN_OVERLAY(3, true),                 // remains 8
	XP_TRACKER(9, true),                      // remains 9
	COLOUR_PICKER(8, false),                  // remains 8 (no mapping provided)
	NOTIFICATION_POS(13, true),               // remains 13 (NOTIFICATION)
	HP_HUD_POS(2, -1, true),                  // remains 2 (TARGET_OVERLAY)
	TOA_MANAGEMENT(61, false),                // remains 61 (no mapping provided)
	NIGHTMARE_TOTEMS_POS(12, -1, true);       // remains 12
	;
	/**
	 * An array containing all of the component types.
	 */
	public static final InterfacePosition[] VALUES = values();

	/**
	 * Gets the component pairs when moving from one game pane to another.
	 * 
	 * @param fromPane
	 *            the pane we're moving from.
	 * @param toPane
	 *            the pane we're moving to.
	 * @return a primitive int map containing all the pairs.
	 */
	public static Int2IntOpenHashMap getPairs(final PaneType fromPane, final PaneType toPane) {
		final Int2IntOpenHashMap pairs = new Int2IntOpenHashMap(VALUES.length);
		for (final InterfacePosition position : VALUES) {
			if (position.equals(DIALOGUE)) {
				continue;
			}
			final int from = position.getComponent(fromPane);
			final int to = position.getComponent(toPane);
			if (from != -1 && to != -1) {
				pairs.put(from, to);
			}
		}
		return pairs;
	}

	/**
	 * Gets the component type for the respective component id and pane.
	 * 
	 * @param componentId
	 *            the resizable component id.
	 * @param pane
	 *            the pane to search.
	 * @return the respective component type, or null if not found.
	 */
	public static InterfacePosition getPosition(final int componentId, final PaneType pane) {
		for (final InterfacePosition position : VALUES) {
			final EnumDefinitions e = pane.getEnum();
			final int bitpacked = e.getIntValue(161 << 16 | componentId);
			if (componentId == (bitpacked & 65535)) {
				return position;
			}
		}
		return null;
	}

	private final int resizableComponent;
	private final int gameframeInterfaceId;
	private final boolean walkable;

	InterfacePosition(final int resizableComponent, final boolean walkable) {
		this(resizableComponent, -1, walkable);
	}

	/**
	 * Constructs the component types with the seed component of resizable, used to search the other types.
	 * 
	 * @param resizableComponent
	 *            the resizable component id, used as a seed.
	 * @param walkable
	 *            whether the component is walkable or not.
	 */
	InterfacePosition(final int resizableComponent, final int gameframeInterfaceId, final boolean walkable) {
		this.resizableComponent = resizableComponent;
		this.gameframeInterfaceId = gameframeInterfaceId;
		this.walkable = walkable;
	}

	/**
	 * Gets the component id for the respective pane based on the resizable type.
	 * 
	 * @param pane
	 *            the pane to seek.
	 * @return the component id
	 */
	public final int getComponent(final PaneType pane) {
		if (pane == null) {
			return -1;
		} else if (pane == PaneType.RESIZABLE || pane == PaneType.TOA_MANAGEMENT) {
			return resizableComponent;
		}
		final EnumDefinitions e = pane.getEnum();
		final int bitpacked = e.getIntValue(161 << 16 | resizableComponent);
        if (bitpacked == -1) return -1;
		return bitpacked == 0 ? resizableComponent : bitpacked & 65535;
	}

	/**
	 * Gets the fixed component based on the resizable one.
	 * 
	 * @return the fixed component's id.
	 */
	public final int getFixedComponent() {
		final EnumDefinitions e = PaneType.FIXED.getEnum();
		final int bitpacked = e.getIntValue(161 << 16 | resizableComponent);
		return bitpacked == 0 ? resizableComponent : bitpacked & 65535;
	}

	/**
	 * Gets the fullscreen component based on the resizable one.
	 * 
	 * @return the fullscreen component's id.
	 */
	public final int getFullScreenComponent() {
		final EnumDefinitions e = PaneType.FULL_SCREEN.getEnum();
		final int bitpacked = e.getIntValue(161 << 16 | resizableComponent);
		return bitpacked == 0 ? resizableComponent : bitpacked & 65535;
	}

	/**
	 * Gets the mobile component based on the resizable one.
	 * 
	 * @return the mobile component's id.
	 */
	public final int getMobileComponent() {
		final EnumDefinitions e = PaneType.MOBILE.getEnum();
		final int bitpacked = e.getIntValue(161 << 16 | resizableComponent);
		return bitpacked == 0 ? resizableComponent : bitpacked & 65535;
	}

	/**
	 * Gets the sidepanels component based on the resizable one.
	 *
	 * @return the sidepanels component's id.
	 */
	public final int getSidepanelsComponent() {
		final EnumDefinitions e = PaneType.SIDE_PANELS.getEnum();
		final int bitpacked = e.getIntValue(161 << 16 | resizableComponent);
		return bitpacked == 0 ? resizableComponent : bitpacked & 65535;
	}

	public int getResizableComponent() {
		return resizableComponent;
	}

	public int getGameframeInterfaceId() {
		return gameframeInterfaceId;
	}

	public boolean isWalkable() {
		return walkable;
	}
}
