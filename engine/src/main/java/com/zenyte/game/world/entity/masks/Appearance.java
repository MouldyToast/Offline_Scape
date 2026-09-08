package com.zenyte.game.world.entity.masks;

import com.google.gson.annotations.Expose;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.PlayerSkulls;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType;
import com.zenyte.game.world.entity.player.update.ObjTypeCustomisation;
import it.unimi.dsi.fastutil.bytes.Byte2ShortOpenHashMap;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import net.rsprot.protocol.game.outgoing.info.playerinfo.PlayerAvatarExtendedInfo;

import java.util.Arrays;

/**
 * @author Kris | 1. veebr 2018 : 22:26.11
 * @author Jire
 */
public final class Appearance {
    //
    public static final short[] DEFAULT_MALE_APPEARANCE = new short[]{0, 10, 18, 26, 33, 36, 42};
    public static final short[] DEFAULT_FEMALE_APPEARANCE = new short[]{45, 1000, 56, 61, 68, 70, 79};
    private final transient Player player;
    private final transient Byte2ShortOpenHashMap forcedAppearance = new Byte2ShortOpenHashMap();
    @Expose
    short[] appearance;
    transient short[] editingAppearance;
    @Expose
    byte[] colours;
    private transient RenderAnimation renderAnimation;
    private transient int npcId;
    private transient boolean invisible;
    private transient boolean hideEquipment;
    @Expose
    private boolean male;
    @Expose
    private byte headIcon;
    public final ObjTypeCustomisation[] objTypeCustomisations = new ObjTypeCustomisation[12];
    public int objCustomisationSettings;

    public Appearance(final Player player) {
        this.player = player;
        renderAnimation = RenderAnimation.DEFAULT_RENDER;
        appearance = Arrays.copyOf(DEFAULT_MALE_APPEARANCE, 7);
        male = true;
        npcId = -1;
        colours = new byte[5];
        headIcon = -1;
    }

    public void initialize(final Appearance appearance) {
        male = appearance.male;
        this.appearance = appearance.appearance;
        colours = appearance.colours;
    }

