package mgi.types.worldmap;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

/**
 * @author Tommeh | 6-12-2018 | 23:21
 * @author Jire
 */
public final class MapElementDefinitions implements Definitions {

    private static final Int2ObjectMap<MapElementDefinitions> definitions =
            new Int2ObjectOpenHashMap<>();

    private int id;
    private int spriteId = -1;
    private int op2 = -1;//always -1
    private String text;
    private int colour;
    private int textSize;
    private String[] options;
    private String optionName;
    private int[] field3312 = new int[0];//always null
    private int field3313;//always 2147483647
    private int field3314;//always 2147483647
    private int field3315;//always -2147483648
    private int field3316;//always -2147483648
    private int horizontalAlignment;
    private int verticalAlignment;
    private int[] field3307 = new int[0];//always null
    private byte[] field3320 = new byte[0];//always null
    //Used for getting string value from enum 1713
    private int tooltipId = -1;
    private int field5 = -1;
    private int field7 = -1;
    private int field8 = -1;
    private int field18 = -1;
    private int field21 = -1;
    private int field22 = -1;
    private int field23_1 = -1;
    private int field23_2 = -1;
    private int field23_3 = -1;
    private int field24_1 = -1;
    private int field24_2 = -1;
    private int field25 = -1;
    private int field28 = -1;
    private int field15_sub = -1;

    public int getGroupId() {
        return id << 8 | 10;//10 = index of the string, Open is first so it's 10; effectively 10 + index.
    }

    @Override
    public void pack() {
        definitions.put(id, this);
        try {
            pack(id, encode());
        } catch (Exception e) {
            logger.error("Failed to pack MapElementDefinition with id: {}", id);
        }
    }

    public static void pack(int id, ByteBuffer buffer) {
        CacheManager.getCache()
                .getArchive(ArchiveType.CONFIGS)
                .findGroupByID(GroupType.MAP_LABELS)
                .addFile(new File(id, buffer));
    }