    public void forceAppearance(final int slot, final int id) {
        forcedAppearance.put((byte) slot, (short) id);
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    public void clearForcedAppearance() {
        if (!forcedAppearance.isEmpty()) {
            forcedAppearance.clear();
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        }
    }

    /**
     * Transforms the Player to specified NPC.
     *
     * @param id id of the npc to which the player is requested to transform to.
     */
    public void transform(final int id) {
        npcId = id;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * Changes the head-icon of the player
     *
     * @param id (id of the head-icon that needs to be changed.)
     */
    public void setHeadIcon(final byte id) {
        headIcon = id;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * Sets the player's visibility state to the specified state.
     *
     * @param invisible whether the player will turn invisible or not.
     */
    public void setInvisible(final boolean invisible) {
        this.invisible = invisible;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * @param invisible whether to hide the equipment
     */
    public void setHideEquipment(final boolean invisible) {
        hideEquipment = invisible;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * Modifies the body part to a certain value
     *
     * @param index (The body part that needs to be modified)
     * @param value (The value that the body part needs to be modified to)
     */
    public void modifyAppearance(final byte index, final short value) {
        appearance[index] = value;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * @param index (The colour of the body part that needs to be modified)
     * @param value (The value that the colour needs to be modified to)
     */
    public void modifyColour(final byte index, final byte value) {
        colours[index] = value;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * Generates a RenderAnimation object which holds the animation ids of the currently held weapon as a render animation. If the player
     * isn't wielding a weapon, the default state is returned.
     *
     * @return render animation object.
     */
    public RenderAnimation generateRenderAnimation() {
        final Item weapon = player.getWeapon();
        if (weapon == null) {
            return RenderAnimation.DEFAULT_RENDER;
        }
        final ItemDefinitions defs = weapon.getDefinitions();
        return new RenderAnimation(defs.getStandAnimation(), defs.getStandTurnAnimation(), defs.getWalkAnimation(), defs.getRotate180Animation(), defs.getRotate90Animation(), defs.getRotate270Animation(), defs.getRunAnimation());
    }

    /**
     * Resets the player's render animation to default, which is dependant on the weapon the player is wielding. If the player isn't
     * wielding a weapon, returns the default stance, if they are, will return the render animation of the weapon. Flags the appearance
     * mask.
     */
    public void resetRenderAnimation() {
        renderAnimation = generateRenderAnimation();
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    /**
     * Sets the player's render animation to the specified render animation object. Flags the appearance mask.
     *
     * @param anim render animation object to set the player's render animation to.
     */
    public void setRenderAnimation(final RenderAnimation anim) {
        renderAnimation = anim;
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    private int getId(final int slot) {
        if (!forcedAppearance.isEmpty() && forcedAppearance.containsKey((byte) slot)) {
            return forcedAppearance.get((byte) slot);
        }
        return player.getEquipment().getId(slot);
    }

    public String getGender() {
        final boolean ironmanmode = player.isIronman();
        return ironmanmode ? isMale() ? "Ironman" : "Ironwoman" : isMale() ? "Man" : "Woman";
    }

    public boolean isTransformedIntoNpc() {
        return npcId != -1;
    }

    public RenderAnimation getRenderAnimation() {
        return renderAnimation;
    }

    public int getNpcId() {
        return npcId;
    }

    public void setNpcId(int npcId) {
        this.npcId = npcId;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean isHideEquipment() {
        return hideEquipment;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public short[] getAppearance() {
        return appearance;
    }

    public void setAppearance(short[] appearance) {
        this.appearance = appearance;
    }

    public short[] getEditingAppearance() {
        return editingAppearance;
    }

    public void setEditingAppearance(short[] appearance) {
        this.editingAppearance = appearance;
    }

    public byte[] getColours() {
        return colours;
    }

    public void setColours(byte[] colours) {
        this.colours = colours;
    }

    public byte getHeadIcon() {
        return headIcon;
    }

    public Byte2ShortOpenHashMap getForcedAppearance() {
        return forcedAppearance;
    }

    public void close() {
    }

    public void recol1(int itemId, int wearSlot, int index, int colour) {
        var obj = objTypeCustomisations[wearSlot];
        if (obj == null) {
            obj = objTypeCustomisations[wearSlot] = new ObjTypeCustomisation(ItemDefinitions.get(itemId));
        }
        obj.recol(index, colour);
    }

    public void recol2(int itemId, int wearSlot, int index, int colour) {
        var obj = objTypeCustomisations[wearSlot];
        if (obj == null) {
            obj = objTypeCustomisations[wearSlot] = new ObjTypeCustomisation(ItemDefinitions.get(itemId));
        }
        obj.recolExtra(index, colour);
    }

    public void sync() {
        sync(false);
    }

    public void sync(boolean force) {
        if(!player.isAllocated)
            return;
        if (!force && !player.getUpdateFlags().get(UpdateFlag.APPEARANCE)) {
            return;
        }

        PlayerAvatarExtendedInfo info = player.getAvatar().getExtendedInfo();
        for (int i = 0; i < 7; i++) {
            if (i == 1 && !male) {
                info.setIdentKit(i, 296);
            } else {
                info.setIdentKit(i, this.appearance[i]);
            }
        }

        final int npcId = this.npcId;
        if (npcId >= 0) {
            final NPCDefinitions definitions = NPCDefinitions.get(npcId);
            if (definitions != null) {
                info.setTransmogrification(npcId);
            } else {
                info.setTransmogrification(-1);
            }
        } else {
            info.setTransmogrification(-1);
        }

        for (int wearPos = 0; wearPos <= 11; wearPos++) {
            if (invisible || hideEquipment) {
                info.setWornObj(wearPos, -1, -1, -1);
                continue;
            }

            int id;
            if (!forcedAppearance.isEmpty() && forcedAppearance.containsKey((byte) wearPos)) {
                id = forcedAppearance.get((byte) wearPos);
            } else {
                id = player.getEquipment().getId(wearPos);
            }
            ItemDefinitions defs = ItemDefinitions.get(id);
            if (defs == null) {
                info.setWornObj(wearPos, -1, -1, -1);
            } else if (false) {
                switch (EquipmentSlot.forSlot(wearPos)) {
                    case HELMET -> {
                        EquipmentType equipmentType = defs.getEquipmentType();
                        if (equipmentType == EquipmentType.FULL_MASK) {
                            info.setIdentKit(0, -1);
                            info.setIdentKit(1, -1);
                        } else if (equipmentType == EquipmentType.FULL_HELM) {
                            info.setIdentKit(0, -1);
                        }
                    }
                    case PLATE -> {
                        info.setIdentKit(2, -1);
                        if (defs.getEquipmentType() == EquipmentType.FULL_BODY) {
                            info.setIdentKit(3, -1);
                        }
                    }
                    case HANDS -> info.setIdentKit(4, -1);
                    case LEGS -> {
                        if (defs.getEquipmentType() == EquipmentType.FULL_LEGS) {
                            info.setIdentKit(5, -1);
                        }
                    }
                    case BOOTS -> info.setIdentKit(6, -1);
                    case null, default -> {}
                }

                info.setWornObj(wearPos, id, -1, -1);
            } else {
                /* these are hiding the identikit slots at given indices */
                int wearPos2 = -1, wearPos3 = -1;

                if(defs.getEquipmentType() == EquipmentType.FULL_BODY) {
                    wearPos3 = 6; // arms
                }
                if(defs.getEquipmentType() == EquipmentType.FULL_MASK) {
                    wearPos2 = 8; // hair
                    wearPos3 = 11; // beard
                }
                if(defs.getEquipmentType() == EquipmentType.FULL_HELM) {
                    wearPos2 = 8; // hair
                }

                info.setWornObj(wearPos, id, wearPos2, wearPos3);
            }
        }

        for (int i = 0; i < 5; i++) {
            info.setColour(i, colours[i]);
        }

        final RenderType renderAnimation = Utils.getOrDefault(this.renderAnimation, this.renderAnimation);
        info.setBaseAnimationSet(
                renderAnimation.getStand(),
                renderAnimation.getStandTurn(),
                renderAnimation.getWalk(),
                renderAnimation.getRotate180(),
                renderAnimation.getRotate90(),
                renderAnimation.getRotate270(),
                renderAnimation.getRun()
        );
        info.setName(player.getTitleName());
        info.setCombatLevel(player.getSkills().getCombatLevel());
        info.setBodyType(male ? 0 : 1);
        info.setPronoun(male ? 0 : 1);
        info.setSkullIcon(PlayerSkulls.getSkull(player));
        info.setOverheadIcon(headIcon);
        info.setHidden(invisible);
        for (int wearpos = 0; wearpos < objTypeCustomisations.length; wearpos++) {
            var obj = objTypeCustomisations[wearpos];
            if(obj == null) {
                continue;
            }
            if (obj.recols != null) {
                for (int slot = 0; slot < obj.recols.length; slot++) {
                    if (slot == 0) {
                        info.setObjRecol1(wearpos, slot, obj.recols[slot] & 0xffff);
                    }
                    if (slot == 1) {
                        info.setObjRecol2(wearpos, slot, obj.recols[slot] & 0xffff);
                    }
                }
            }
            if (obj.extraRecols != null) {
                for (int slot = 0; slot < obj.extraRecols.length; slot++) {
                    if (slot == 0) {
                        info.setObjRetex1(wearpos, slot, obj.extraRecols[slot] & 0xffff);
                    }
                    if (slot == 1) {
                        info.setObjRetex2(wearpos, slot, obj.extraRecols[slot] & 0xffff);
                    }
                }
            }
        }
        final String[] nametags = player.getNametags();
        if (nametags == null) {
            info.setNameExtras("", "", "");
        } else {
            info.setNameExtras(
                    nametags[0] == null ? "" : nametags[0],
                    nametags[1] == null ? "" : nametags[1],
                    nametags[2] == null ? "" : nametags[2]);
        }
    }

    /**
     * Removes all previously cached customizations and optionally queuing an appearance update by passing in a
     * value of `true`.
     */
    public void clearOverrides(boolean update) {
        Arrays.fill(objTypeCustomisations, null);
        if (update) {
            player.getAppearance().clearForcedAppearance();
        }
    }
}