    @Override
    public void load() {
        definitions.clear();

        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group labels = configs.findGroupByID(GroupType.MAP_LABELS);
        for (int id = 0; id < labels.getHighestFileId(); id++) {
            final File file = labels.findFileByID(id);
            if (file == null) {
                continue;
            }

            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }

            final MapElementDefinitions def = new MapElementDefinitions(id, buffer);
            put(id, def);
        }
    }

    @Nullable
    public static MapElementDefinitions get(final int id) {
        return definitions.get(id);
    }

    public static void put(final int id, final MapElementDefinitions def) {
        if (definitions.containsKey(id)) {
            throw new IllegalArgumentException("Map element def with ID " + id + " already exists.");
        }
        definitions.put(id, def);
    }

    public MapElementDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        this.textSize = 0;
        this.horizontalAlignment = 1;
        this.verticalAlignment = 1;
        this.options = new String[5];
        this.field3313 = Integer.MAX_VALUE;
        this.field3314 = Integer.MAX_VALUE;
        this.field3315 = Integer.MIN_VALUE;
        this.field3316 = Integer.MIN_VALUE;
        decode(buffer);
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    @Override
    public void decode(final ByteBuffer buffer, final int opcode) {
        int var3;
        int var4;
        switch (opcode) {
            case 1:
                spriteId = buffer.readBigSmart();
                return;
            case 2:
                op2 = buffer.readBigSmart();
                return;
            case 3:
                text = buffer.readString();
                return;
            case 4:
                colour = buffer.readMedium();
                return;
            case 5:
                field5 = buffer.readMedium();
                return;
            case 6:
                textSize = buffer.readUnsignedByte();
                return;
            case 7:
                field7 = buffer.readUnsignedByte();
                return;
            case 8:
                field8 = buffer.readByte();
                return;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                options[opcode - 10] = buffer.readString();
                return;
            case 15:
                var3 = buffer.readUnsignedByte();
                field3312 = new int[var3 * 2];
                for (int index = 0; index < var3 * 2; index++) {
                    field3312[index] = buffer.readShort();
                }
                field15_sub = buffer.readInt();
                var4 = buffer.readUnsignedByte();
                field3307 = new int[var4];
                for (int index = 0; index < field3307.length; index++) {
                    field3307[index] = buffer.readInt();
                }
                field3320 = new byte[var3];
                for (int index = 0; index < var3; index++) {
                    field3320[index] = buffer.readByte();
                }
                return;
            case 17:
                optionName = buffer.readString();
                return;
            case 18:
                field18 = buffer.readBigSmart();
                return;
            case 19:
                tooltipId = buffer.readUnsignedShort();
                return;
            case 21:
                field21 = buffer.readInt();
                return;
            case 22:
                field22 = buffer.readInt();
                return;
            case 23:
                field23_1 = buffer.readByte();
                field23_2 = buffer.readByte();
                field23_3 = buffer.readByte();
                return;
            case 24:
                field24_1 = buffer.readShort();
                field24_2 = buffer.readShort();
                return;
            case 25:
                field25 = buffer.readBigSmart();
                return;
            case 28:
                field28 = buffer.readByte();
                return;
            case 29:
                horizontalAlignment = buffer.readUnsignedByte();
                return;
            case 30:
                verticalAlignment = buffer.readUnsignedByte();
                return;
        }
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(4096);
        if(spriteId != -1) {
            buffer.writeByte(1);
            buffer.writeBigSmart(spriteId);
        }

        if(op2 != -1) {
            buffer.writeByte(2);
            buffer.writeBigSmart(op2);
        }

        if (text != null) {
            buffer.writeByte(3);
            buffer.writeString(text);
        }

        buffer.writeByte(4);
        buffer.writeMedium(colour);

        if(field5 != -1) {
            buffer.writeByte(5);
            buffer.writeMedium(field5);
        }

        buffer.writeByte(6);
        buffer.writeByte(textSize);
        if(field7 != -1) {
            buffer.writeByte(7);
            buffer.writeByte(field7);
        }

        if(field8 != -1) {
            buffer.writeByte(8);
            buffer.writeByte(field8);
        }

        for (int opcode = 10; opcode <= 14; opcode++) {
            if (options[opcode - 10] != null) {
                buffer.writeByte(opcode);
                buffer.writeString(options[opcode - 10]);
            }
        }
        if(field3312.length > 0 || field3307.length > 0 || field3320.length > 0) {
            int var3 = field3312.length / 2;
            buffer.writeByte(var3);

            for (int i = 0; i < var3 * 2; i++) {
                buffer.writeShort(field3312[i]);
            }

            buffer.writeInt(field15_sub);

            int var4 = field3307.length;
            buffer.writeByte(var4);

            for (int i = 0; i < var4; i++) {
                buffer.writeInt(field3307[i]);
            }

            for (int i = 0; i < var3; i++) {
                buffer.writeByte(field3320[i]);
            }
        }
        /*
        buffer.writeByte(15);
        if (field3312 != null) {
            buffer.writeByte((field3312.length / 2));
            for (int index = 0; index < field3312.length / 2; index++) {
                buffer.writeShort(field3312[index]);
            }
        }
        buffer.putInt(-1);
        if (field3307 != null) {
            buffer.writeByte(field3307.length);
            for (int index = 0; index < field3307.length; index++) {
                buffer.putInt(field3307[index]);
            }
        }

        if (field3320 != null) {
            for (int index = 0; index < field3312.length; index++) {
                buffer.put(field3320[index]);
            }
        }*/
        if (optionName != null) {
            buffer.writeByte(17);
            buffer.writeString(optionName);
        }
        if(field18 != -1) {
            buffer.writeByte(18);
            buffer.writeBigSmart(field18);
        }
        if(tooltipId != -1) {
            buffer.writeByte(19);
            buffer.writeShort(tooltipId);
        }
        if(field21 != -1) {
            buffer.writeByte(21);
            buffer.writeInt(field21);
        }
        if(field23_1 != -1 || field23_2 != -1 || field23_3 != -1) {
            buffer.writeByte(23);
            buffer.writeByte(field23_1);
            buffer.writeByte(field23_1);
            buffer.writeByte(field23_1);
        }
        if(field24_1 != -1 || field24_2 != -1) {
            buffer.writeByte(24);
            buffer.writeShort(field24_1);
            buffer.writeShort(field24_2);
        }
        if(field25 != -1) {
            buffer.writeByte(25);
            buffer.writeBigSmart(field25);
        }
        if(field28 != -1) {
            buffer.writeByte(28);
            buffer.writeByte(field28);
        }
        buffer.writeByte(29);
        buffer.writeByte(horizontalAlignment);
        buffer.writeByte(30);
        buffer.writeByte(verticalAlignment);
        buffer.writeByte(0);
        return buffer;
    }

    public MapElementDefinitions() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(int spriteId) {
        this.spriteId = spriteId;
    }

    public int getOpcode2() {
        return op2;
    }

    public void setOpcode2(int op2) {
        this.op2 = op2;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int[] getField3312() {
        return field3312;
    }

    public void setField3312(int[] field3312) {
        this.field3312 = field3312;
    }

    public int getField3313() {
        return field3313;
    }

    public void setField3313(int field3313) {
        this.field3313 = field3313;
    }

    public int getField3314() {
        return field3314;
    }

    public void setField3314(int field3314) {
        this.field3314 = field3314;
    }

    public int getField3315() {
        return field3315;
    }

    public void setField3315(int field3315) {
        this.field3315 = field3315;
    }

    public int getField3316() {
        return field3316;
    }

    public void setField3316(int field3316) {
        this.field3316 = field3316;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public int[] getField3307() {
        return field3307;
    }

    public void setField3307(int[] field3307) {
        this.field3307 = field3307;
    }

    public byte[] getField3320() {
        return field3320;
    }

    public void setField3320(byte[] field3320) {
        this.field3320 = field3320;
    }

    public int getTooltipId() {
        return tooltipId;
    }

    public void setTooltipId(int tooltipId) {
        this.tooltipId = tooltipId;
    }

    @Override
    public String toString() {
        return "MapElementDefinitions(id=" + this.getId() + ", spriteId=" + this.getSpriteId() + ", field3306=" + this.getOpcode2() + ", text=" + this.getText() + ", colour=" + this.getColour() + ", textSize=" + this.getTextSize() + ", options=" + Arrays.deepToString(this.getOptions()) + ", optionName=" + this.getOptionName() + ", field3312=" + Arrays.toString(this.getField3312()) + ", field3313=" + this.getField3313() + ", field3314=" + this.getField3314() + ", field3315=" + this.getField3315() + ", field3316=" + this.getField3316() + ", horizontalAlignment=" + this.getHorizontalAlignment() + ", verticalAlignment=" + this.getVerticalAlignment() + ", field3307=" + Arrays.toString(this.getField3307()) + ", field3320=" + Arrays.toString(this.getField3320()) + ", tooltipId=" + this.getTooltipId() + ")";
    }

    public static class Builder {
        private int id;
        private int spriteId = -1;
        private int op2 = -1;
        private String text;
        private int colour;
        private int textSize = 0;
        private String[] options = new String[5];
        private String optionName;
        private int[] field3312 = new int[0];
        private int field3313 = Integer.MAX_VALUE;
        private int field3314 = Integer.MAX_VALUE;
        private int field3315 = Integer.MIN_VALUE;
        private int field3316 = Integer.MIN_VALUE;
        private int horizontalAlignment = 1;
        private int verticalAlignment = 1;
        private int[] field3307 = new int[0];
        private byte[] field3320 = new byte[0];
        private int tooltipId = -1;
        private int field5 = -1;
        private int field7 = -1;
        private int field8 = -1;
        private int field18 = -1;
        private int field21 = -1;
        private int field22 = -1;
        private int field23_1 = -1;
        private int field23_2 = -1;
        private int field23_3 = -1;
        private int field24_1 = -1;
        private int field24_2 = -1;
        private int field25 = -1;
        private int field28 = -1;
        private int field15_sub = -1;

        public Builder id(int id) { this.id = id; return this; }
        public Builder spriteId(int spriteId) { this.spriteId = spriteId; return this; }
        public Builder op2(int op2) { this.op2 = op2; return this; }
        public Builder text(String text) { this.text = text; return this; }
        public Builder colour(int colour) { this.colour = colour; return this; }
        public Builder textSize(int textSize) { this.textSize = textSize; return this; }
        public Builder options(String... options) { this.options = options; return this; }
        public Builder optionName(String optionName) { this.optionName = optionName; return this; }
        public Builder field3312(int... coords) { this.field3312 = coords; return this; }
        public Builder field3313(int v) { this.field3313 = v; return this; }
        public Builder field3314(int v) { this.field3314 = v; return this; }
        public Builder field3315(int v) { this.field3315 = v; return this; }
        public Builder field3316(int v) { this.field3316 = v; return this; }
        public Builder horizontalAlignment(int ha) { this.horizontalAlignment = ha; return this; }
        public Builder verticalAlignment(int va) { this.verticalAlignment = va; return this; }
        public Builder field3307(int... ints) { this.field3307 = ints; return this; }
        public Builder field3320(byte... flags) { this.field3320 = flags; return this; }
        public Builder tooltipId(int tooltipId) { this.tooltipId = tooltipId; return this; }
        public Builder field5(int v) { this.field5 = v; return this; }
        public Builder field7(int v) { this.field7 = v; return this; }
        public Builder field8(int v) { this.field8 = v; return this; }
        public Builder field18(int v) { this.field18 = v; return this; }
        public Builder field21(int v) { this.field21 = v; return this; }
        public Builder field22(int v) { this.field22 = v; return this; }
        public Builder field23(int v1, int v2, int v3) { this.field23_1 = v1; this.field23_2 = v2; this.field23_3 = v3; return this; }
        public Builder field24(int v1, int v2) { this.field24_1 = v1; this.field24_2 = v2; return this; }
        public Builder field25(int v) { this.field25 = v; return this; }
        public Builder field28(int v) { this.field28 = v; return this; }
        public Builder field15_sub(int v) { this.field15_sub = v; return this; }

        public MapElementDefinitions build() {
            final MapElementDefinitions def = new MapElementDefinitions();
            final int id = this.id;
            def.id = id;
            def.spriteId = this.spriteId;
            def.op2 = this.op2;
            def.text = this.text;
            def.colour = this.colour;
            def.textSize = this.textSize;
            def.options = this.options;
            def.optionName = this.optionName;
            def.field3312 = this.field3312;
            def.field3313 = this.field3313;
            def.field3314 = this.field3314;
            def.field3315 = this.field3315;
            def.field3316 = this.field3316;
            def.horizontalAlignment = this.horizontalAlignment;
            def.verticalAlignment = this.verticalAlignment;
            def.field3307 = this.field3307;
            def.field3320 = this.field3320;
            def.tooltipId = this.tooltipId;
            def.field5 = this.field5;
            def.field7 = this.field7;
            def.field8 = this.field8;
            def.field18 = this.field18;
            def.field21 = this.field21;
            def.field22 = this.field22;
            def.field23_1 = this.field23_1;
            def.field23_2 = this.field23_2;
            def.field23_3 = this.field23_3;
            def.field24_1 = this.field24_1;
            def.field24_2 = this.field24_2;
            def.field25 = this.field25;
            def.field28 = this.field28;
            def.field15_sub = this.field15_sub;
            return def;
        }
    }

    public Builder toBuilder() {
        return new Builder()
                .id(this.id)
                .spriteId(this.spriteId)
                .op2(this.op2)
                .text(this.text)
                .colour(this.colour)
                .textSize(this.textSize)
                .options(this.options)
                .optionName(this.optionName)
                .field3312(this.field3312)
                .field3313(this.field3313)
                .field3314(this.field3314)
                .field3315(this.field3315)
                .field3316(this.field3316)
                .horizontalAlignment(this.horizontalAlignment)
                .verticalAlignment(this.verticalAlignment)
                .field3307(this.field3307)
                .field3320(this.field3320)
                .tooltipId(this.tooltipId)
                .field5(this.field5)
                .field7(this.field7)
                .field8(this.field8)
                .field18(this.field18)
                .field21(this.field21)
                .field22(this.field22)
                .field23(this.field23_1, this.field23_2, this.field23_3)
                .field24(this.field24_1, this.field24_2)
                .field25(this.field25)
                .field28(this.field28)
                .field15_sub(this.field15_sub);
    }

}
