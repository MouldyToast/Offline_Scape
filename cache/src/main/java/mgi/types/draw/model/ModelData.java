package mgi.types.draw.model;

import mgi.types.draw.Rasterizer3D;
import mgi.utilities.Buffer;

public class ModelData extends Renderable {

    static int[] field2314;
    static int[] field2290;
    static int field2308;
    static int[] ModelData_sine;
    static int[] ModelData_cosine;
    int verticesCount;
    float[] verticesX;
    float[] verticesY;
    float[] verticesZ;
    int trianglesCount;
    int[] trianglesX;
    int[] trianglesY;
    int[] trianglesZ;
    byte[] types;
    byte[] priorities;
    byte[] alphas;
    byte[] textures;
    short[] colors;
    short[] materials;
    byte priority;
    int texturesCount;
    byte[] textureTypes;
    short[] texturesX;
    short[] texturesY;
    short[] texturesZ;
    int[] vertexData;
    int[] triangleData;
    int[][] vertexLabels;
    int[][] faceLabelsAlpha;
    int[][] field2095;
    int[][] field2096;

    FaceNormal[] faceNormals;
    VertexNormal[] vertexNormals;
    VertexNormal[] vertexVertices;

    public short ambient;
    public short contrast;

    boolean isBoundsCalculated;

    int field2309;
    int field2310;
    int field2315;
    int field2312;
    int field2313;

    static {
        field2314 = new int[10000]; // L: 49
        field2290 = new int[10000]; // L: 50
        field2308 = 0; // L: 51
        ModelData_sine = Rasterizer3D.Rasterizer3D_sine; // L: 52
        ModelData_cosine = Rasterizer3D.Rasterizer3D_cosine; // L: 53
    }

    ModelData() {
        this.verticesCount = 0;
        this.trianglesCount = 0;
        this.priority = 0;
        this.isBoundsCalculated = false;
    } // L: 55

    public ModelData(ModelData[] var1, int var2) {
        this.verticesCount = 0;
        this.trianglesCount = 0;
        this.priority = 0;
        this.isBoundsCalculated = false;
        boolean var3 = false; // L: 921
        boolean var4 = false; // L: 922
        boolean var5 = false; // L: 923
        boolean var6 = false; // L: 924
        boolean var7 = false; // L: 925
        boolean var8 = false; // L: 926
        boolean var9 = false; // L: 927
        this.verticesCount = 0; // L: 928
        this.trianglesCount = 0; // L: 929
        this.texturesCount = 0; // L: 930
        this.priority = -1; // L: 931

        int var10;
        ModelData var11;
        for (var10 = 0; var10 < var2; ++var10) { // L: 932
            var11 = var1[var10]; // L: 933
            if (var11 != null) { // L: 934
                this.verticesCount += var11.verticesCount; // L: 935
                this.trianglesCount += var11.trianglesCount; // L: 936
                this.texturesCount += var11.texturesCount; // L: 937
                if (var11.priorities != null) { // L: 938
                    var4 = true;
                } else {
                    if (this.priority == -1) { // L: 940
                        this.priority = var11.priority;
                    }

                    if (this.priority != var11.priority) { // L: 941
                        var4 = true;
                    }
                }

                var3 |= var11.types != null; // L: 943
                var5 |= var11.alphas != null; // L: 944
                var6 |= var11.triangleData != null; // L: 945
                var7 |= var11.materials != null; // L: 946
                var8 |= var11.textures != null; // L: 947
                var9 |= var11.field2095 != null; // L: 948
            }
        }

        this.verticesX = new float[this.verticesCount]; // L: 951
        this.verticesY = new float[this.verticesCount]; // L: 952
        this.verticesZ = new float[this.verticesCount]; // L: 953
        this.vertexData = new int[this.verticesCount]; // L: 954
        this.trianglesX = new int[this.trianglesCount]; // L: 955
        this.trianglesY = new int[this.trianglesCount]; // L: 956
        this.trianglesZ = new int[this.trianglesCount]; // L: 957
        if (var3) { // L: 958
            this.types = new byte[this.trianglesCount];
        }

        if (var4) { // L: 959
            this.priorities = new byte[this.trianglesCount];
        }

        if (var5) { // L: 960
            this.alphas = new byte[this.trianglesCount];
        }

        if (var6) { // L: 961
            this.triangleData = new int[this.trianglesCount];
        }

        if (var7) { // L: 962
            this.materials = new short[this.trianglesCount];
        }

        if (var8) { // L: 963
            this.textures = new byte[this.trianglesCount];
        }

        if (var9) { // L: 964
            this.field2095 = new int[this.verticesCount][]; // L: 965
            this.field2096 = new int[this.verticesCount][]; // L: 966
        }

        this.colors = new short[this.trianglesCount]; // L: 968
        if (this.texturesCount > 0) { // L: 969
            this.textureTypes = new byte[this.texturesCount]; // L: 970
            this.texturesX = new short[this.texturesCount]; // L: 971
            this.texturesY = new short[this.texturesCount]; // L: 972
            this.texturesZ = new short[this.texturesCount]; // L: 973
        }

        this.verticesCount = 0; // L: 975
        this.trianglesCount = 0; // L: 976
        this.texturesCount = 0; // L: 977

        for (var10 = 0; var10 < var2; ++var10) { // L: 978
            var11 = var1[var10]; // L: 979
            if (var11 != null) { // L: 980
                int var12;
                for (var12 = 0; var12 < var11.trianglesCount; ++var12) { // L: 981
                    if (var3 && var11.types != null) { // L: 982
                        this.types[this.trianglesCount] = var11.types[var12]; // L: 983
                    }

                    if (var4) { // L: 985
                        if (var11.priorities != null) { // L: 986
                            this.priorities[this.trianglesCount] = var11.priorities[var12];
                        } else {
                            this.priorities[this.trianglesCount] = var11.priority; // L: 987
                        }
                    }

                    if (var5 && var11.alphas != null) { // L: 989 990
                        this.alphas[this.trianglesCount] = var11.alphas[var12];
                    }

                    if (var6 && var11.triangleData != null) { // L: 992 993
                        this.triangleData[this.trianglesCount] = var11.triangleData[var12];
                    }

                    if (var7) { // L: 995
                        if (var11.materials != null) { // L: 996
                            this.materials[this.trianglesCount] = var11.materials[var12];
                        } else {
                            this.materials[this.trianglesCount] = -1; // L: 997
                        }
                    }

                    if (var8) { // L: 999
                        if (var11.textures != null && var11.textures[var12] != -1) { // L: 1000
                            this.textures[this.trianglesCount] = (byte)(this.texturesCount + var11.textures[var12]);
                        } else {
                            this.textures[this.trianglesCount] = -1; // L: 1001
                        }
                    }

                    this.colors[this.trianglesCount] = var11.colors[var12]; // L: 1003
                    this.trianglesX[this.trianglesCount] = this.method3811(var11, var11.trianglesX[var12]); // L: 1004
                    this.trianglesY[this.trianglesCount] = this.method3811(var11, var11.trianglesY[var12]); // L: 1005
                    this.trianglesZ[this.trianglesCount] = this.method3811(var11, var11.trianglesZ[var12]); // L: 1006
                    ++this.trianglesCount; // L: 1007
                }

                for (var12 = 0; var12 < var11.texturesCount; ++var12) { // L: 1009
                    byte var13 = this.textureTypes[this.texturesCount] = var11.textureTypes[var12]; // L: 1010
                    if (var13 == 0) { // L: 1011
                        this.texturesX[this.texturesCount] = (short)this.method3811(var11, var11.texturesX[var12]); // L: 1012
                        this.texturesY[this.texturesCount] = (short)this.method3811(var11, var11.texturesY[var12]); // L: 1013
                        this.texturesZ[this.texturesCount] = (short)this.method3811(var11, var11.texturesZ[var12]); // L: 1014
                    }

                    ++this.texturesCount; // L: 1016
                }
            }
        }

    } // L: 1020

    public ModelData(byte[] var1) {
        this.verticesCount = 0;
        this.trianglesCount = 0;
        this.priority = 0;
        this.isBoundsCalculated = false;
        Buffer var2 = new Buffer(new byte[10]); // L: 64
        var2.writeShort(-2); // L: 65
        if (var1[var1.length - 1] == -7 && var1[var1.length - 2] == -7) {
            System.out.println("Decoding RS2 Model.");
            this.decodeRS2(var1);
            processTextures(true);
            if (version > 12) {
                downscale(2);
            }
        } else if (var1[var1.length - 1] == -7 && var1[var1.length - 2] == -6) {
            System.out.println("Decoding 317 OSRS based Model.");
            decode317OSRS(var1);
        }if (var1[var1.length - 1] == -3 && var1[var1.length - 2] == -1) { // L: 66
            this.method3812(var1);
        } else if (var1[var1.length - 1] == -2 && var1[var1.length - 2] == -1) { // L: 67
            this.method3889(var1);
        } else if (var1[var1.length - 1] == -1 && var1[var1.length - 2] == -1) { // L: 68
            this.method3814(var1);
        } else {
            this.method3815(var1); // L: 69
        }

    } // L: 70

    public void downscale(int i) {
        for (int i1 = 0; i1 < verticesCount; i1++) {
            verticesX[i1] = (int) verticesX[i1] >> i;
            verticesY[i1] = (int) verticesY[i1] >> i;
            verticesZ[i1] = (int) verticesZ[i1] >> i;
        }
    }

    public void processTextures(final boolean detexture) {
        if (detexture) {
            texturesCount = 0;
            materials = null;
            textures = null;
        }
        if (texturesCount <= 0 || materials == null || textures == null) {
            return;
        }
    }

    int version = 12;

    void decode317OSRS(final byte[] data) {
        boolean var2 = false;
        boolean var3 = false;
        final Buffer var4 = new Buffer(data);
        final Buffer var5 = new Buffer(data);
        final Buffer var6 = new Buffer(data);
        final Buffer var7 = new Buffer(data);
        final Buffer var8 = new Buffer(data);
        var4.offset = data.length - 20;
        final int vertexCount = var4.readUnsignedShort();
        final int triCount = var4.readUnsignedShort();
        final int texCount = var4.readUnsignedByte();
        final int hasMaterials = var4.readUnsignedByte();
        final int hasDefaultPriority = var4.readUnsignedByte();
        final int hasAlphas = var4.readUnsignedByte();
        final int hasTriData = var4.readUnsignedByte();
        final int hasVertexData = var4.readUnsignedByte();
        final int var17 = var4.readUnsignedShort();
        final int var18 = var4.readUnsignedShort();
        final int var19 = var4.readUnsignedShort();
        final int var20 = var4.readUnsignedShort();
        final byte vertexStartIdx = 0;
        int var45 = vertexStartIdx + vertexCount;
        final int triStartIdx = var45;
        var45 += triCount;
        final int priorityStartIdx = var45;
        if (hasDefaultPriority == 255) {
            var45 += triCount;
        }
        final int triDataStartIdx = var45;
        if (hasTriData == 1) {
            var45 += triCount;
        }
        final int materialStartIdx = var45;
        if (hasMaterials == 1) {
            var45 += triCount;
        }
        final int vertexDataStartIdx = var45;
        if (hasVertexData == 1) {
            var45 += vertexCount;
        }
        final int alphaStartIdx = var45;
        if (hasAlphas == 1) {
            var45 += triCount;
        }
        final int var29 = var45;
        var45 += var20;
        final int var30 = var45;
        var45 += triCount * 2;
        final int texturePosStartIdx = var45;
        var45 += texCount * 6;
        final int var32 = var45;
        var45 += var17;
        final int var33 = var45;
        var45 += var18;
        this.verticesCount = vertexCount;
        this.trianglesCount = triCount;
        this.texturesCount = texCount;
        this.verticesX = new float[vertexCount];
        this.verticesY = new float[vertexCount];
        this.verticesZ = new float[vertexCount];
        this.trianglesX = new int[triCount];
        this.trianglesY = new int[triCount];
        this.trianglesZ = new int[triCount];
        if (texCount > 0) {
            this.textureTypes = new byte[texCount];
            this.texturesX = new short[texCount];
            this.texturesY = new short[texCount];
            this.texturesZ = new short[texCount];
        }
        if (hasVertexData == 1) {
            this.vertexData = new int[vertexCount];
        }
        if (hasMaterials == 1) {
            this.types = new byte[triCount];
            this.textures = new byte[triCount];
            this.materials = new short[triCount];
        }
        if (hasDefaultPriority == 255) {
            this.priorities = new byte[triCount];
        } else {
            this.priority = (byte) hasDefaultPriority;
        }
        if (hasAlphas == 1) {
            this.alphas = new byte[triCount];
        }
        if (hasTriData == 1) {
            this.triangleData = new int[triCount];
        }
        this.colors = new short[triCount];
        var4.offset = vertexStartIdx;
        var5.offset = var32;
        var6.offset = var33;
        var7.offset = var45;
        var8.offset = vertexDataStartIdx;
        int var35 = 0;
        int var36 = 0;
        int var37 = 0;
        int var38;
        int var39;
        int var40;
        int var41;
        int var42;
        for (int vertexIndex = 0; vertexIndex < vertexCount; ++vertexIndex) {
            var39 = var4.readUnsignedByte();
            var40 = 0;
            if ((var39 & 1) != 0) {
                var40 = var5.readShortSmart();
            }
            var41 = 0;
            if ((var39 & 2) != 0) {
                var41 = var6.readShortSmart();
            }
            var42 = 0;
            if ((var39 & 4) != 0) {
                var42 = var7.readShortSmart();
            }
            this.verticesX[vertexIndex] = var35 + var40;
            this.verticesY[vertexIndex] = var36 + var41;
            this.verticesZ[vertexIndex] = var37 + var42;
            var35 = (int) this.verticesX[vertexIndex];
            var36 = (int) this.verticesY[vertexIndex];
            var37 = (int) this.verticesZ[vertexIndex];
            if (hasVertexData == 1) {
                this.vertexData[vertexIndex] = var8.readUnsignedByte();
            }
        }
        var4.offset = var30;
        var5.offset = materialStartIdx;
        var6.offset = priorityStartIdx;
        var7.offset = alphaStartIdx;
        var8.offset = triDataStartIdx;
        for (var38 = 0; var38 < triCount; ++var38) {
            this.colors[var38] = (short) var4.readUnsignedShort();
            if (hasMaterials == 1) {
                var39 = var5.readUnsignedByte();
                if ((var39 & 1) == 1) {
                    this.types[var38] = 1;
                    var2 = true;
                } else {
                    this.types[var38] = 0;
                }
                if ((var39 & 2) == 2) {
                    this.textures[var38] = (byte) (var39 >> 2);
                    this.materials[var38] = this.colors[var38];
                    this.colors[var38] = 127;
                    if (this.materials[var38] != -1) {
                        var3 = true;
                    }
                } else {
                    this.textures[var38] = -1;
                    this.materials[var38] = -1;
                }
            }
            if (hasDefaultPriority == 255) {
                this.priorities[var38] = var6.readByte();
            }
            if (hasAlphas == 1) {
                this.alphas[var38] = var7.readByte();
                if (alphas[var38] < 0) {
                    alphas[var38] = (byte) (256 + alphas[var38]);
                }
            }
            if (hasTriData == 1) {
                this.triangleData[var38] = var8.readUnsignedByte();
            }
        }
        var4.offset = var29;
        var5.offset = triStartIdx;
        var38 = 0;
        var39 = 0;
        var40 = 0;
        var41 = 0;
        int var43;
        int var44;
        for (var42 = 0; var42 < triCount; ++var42) {
            var43 = var5.readUnsignedByte();
            if (var43 == 1) {
                var38 = var4.readShortSmart() + var41;
                var39 = var4.readShortSmart() + var38;
                var40 = var4.readShortSmart() + var39;
                var41 = var40;
                this.trianglesX[var42] = var38;
                this.trianglesY[var42] = var39;
                this.trianglesZ[var42] = var40;
            }
            if (var43 == 2) {
                var39 = var40;
                var40 = var4.readShortSmart() + var41;
                var41 = var40;
                this.trianglesX[var42] = var38;
                this.trianglesY[var42] = var39;
                this.trianglesZ[var42] = var40;
            }
            if (var43 == 3) {
                var38 = var40;
                var40 = var4.readShortSmart() + var41;
                var41 = var40;
                this.trianglesX[var42] = var38;
                this.trianglesY[var42] = var39;
                this.trianglesZ[var42] = var40;
            }
            if (var43 == 4) {
                var44 = var38;
                var38 = var39;
                var39 = var44;
                var40 = var4.readShortSmart() + var41;
                var41 = var40;
                this.trianglesX[var42] = var38;
                this.trianglesY[var42] = var44;
                this.trianglesZ[var42] = var40;
            }
        }
        var4.offset = texturePosStartIdx;
        for (var42 = 0; var42 < texCount; ++var42) {
            this.textureTypes[var42] = 0;
            this.texturesX[var42] = (short) var4.readUnsignedShort();
            this.texturesY[var42] = (short) var4.readUnsignedShort();
            this.texturesZ[var42] = (short) var4.readUnsignedShort();
        }
        if (this.textures != null) {
            boolean var46 = false;
            for (var43 = 0; var43 < triCount; ++var43) {
                var44 = this.textures[var43] & 255;
                if (var44 != 255) {
                    if (this.trianglesX[var43] == (this.texturesX[var44] & '\uffff') && this.trianglesY[var43] == (this.texturesY[var44] & '\uffff') && this.trianglesZ[var43] == (this.texturesZ[var44] & '\uffff')) {
                        this.textures[var43] = -1;
                    } else {
                        var46 = true;
                    }
                }
            }
            if (!var46) {
                this.textures = null;
            }
        }
        if (!var3) {
            this.materials = null;
        }
        if (!var2) {
            this.types = null;
        }
    }
    
    void decodeRS2(final byte[] data) {
        final Buffer first = new Buffer(data);
        final Buffer second = new Buffer(data);
        final Buffer third = new Buffer(data);
        final Buffer fourth = new Buffer(data);
        final Buffer fifth = new Buffer(data);
        final Buffer sixth = new Buffer(data);
        final Buffer seventh = new Buffer(data);
        first.offset = data.length - 23;
        verticesCount = first.readUnsignedShort();
        trianglesCount = first.readUnsignedShort();
        texturesCount = first.readUnsignedByte();
        final int properties = first.readUnsignedByte();
        final boolean hasTypes = (properties & 0x1) == 1;
        final boolean hasParticles = (properties & 0x2) == 2;
        final boolean bool_13_ = (properties & 0x4) == 4;
        final boolean hasVersion = (properties & 0x8) == 8;
        if (hasVersion) {
            first.offset -= 7;
            version = first.readUnsignedByte();
            first.offset += 6;
        }
        final int hasPriorities = first.readUnsignedByte();
        final int hasAlphas = first.readUnsignedByte();
        final int hasTriangleData = first.readUnsignedByte();
        final int hasTextures = first.readUnsignedByte();
        final int hasVertexData = first.readUnsignedByte();
        final int i_20_ = first.readUnsignedShort();
        final int i_21_ = first.readUnsignedShort();
        final int i_22_ = first.readUnsignedShort();
        final int i_23_ = first.readUnsignedShort();
        final int i_24_ = first.readUnsignedShort();
        int simpleTexturesCount = 0;
        int complexTexturesCount = 0;
        int cubeTexturesCount = 0;
        if (texturesCount > 0) {
            textureTypes = new byte[texturesCount];
            first.offset = 0;
            for (int index = 0; index < texturesCount; index++) {
                final byte type = textureTypes[index] = first.readByte();
                if (type == 0) {
                    simpleTexturesCount++;
                }
                if (type >= 1 && type <= 3) {
                    complexTexturesCount++;
                }
                if (type == 2) {
                    cubeTexturesCount++;
                }
            }
        }
        int i_30_ = texturesCount;
        final int i_31_ = i_30_;
        i_30_ += verticesCount;
        final int i_32_ = i_30_;
        if (hasTypes) {
            i_30_ += trianglesCount;
        }
        final int i_33_ = i_30_;
        i_30_ += trianglesCount;
        final int i_34_ = i_30_;
        if (hasPriorities == 255) {
            i_30_ += trianglesCount;
        }
        final int i_35_ = i_30_;
        if (hasTriangleData == 1) {
            i_30_ += trianglesCount;
        }
        final int i_36_ = i_30_;
        if (hasVertexData == 1) {
            i_30_ += verticesCount;
        }
        final int i_37_ = i_30_;
        if (hasAlphas == 1) {
            i_30_ += trianglesCount;
        }
        final int i_38_ = i_30_;
        i_30_ += i_23_;
        final int i_39_ = i_30_;
        if (hasTextures == 1) {
            i_30_ += trianglesCount * 2;
        }
        final int i_40_ = i_30_;
        i_30_ += i_24_;
        final int i_41_ = i_30_;
        i_30_ += trianglesCount * 2;
        final int i_42_ = i_30_;
        i_30_ += i_20_;
        final int i_43_ = i_30_;
        i_30_ += i_21_;
        final int i_44_ = i_30_;
        i_30_ += i_22_;
        final int i_45_ = i_30_;
        i_30_ += simpleTexturesCount * 6;
        final int i_46_ = i_30_;
        i_30_ += complexTexturesCount * 6;
        int i_47_ = 6;
        if (version == 14) {
            i_47_ = 7;
        } else if (version >= 15) {
            i_47_ = 9;
        }
        final int i_48_ = i_30_;
        i_30_ += complexTexturesCount * i_47_;
        final int i_49_ = i_30_;
        i_30_ += complexTexturesCount;
        final int i_50_ = i_30_;
        i_30_ += complexTexturesCount;
        final int i_51_ = i_30_;
        i_30_ += complexTexturesCount + cubeTexturesCount * 2;
        final int i_52_ = i_30_;
        verticesX = new float[verticesCount];
        verticesY = new float[verticesCount];
        verticesZ = new float[verticesCount];
        trianglesX = new int[trianglesCount];
        trianglesY = new int[trianglesCount];
        trianglesZ = new int[trianglesCount];
        if (hasVertexData == 1) {
            vertexData = new int[verticesCount];
        }
        if (hasTypes) {
            types = new byte[trianglesCount];
        }
        if (hasPriorities == 255) {
            priorities = new byte[trianglesCount];
        } else {
            priority = (byte) hasPriorities;
        }
        if (hasAlphas == 1) {
            alphas = new byte[trianglesCount];
        }
        if (hasTriangleData == 1) {
            triangleData = new int[trianglesCount];
        }
        if (hasTextures == 1) {
            materials = new short[trianglesCount];
        }
        if (hasTextures == 1 && texturesCount > 0) {
            textures = new byte[trianglesCount];
        }
        colors = new short[trianglesCount];
        if (texturesCount > 0) {
            texturesX = new short[texturesCount];
            texturesY = new short[texturesCount];
            texturesZ = new short[texturesCount];
        }
        first.offset = i_31_;
        second.offset = i_42_;
        third.offset = i_43_;
        fourth.offset = i_44_;
        fifth.offset = i_36_;
        float verticeX = 0;
        float verticeY = 0;
        float verticeZ = 0;
        for (int index = 0; index < verticesCount; index++) {
            final int mask = first.readUnsignedByte();
            int x = 0;
            if ((mask & 0x1) != 0) {
                x = second.readShortSmart();
            }
            int y = 0;
            if ((mask & 0x2) != 0) {
                y = third.readShortSmart();
            }
            int z = 0;
            if ((mask & 0x4) != 0) {
                z = fourth.readShortSmart();
            }
            verticesX[index] = verticeX + x;
            verticesY[index] = verticeY + y;
            verticesZ[index] = verticeZ + z;
            verticeX = verticesX[index];
            verticeY = verticesY[index];
            verticeZ = verticesZ[index];
            if (hasVertexData == 1) {
                vertexData[index] = fifth.readUnsignedByte();
            }
        }
        first.offset = i_41_;
        second.offset = i_32_;
        third.offset = i_34_;
        fourth.offset = i_37_;
        fifth.offset = i_35_;
        sixth.offset = i_39_;
        seventh.offset = i_40_;
        for (int index = 0; index < trianglesCount; index++) {
            colors[index] = (short) first.readUnsignedShort();
            if (hasTypes) {
                types[index] = second.readByte();
            }
            if (hasPriorities == 255) {
                priorities[index] = third.readByte();
            }
            if (hasAlphas == 1) {
                alphas[index] = fourth.readByte();
                if (alphas[index] < 0) {
                    alphas[index] = (byte) (256 + alphas[index]);
                }
            }
            if (hasTriangleData == 1) {
                triangleData[index] = fifth.readUnsignedByte();
            }
            if (hasTextures == 1) {
                materials[index] = (short) (sixth.readUnsignedShort() - 1);
            }
            if (textures != null) {
                if (materials[index] != -1) {
                    textures[index] = (byte) (seventh.readUnsignedByte() - 1);
                } else {
                    textures[index] = (byte) -1;
                }
            }
        }
        first.offset = i_38_;
        second.offset = i_33_;
        short triangleX = 0;
        short triangleY = 0;
        short triangleZ = 0;
        int variable = 0;
        for (int index = 0; index < trianglesCount; index++) {
            final int type = second.readUnsignedByte();
            if (type == 1) {
                triangleX = (short) (first.readShortSmart() + variable);
                variable = triangleX;
                triangleY = (short) (first.readShortSmart() + variable);
                variable = triangleY;
                triangleZ = (short) (first.readShortSmart() + variable);
                variable = triangleZ;
                trianglesX[index] = triangleX;
                trianglesY[index] = triangleY;
                trianglesZ[index] = triangleZ;
            }
            if (type == 2) {
                triangleY = triangleZ;
                triangleZ = (short) (first.readShortSmart() + variable);
                variable = triangleZ;
                trianglesX[index] = triangleX;
                trianglesY[index] = triangleY;
                trianglesZ[index] = triangleZ;
            }
            if (type == 3) {
                triangleX = triangleZ;
                triangleZ = (short) (first.readShortSmart() + variable);
                variable = triangleZ;
                trianglesX[index] = triangleX;
                trianglesY[index] = triangleY;
                trianglesZ[index] = triangleZ;
            }
            if (type == 4) {
                final short save = triangleX;
                triangleX = triangleY;
                triangleY = save;
                triangleZ = (short) (first.readShortSmart() + variable);
                variable = triangleZ;
                trianglesX[index] = triangleX;
                trianglesY[index] = triangleY;
                trianglesZ[index] = triangleZ;
            }
        }
        first.offset = i_45_;
        second.offset = i_46_;
        third.offset = i_48_;
        fourth.offset = i_49_;
        fifth.offset = i_50_;
        sixth.offset = i_51_;
        for (int index = 0; index < texturesCount; index++) {
            final int type = textureTypes[index] & 0xff;
            if (type == 0) {
                texturesX[index] = (short) first.readUnsignedShort();
                texturesY[index] = (short) first.readUnsignedShort();
                texturesZ[index] = (short) first.readUnsignedShort();
            }
            if (type == 1) {
                texturesX[index] = (short) second.readUnsignedShort();
                texturesY[index] = (short) second.readUnsignedShort();
                texturesZ[index] = (short) second.readUnsignedShort();
                if (version < 15) {
                    third.readUnsignedShort();
                    if (version < 14) {
                        third.readUnsignedShort();
                    } else {
                        third.readMedium();
                    }
                    third.readUnsignedShort();
                } else {
                    third.readMedium();
                    third.readMedium();
                    third.readMedium();
                }
                fourth.readByte();
                fifth.readByte();
                sixth.readByte();
            }
            if (type == 2) {
                texturesX[index] = (short) second.readUnsignedShort();
                texturesY[index] = (short) second.readUnsignedShort();
                texturesZ[index] = (short) second.readUnsignedShort();
                if (version < 15) {
                    third.readUnsignedShort();
                    if (version < 14) {
                        third.readUnsignedShort();
                    } else {
                        third.readMedium();
                    }
                    third.readUnsignedShort();
                } else {
                    third.readMedium();
                    third.readMedium();
                    third.readMedium();
                }
                fourth.readByte();
                fifth.readByte();
                sixth.readByte();
                sixth.readByte();
                sixth.readByte();
            }
            if (type == 3) {
                texturesX[index] = (short) second.readUnsignedShort();
                texturesY[index] = (short) second.readUnsignedShort();
                texturesZ[index] = (short) second.readUnsignedShort();
                if (version < 15) {
                    third.readUnsignedShort();
                    if (version < 14) {
                        third.readUnsignedShort();
                    } else {
                        third.readMedium();
                    }
                    third.readUnsignedShort();
                } else {
                    third.readMedium();
                    third.readMedium();
                    third.readMedium();
                }
                fourth.readByte();
                fifth.readByte();
                sixth.readByte();
            }
        }
        first.offset = i_52_;
        if (hasParticles) {
            int amount = first.readUnsignedByte();
            if (amount > 0) {
                for (int index = 0; index < amount; index++) {
                    first.readUnsignedShort();
                    first.readUnsignedShort();
                }
            }
            amount = first.readUnsignedByte();
            if (amount > 0) {
                for (int index = 0; index < amount; index++) {
                    first.readUnsignedShort();
                    first.readUnsignedShort();
                }
            }
        }
        if (bool_13_) {
            final int amount = first.readUnsignedByte();
            if (amount > 0) {
                for (int index = 0; index < amount; index++) {
                    first.readUnsignedShort();
                    first.readUnsignedShort();
                    first.readUnsignedByte();
                    first.readByte();
                }
            }
        }
    }

    public ModelData(ModelData var1, boolean var2, boolean var3, boolean var4, boolean var5) {
        this.verticesCount = 0; // L: 12
        this.trianglesCount = 0; // L: 16
        this.priority = 0; // L: 26
        this.isBoundsCalculated = false; // L: 43
        this.verticesCount = var1.verticesCount; // L: 1048
        this.trianglesCount = var1.trianglesCount; // L: 1049
        this.texturesCount = var1.texturesCount; // L: 1050
        int var6;
        if (var2) { // L: 1051
            this.verticesX = var1.verticesX; // L: 1052
            this.verticesY = var1.verticesY; // L: 1053
            this.verticesZ = var1.verticesZ; // L: 1054
        } else {
            this.verticesX = new float[this.verticesCount]; // L: 1057
            this.verticesY = new float[this.verticesCount]; // L: 1058
            this.verticesZ = new float[this.verticesCount]; // L: 1059

            for (var6 = 0; var6 < this.verticesCount; ++var6) { // L: 1060
                this.verticesX[var6] = var1.verticesX[var6]; // L: 1061
                this.verticesY[var6] = var1.verticesY[var6]; // L: 1062
                this.verticesZ[var6] = var1.verticesZ[var6]; // L: 1063
            }
        }

        if (var3) { // L: 1066
            this.colors = var1.colors;
        } else {
            this.colors = new short[this.trianglesCount]; // L: 1068

            for (var6 = 0; var6 < this.trianglesCount; ++var6) { // L: 1069
                this.colors[var6] = var1.colors[var6];
            }
        }

        if (!var4 && var1.materials != null) { // L: 1071
            this.materials = new short[this.trianglesCount]; // L: 1073

            for (var6 = 0; var6 < this.trianglesCount; ++var6) { // L: 1074
                this.materials[var6] = var1.materials[var6];
            }
        } else {
            this.materials = var1.materials;
        }

        this.alphas = var1.alphas; // L: 1076
        this.trianglesX = var1.trianglesX; // L: 1086
        this.trianglesY = var1.trianglesY; // L: 1087
        this.trianglesZ = var1.trianglesZ; // L: 1088
        this.types = var1.types; // L: 1089
        this.priorities = var1.priorities; // L: 1090
        this.textures = var1.textures; // L: 1091
        this.priority = var1.priority; // L: 1092
        this.textureTypes = var1.textureTypes; // L: 1093
        this.texturesX = var1.texturesX; // L: 1094
        this.texturesY = var1.texturesY; // L: 1095
        this.texturesZ = var1.texturesZ; // L: 1096
        this.vertexData = var1.vertexData; // L: 1097
        this.triangleData = var1.triangleData; // L: 1098
        this.vertexLabels = var1.vertexLabels; // L: 1099
        this.faceLabelsAlpha = var1.faceLabelsAlpha; // L: 1100
        this.vertexNormals = var1.vertexNormals; // L: 1101
        this.faceNormals = var1.faceNormals; // L: 1102
        this.vertexVertices = var1.vertexVertices; // L: 1103
        this.field2095 = var1.field2095; // L: 1104
        this.field2096 = var1.field2096; // L: 1105
        this.ambient = var1.ambient; // L: 1106
        this.contrast = var1.contrast; // L: 1107
    }


    void method3812(byte[] var1) {
        Buffer var2 = new Buffer(var1); // L: 73
        Buffer var3 = new Buffer(var1); // L: 74
        Buffer var4 = new Buffer(var1); // L: 75
        Buffer var5 = new Buffer(var1); // L: 76
        Buffer var6 = new Buffer(var1); // L: 77
        Buffer var7 = new Buffer(var1); // L: 78
        Buffer var8 = new Buffer(var1); // L: 79
        var2.offset = var1.length - 26; // L: 80
        int var9 = var2.readUnsignedShort(); // L: 81
        int var10 = var2.readUnsignedShort(); // L: 82
        int var11 = var2.readUnsignedByte(); // L: 83
        int var12 = var2.readUnsignedByte(); // L: 84
        int var13 = var2.readUnsignedByte(); // L: 85
        int var14 = var2.readUnsignedByte(); // L: 86
        int var15 = var2.readUnsignedByte(); // L: 87
        int var16 = var2.readUnsignedByte(); // L: 88
        int var17 = var2.readUnsignedByte(); // L: 89
        int var18 = var2.readUnsignedByte(); // L: 90
        int var19 = var2.readUnsignedShort(); // L: 91
        int var20 = var2.readUnsignedShort(); // L: 92
        int var21 = var2.readUnsignedShort(); // L: 93
        int var22 = var2.readUnsignedShort(); // L: 94
        int var23 = var2.readUnsignedShort(); // L: 95
        int var24 = var2.readUnsignedShort(); // L: 96
        int var25 = 0; // L: 97
        int var26 = 0; // L: 98
        int var27 = 0; // L: 99
        int var28;
        if (var11 > 0) { // L: 100
            this.textureTypes = new byte[var11]; // L: 101
            var2.offset = 0; // L: 102

            for (var28 = 0; var28 < var11; ++var28) { // L: 103
                byte var29 = this.textureTypes[var28] = var2.readByte(); // L: 104
                if (var29 == 0) { // L: 105
                    ++var25;
                }

                if (var29 >= 1 && var29 <= 3) { // L: 106
                    ++var26;
                }

                if (var29 == 2) { // L: 107
                    ++var27;
                }
            }
        }

        var28 = var11 + var9; // L: 112
        int var30 = var28; // L: 113
        if (var12 == 1) { // L: 114
            var28 += var10;
        }

        int var31 = var28; // L: 115
        var28 += var10; // L: 116
        int var32 = var28; // L: 117
        if (var13 == 255) { // L: 118
            var28 += var10;
        }

        int var33 = var28; // L: 119
        if (var15 == 1) { // L: 120
            var28 += var10;
        }

        int var34 = var28; // L: 121
        var28 += var24; // L: 122
        int var35 = var28; // L: 123
        if (var14 == 1) { // L: 124
            var28 += var10;
        }

        int var36 = var28; // L: 125
        var28 += var22; // L: 126
        int var37 = var28; // L: 127
        if (var16 == 1) { // L: 128
            var28 += var10 * 2;
        }

        int var38 = var28; // L: 129
        var28 += var23; // L: 130
        int var39 = var28; // L: 131
        var28 += var10 * 2; // L: 132
        int var40 = var28; // L: 133
        var28 += var19; // L: 134
        int var41 = var28; // L: 135
        var28 += var20; // L: 136
        int var42 = var28; // L: 137
        var28 += var21; // L: 138
        int var43 = var28; // L: 139
        var28 += var25 * 6; // L: 140
        int var44 = var28; // L: 141
        var28 += var26 * 6; // L: 142
        int var45 = var28; // L: 143
        var28 += var26 * 6; // L: 144
        int var46 = var28; // L: 145
        var28 += var26 * 2; // L: 146
        int var47 = var28; // L: 147
        var28 += var26; // L: 148
        int var48 = var28; // L: 149
        var28 += var26 * 2 + var27 * 2; // L: 150
        this.verticesCount = var9; // L: 152
        this.trianglesCount = var10; // L: 153
        this.texturesCount = var11; // L: 154
        this.verticesX = new float[var9]; // L: 155
        this.verticesY = new float[var9]; // L: 156
        this.verticesZ = new float[var9]; // L: 157
        this.trianglesX = new int[var10]; // L: 158
        this.trianglesY = new int[var10]; // L: 159
        this.trianglesZ = new int[var10]; // L: 160
        if (var17 == 1) { // L: 161
            this.vertexData = new int[var9];
        }

        if (var12 == 1) { // L: 162
            this.types = new byte[var10];
        }

        if (var13 == 255) { // L: 163
            this.priorities = new byte[var10];
        } else {
            this.priority = (byte)var13; // L: 164
        }

        if (var14 == 1) { // L: 165
            this.alphas = new byte[var10];
        }

        if (var15 == 1) { // L: 166
            this.triangleData = new int[var10];
        }

        if (var16 == 1) { // L: 167
            this.materials = new short[var10];
        }

        if (var16 == 1 && var11 > 0) { // L: 168
            this.textures = new byte[var10];
        }

        if (var18 == 1) { // L: 169
            this.field2095 = new int[var9][]; // L: 170
            this.field2096 = new int[var9][]; // L: 171
        }

        this.colors = new short[var10]; // L: 173
        if (var11 > 0) { // L: 174
            this.texturesX = new short[var11]; // L: 175
            this.texturesY = new short[var11]; // L: 176
            this.texturesZ = new short[var11]; // L: 177
        }

        var2.offset = var11; // L: 179
        var3.offset = var40; // L: 180
        var4.offset = var41; // L: 181
        var5.offset = var42; // L: 182
        var6.offset = var34; // L: 183
        int var50 = 0; // L: 184
        int var51 = 0; // L: 185
        int var52 = 0; // L: 186

        int var53;
        int var54;
        int var55;
        int var56;
        int var57;
        for (var53 = 0; var53 < var9; ++var53) { // L: 187
            var54 = var2.readUnsignedByte(); // L: 188
            var55 = 0; // L: 189
            if ((var54 & 1) != 0) { // L: 190
                var55 = var3.readShortSmart();
            }

            var56 = 0; // L: 191
            if ((var54 & 2) != 0) { // L: 192
                var56 = var4.readShortSmart();
            }

            var57 = 0; // L: 193
            if ((var54 & 4) != 0) { // L: 194
                var57 = var5.readShortSmart();
            }

            this.verticesX[var53] = var50 + var55; // L: 195
            this.verticesY[var53] = var51 + var56; // L: 196
            this.verticesZ[var53] = var52 + var57; // L: 197
            var50 = (int) this.verticesX[var53]; // L: 198
            var51 = (int) this.verticesY[var53]; // L: 199
            var52 = (int) this.verticesZ[var53]; // L: 200
            if (var17 == 1) { // L: 201
                this.vertexData[var53] = var6.readUnsignedByte();
            }
        }

        if (var18 == 1) { // L: 203
            for (var53 = 0; var53 < var9; ++var53) { // L: 204
                var54 = var6.readUnsignedByte(); // L: 205
                this.field2095[var53] = new int[var54]; // L: 206
                this.field2096[var53] = new int[var54]; // L: 207

                for (var55 = 0; var55 < var54; ++var55) { // L: 208
                    this.field2095[var53][var55] = var6.readUnsignedByte(); // L: 209
                    this.field2096[var53][var55] = var6.readUnsignedByte(); // L: 210
                }
            }
        }

        var2.offset = var39; // L: 214
        var3.offset = var30; // L: 215
        var4.offset = var32; // L: 216
        var5.offset = var35; // L: 217
        var6.offset = var33; // L: 218
        var7.offset = var37; // L: 219
        var8.offset = var38; // L: 220

        for (var53 = 0; var53 < var10; ++var53) { // L: 221
            this.colors[var53] = (short)var2.readUnsignedShort(); // L: 222
            if (var12 == 1) { // L: 223
                this.types[var53] = var3.readByte();
            }

            if (var13 == 255) { // L: 224
                this.priorities[var53] = var4.readByte();
            }

            if (var14 == 1) { // L: 225
                this.alphas[var53] = var5.readByte();
            }

            if (var15 == 1) { // L: 226
                this.triangleData[var53] = var6.readUnsignedByte();
            }

            if (var16 == 1) { // L: 227
                this.materials[var53] = (short)(var7.readUnsignedShort() - 1);
            }

            if (this.textures != null && this.materials[var53] != -1) { // L: 228
                this.textures[var53] = (byte)(var8.readUnsignedByte() - 1);
            }
        }

        var2.offset = var36; // L: 230
        var3.offset = var31; // L: 231
        var53 = 0; // L: 232
        var54 = 0; // L: 233
        var55 = 0; // L: 234
        var56 = 0; // L: 235

        int var58;
        for (var57 = 0; var57 < var10; ++var57) { // L: 236
            var58 = var3.readUnsignedByte(); // L: 237
            if (var58 == 1) { // L: 238
                var53 = var2.readShortSmart() + var56; // L: 239
                var54 = var2.readShortSmart() + var53; // L: 241
                var55 = var2.readShortSmart() + var54; // L: 243
                var56 = var55; // L: 244
                this.trianglesX[var57] = var53; // L: 245
                this.trianglesY[var57] = var54; // L: 246
                this.trianglesZ[var57] = var55; // L: 247
            }

            if (var58 == 2) { // L: 249
                var54 = var55; // L: 250
                var55 = var2.readShortSmart() + var56; // L: 251
                var56 = var55; // L: 252
                this.trianglesX[var57] = var53; // L: 253
                this.trianglesY[var57] = var54; // L: 254
                this.trianglesZ[var57] = var55; // L: 255
            }

            if (var58 == 3) { // L: 257
                var53 = var55; // L: 258
                var55 = var2.readShortSmart() + var56; // L: 259
                var56 = var55; // L: 260
                this.trianglesX[var57] = var53; // L: 261
                this.trianglesY[var57] = var54; // L: 262
                this.trianglesZ[var57] = var55; // L: 263
            }

            if (var58 == 4) { // L: 265
                int var59 = var53; // L: 266
                var53 = var54; // L: 267
                var54 = var59; // L: 268
                var55 = var2.readShortSmart() + var56; // L: 269
                var56 = var55; // L: 270
                this.trianglesX[var57] = var53; // L: 271
                this.trianglesY[var57] = var59; // L: 272
                this.trianglesZ[var57] = var55; // L: 273
            }
        }

        var2.offset = var43; // L: 276
        var3.offset = var44; // L: 277
        var4.offset = var45; // L: 278
        var5.offset = var46; // L: 279
        var6.offset = var47; // L: 280
        var7.offset = var48; // L: 281

        for (var57 = 0; var57 < var11; ++var57) { // L: 282
            var58 = this.textureTypes[var57] & 255; // L: 283
            if (var58 == 0) { // L: 284
                this.texturesX[var57] = (short)var2.readUnsignedShort(); // L: 285
                this.texturesY[var57] = (short)var2.readUnsignedShort(); // L: 286
                this.texturesZ[var57] = (short)var2.readUnsignedShort(); // L: 287
            }
        }

        var2.offset = var28; // L: 290
        var57 = var2.readUnsignedByte(); // L: 291
        if (var57 != 0) { // L: 292
            new ModelData0();
            var2.readUnsignedShort(); // L: 294
            var2.readUnsignedShort(); // L: 295
            var2.readUnsignedShort(); // L: 296
            var2.readInt(); // L: 297
        }

    } // L: 299


    void method3889(byte[] var1) {
        boolean var2 = false; // L: 302
        boolean var3 = false; // L: 303
        Buffer var4 = new Buffer(var1); // L: 304
        Buffer var5 = new Buffer(var1); // L: 305
        Buffer var6 = new Buffer(var1); // L: 306
        Buffer var7 = new Buffer(var1); // L: 307
        Buffer var8 = new Buffer(var1); // L: 308
        var4.offset = var1.length - 23; // L: 309
        int var9 = var4.readUnsignedShort(); // L: 310
        int var10 = var4.readUnsignedShort(); // L: 311
        int var11 = var4.readUnsignedByte(); // L: 312
        int var12 = var4.readUnsignedByte(); // L: 313
        int var13 = var4.readUnsignedByte(); // L: 314
        int var14 = var4.readUnsignedByte(); // L: 315
        int var15 = var4.readUnsignedByte(); // L: 316
        int var16 = var4.readUnsignedByte(); // L: 317
        int var17 = var4.readUnsignedByte(); // L: 318
        int var18 = var4.readUnsignedShort(); // L: 319
        int var19 = var4.readUnsignedShort(); // L: 320
        int var20 = var4.readUnsignedShort(); // L: 321
        int var21 = var4.readUnsignedShort(); // L: 322
        int var22 = var4.readUnsignedShort(); // L: 323
        byte var23 = 0; // L: 324
        int var47 = var23 + var9; // L: 326
        int var25 = var47; // L: 327
        var47 += var10; // L: 328
        int var26 = var47; // L: 329
        if (var13 == 255) { // L: 330
            var47 += var10;
        }

        int var27 = var47; // L: 331
        if (var15 == 1) { // L: 332
            var47 += var10;
        }

        int var28 = var47; // L: 333
        if (var12 == 1) { // L: 334
            var47 += var10;
        }

        int var29 = var47; // L: 335
        var47 += var22; // L: 336
        int var30 = var47; // L: 337
        if (var14 == 1) { // L: 338
            var47 += var10;
        }

        int var31 = var47; // L: 339
        var47 += var21; // L: 340
        int var32 = var47; // L: 341
        var47 += var10 * 2; // L: 342
        int var33 = var47; // L: 343
        var47 += var11 * 6; // L: 344
        int var34 = var47; // L: 345
        var47 += var18; // L: 346
        int var35 = var47; // L: 347
        var47 += var19; // L: 348
        int var10000 = var47 + var20; // L: 350
        this.verticesCount = var9; // L: 351
        this.trianglesCount = var10; // L: 352
        this.texturesCount = var11; // L: 353
        this.verticesX = new float[var9]; // L: 354
        this.verticesY = new float[var9]; // L: 355
        this.verticesZ = new float[var9]; // L: 356
        this.trianglesX = new int[var10]; // L: 357
        this.trianglesY = new int[var10]; // L: 358
        this.trianglesZ = new int[var10]; // L: 359
        if (var11 > 0) { // L: 360
            this.textureTypes = new byte[var11]; // L: 361
            this.texturesX = new short[var11]; // L: 362
            this.texturesY = new short[var11]; // L: 363
            this.texturesZ = new short[var11]; // L: 364
        }

        if (var16 == 1) { // L: 366
            this.vertexData = new int[var9];
        }

        if (var12 == 1) { // L: 367
            this.types = new byte[var10]; // L: 368
            this.textures = new byte[var10]; // L: 369
            this.materials = new short[var10]; // L: 370
        }

        if (var13 == 255) { // L: 372
            this.priorities = new byte[var10];
        } else {
            this.priority = (byte)var13; // L: 373
        }

        if (var14 == 1) { // L: 374
            this.alphas = new byte[var10];
        }

        if (var15 == 1) { // L: 375
            this.triangleData = new int[var10];
        }

        if (var17 == 1) { // L: 376
            this.field2095 = new int[var9][]; // L: 377
            this.field2096 = new int[var9][]; // L: 378
        }

        this.colors = new short[var10]; // L: 380
        var4.offset = var23; // L: 381
        var5.offset = var34; // L: 382
        var6.offset = var35; // L: 383
        var7.offset = var47; // L: 384
        var8.offset = var29; // L: 385
        int var37 = 0; // L: 386
        int var38 = 0; // L: 387
        int var39 = 0; // L: 388

        int var40;
        int var41;
        int var42;
        int var43;
        int var44;
        for (var40 = 0; var40 < var9; ++var40) { // L: 389
            var41 = var4.readUnsignedByte(); // L: 390
            var42 = 0; // L: 391
            if ((var41 & 1) != 0) { // L: 392
                var42 = var5.readShortSmart();
            }

            var43 = 0; // L: 393
            if ((var41 & 2) != 0) { // L: 394
                var43 = var6.readShortSmart();
            }

            var44 = 0; // L: 395
            if ((var41 & 4) != 0) { // L: 396
                var44 = var7.readShortSmart();
            }

            this.verticesX[var40] = var37 + var42; // L: 397
            this.verticesY[var40] = var38 + var43; // L: 398
            this.verticesZ[var40] = var39 + var44; // L: 399
            var37 = (int) this.verticesX[var40]; // L: 400
            var38 = (int) this.verticesY[var40]; // L: 401
            var39 = (int) this.verticesZ[var40]; // L: 402
            if (var16 == 1) { // L: 403
                this.vertexData[var40] = var8.readUnsignedByte();
            }
        }

        if (var17 == 1) { // L: 405
            for (var40 = 0; var40 < var9; ++var40) { // L: 406
                var41 = var8.readUnsignedByte(); // L: 407
                this.field2095[var40] = new int[var41]; // L: 408
                this.field2096[var40] = new int[var41]; // L: 409

                for (var42 = 0; var42 < var41; ++var42) { // L: 410
                    this.field2095[var40][var42] = var8.readUnsignedByte(); // L: 411
                    this.field2096[var40][var42] = var8.readUnsignedByte(); // L: 412
                }
            }
        }

        var4.offset = var32; // L: 416
        var5.offset = var28; // L: 417
        var6.offset = var26; // L: 418
        var7.offset = var30; // L: 419
        var8.offset = var27; // L: 420

        for (var40 = 0; var40 < var10; ++var40) { // L: 421
            this.colors[var40] = (short)var4.readUnsignedShort(); // L: 422
            if (var12 == 1) { // L: 423
                var41 = var5.readUnsignedByte(); // L: 424
                if ((var41 & 1) == 1) { // L: 425
                    this.types[var40] = 1; // L: 426
                    var2 = true; // L: 427
                } else {
                    this.types[var40] = 0; // L: 429
                }

                if ((var41 & 2) == 2) { // L: 430
                    this.textures[var40] = (byte)(var41 >> 2); // L: 431
                    this.materials[var40] = this.colors[var40]; // L: 432
                    this.colors[var40] = 127; // L: 433
                    if (this.materials[var40] != -1) { // L: 434
                        var3 = true;
                    }
                } else {
                    this.textures[var40] = -1; // L: 437
                    this.materials[var40] = -1; // L: 438
                }
            }

            if (var13 == 255) { // L: 441
                this.priorities[var40] = var6.readByte();
            }

            if (var14 == 1) { // L: 442
                this.alphas[var40] = var7.readByte();
            }

            if (var15 == 1) { // L: 443
                this.triangleData[var40] = var8.readUnsignedByte();
            }
        }

        var4.offset = var31; // L: 445
        var5.offset = var25; // L: 446
        var40 = 0; // L: 447
        var41 = 0; // L: 448
        var42 = 0; // L: 449
        var43 = 0; // L: 450

        int var45;
        int var46;
        for (var44 = 0; var44 < var10; ++var44) { // L: 451
            var45 = var5.readUnsignedByte(); // L: 452
            if (var45 == 1) { // L: 453
                var40 = var4.readShortSmart() + var43; // L: 454
                var41 = var4.readShortSmart() + var40; // L: 456
                var42 = var4.readShortSmart() + var41; // L: 458
                var43 = var42; // L: 459
                this.trianglesX[var44] = var40; // L: 460
                this.trianglesY[var44] = var41; // L: 461
                this.trianglesZ[var44] = var42; // L: 462
            }

            if (var45 == 2) { // L: 464
                var41 = var42; // L: 465
                var42 = var4.readShortSmart() + var43; // L: 466
                var43 = var42; // L: 467
                this.trianglesX[var44] = var40; // L: 468
                this.trianglesY[var44] = var41; // L: 469
                this.trianglesZ[var44] = var42; // L: 470
            }

            if (var45 == 3) { // L: 472
                var40 = var42; // L: 473
                var42 = var4.readShortSmart() + var43; // L: 474
                var43 = var42; // L: 475
                this.trianglesX[var44] = var40; // L: 476
                this.trianglesY[var44] = var41; // L: 477
                this.trianglesZ[var44] = var42; // L: 478
            }

            if (var45 == 4) { // L: 480
                var46 = var40; // L: 481
                var40 = var41; // L: 482
                var41 = var46; // L: 483
                var42 = var4.readShortSmart() + var43; // L: 484
                var43 = var42; // L: 485
                this.trianglesX[var44] = var40; // L: 486
                this.trianglesY[var44] = var46; // L: 487
                this.trianglesZ[var44] = var42; // L: 488
            }
        }

        var4.offset = var33; // L: 491

        for (var44 = 0; var44 < var11; ++var44) { // L: 492
            this.textureTypes[var44] = 0; // L: 493
            this.texturesX[var44] = (short)var4.readUnsignedShort(); // L: 494
            this.texturesY[var44] = (short)var4.readUnsignedShort(); // L: 495
            this.texturesZ[var44] = (short)var4.readUnsignedShort(); // L: 496
        }

        if (this.textures != null) { // L: 498
            boolean var48 = false; // L: 499

            for (var45 = 0; var45 < var10; ++var45) { // L: 500
                var46 = this.textures[var45] & 255; // L: 501
                if (var46 != 255) { // L: 502
                    if (this.trianglesX[var45] == (this.texturesX[var46] & '\uffff') && this.trianglesY[var45] == (this.texturesY[var46] & '\uffff') && this.trianglesZ[var45] == (this.texturesZ[var46] & '\uffff')) { // L: 503
                        this.textures[var45] = -1;
                    } else {
                        var48 = true; // L: 504
                    }
                }
            }

            if (!var48) { // L: 507
                this.textures = null;
            }
        }

        if (!var3) { // L: 509
            this.materials = null;
        }

        if (!var2) { // L: 510
            this.types = null;
        }

    } // L: 511


    void method3814(byte[] var1) {
        Buffer var2 = new Buffer(var1); // L: 514
        Buffer var3 = new Buffer(var1); // L: 515
        Buffer var4 = new Buffer(var1); // L: 516
        Buffer var5 = new Buffer(var1); // L: 517
        Buffer var6 = new Buffer(var1); // L: 518
        Buffer var7 = new Buffer(var1); // L: 519
        Buffer var8 = new Buffer(var1); // L: 520
        var2.offset = var1.length - 23; // L: 521
        int var9 = var2.readUnsignedShort(); // L: 522
        int var10 = var2.readUnsignedShort(); // L: 523
        int var11 = var2.readUnsignedByte(); // L: 524
        int var12 = var2.readUnsignedByte(); // L: 525
        int var13 = var2.readUnsignedByte(); // L: 526
        int var14 = var2.readUnsignedByte(); // L: 527
        int var15 = var2.readUnsignedByte(); // L: 528
        int var16 = var2.readUnsignedByte(); // L: 529
        int var17 = var2.readUnsignedByte(); // L: 530
        int var18 = var2.readUnsignedShort(); // L: 531
        int var19 = var2.readUnsignedShort(); // L: 532
        int var20 = var2.readUnsignedShort(); // L: 533
        int var21 = var2.readUnsignedShort(); // L: 534
        int var22 = var2.readUnsignedShort(); // L: 535
        int var23 = 0; // L: 536
        int var24 = 0; // L: 537
        int var25 = 0; // L: 538
        int var26;
        if (var11 > 0) { // L: 539
            this.textureTypes = new byte[var11]; // L: 540
            var2.offset = 0; // L: 541

            for (var26 = 0; var26 < var11; ++var26) { // L: 542
                byte var27 = this.textureTypes[var26] = var2.readByte(); // L: 543
                if (var27 == 0) { // L: 544
                    ++var23;
                }

                if (var27 >= 1 && var27 <= 3) { // L: 545
                    ++var24;
                }

                if (var27 == 2) { // L: 546
                    ++var25;
                }
            }
        }

        var26 = var11 + var9; // L: 551
        int var28 = var26; // L: 552
        if (var12 == 1) { // L: 553
            var26 += var10;
        }

        int var29 = var26; // L: 554
        var26 += var10; // L: 555
        int var30 = var26; // L: 556
        if (var13 == 255) { // L: 557
            var26 += var10;
        }

        int var31 = var26; // L: 558
        if (var15 == 1) { // L: 559
            var26 += var10;
        }

        int var32 = var26; // L: 560
        if (var17 == 1) { // L: 561
            var26 += var9;
        }

        int var33 = var26; // L: 562
        if (var14 == 1) { // L: 563
            var26 += var10;
        }

        int var34 = var26; // L: 564
        var26 += var21; // L: 565
        int var35 = var26; // L: 566
        if (var16 == 1) { // L: 567
            var26 += var10 * 2;
        }

        int var36 = var26; // L: 568
        var26 += var22; // L: 569
        int var37 = var26; // L: 570
        var26 += var10 * 2; // L: 571
        int var38 = var26; // L: 572
        var26 += var18; // L: 573
        int var39 = var26; // L: 574
        var26 += var19; // L: 575
        int var40 = var26; // L: 576
        var26 += var20; // L: 577
        int var41 = var26; // L: 578
        var26 += var23 * 6; // L: 579
        int var42 = var26; // L: 580
        var26 += var24 * 6; // L: 581
        int var43 = var26; // L: 582
        var26 += var24 * 6; // L: 583
        int var44 = var26; // L: 584
        var26 += var24 * 2; // L: 585
        int var45 = var26; // L: 586
        var26 += var24; // L: 587
        int var46 = var26; // L: 588
        var26 += var24 * 2 + var25 * 2; // L: 589
        this.verticesCount = var9; // L: 591
        this.trianglesCount = var10; // L: 592
        this.texturesCount = var11; // L: 593
        this.verticesX = new float[var9]; // L: 594
        this.verticesY = new float[var9]; // L: 595
        this.verticesZ = new float[var9]; // L: 596
        this.trianglesX = new int[var10]; // L: 597
        this.trianglesY = new int[var10]; // L: 598
        this.trianglesZ = new int[var10]; // L: 599
        if (var17 == 1) { // L: 600
            this.vertexData = new int[var9];
        }

        if (var12 == 1) { // L: 601
            this.types = new byte[var10];
        }

        if (var13 == 255) { // L: 602
            this.priorities = new byte[var10];
        } else {
            this.priority = (byte)var13; // L: 603
        }

        if (var14 == 1) { // L: 604
            this.alphas = new byte[var10];
        }

        if (var15 == 1) { // L: 605
            this.triangleData = new int[var10];
        }

        if (var16 == 1) { // L: 606
            this.materials = new short[var10];
        }

        if (var16 == 1 && var11 > 0) { // L: 607
            this.textures = new byte[var10];
        }

        this.colors = new short[var10]; // L: 608
        if (var11 > 0) { // L: 609
            this.texturesX = new short[var11]; // L: 610
            this.texturesY = new short[var11]; // L: 611
            this.texturesZ = new short[var11]; // L: 612
        }

        var2.offset = var11; // L: 614
        var3.offset = var38; // L: 615
        var4.offset = var39; // L: 616
        var5.offset = var40; // L: 617
        var6.offset = var32; // L: 618
        int var48 = 0; // L: 619
        int var49 = 0; // L: 620
        int var50 = 0; // L: 621

        int var51;
        int var52;
        int var53;
        int var54;
        int var55;
        for (var51 = 0; var51 < var9; ++var51) { // L: 622
            var52 = var2.readUnsignedByte(); // L: 623
            var53 = 0; // L: 624
            if ((var52 & 1) != 0) { // L: 625
                var53 = var3.readShortSmart();
            }

            var54 = 0; // L: 626
            if ((var52 & 2) != 0) { // L: 627
                var54 = var4.readShortSmart();
            }

            var55 = 0; // L: 628
            if ((var52 & 4) != 0) { // L: 629
                var55 = var5.readShortSmart();
            }

            this.verticesX[var51] = var48 + var53; // L: 630
            this.verticesY[var51] = var49 + var54; // L: 631
            this.verticesZ[var51] = var50 + var55; // L: 632
            var48 = (int) this.verticesX[var51]; // L: 633
            var49 = (int) this.verticesY[var51]; // L: 634
            var50 = (int) this.verticesZ[var51]; // L: 635
            if (var17 == 1) { // L: 636
                this.vertexData[var51] = var6.readUnsignedByte();
            }
        }

        var2.offset = var37; // L: 638
        var3.offset = var28; // L: 639
        var4.offset = var30; // L: 640
        var5.offset = var33; // L: 641
        var6.offset = var31; // L: 642
        var7.offset = var35; // L: 643
        var8.offset = var36; // L: 644

        for (var51 = 0; var51 < var10; ++var51) { // L: 645
            this.colors[var51] = (short)var2.readUnsignedShort(); // L: 646
            if (var12 == 1) { // L: 647
                this.types[var51] = var3.readByte();
            }

            if (var13 == 255) { // L: 648
                this.priorities[var51] = var4.readByte();
            }

            if (var14 == 1) { // L: 649
                this.alphas[var51] = var5.readByte();
            }

            if (var15 == 1) { // L: 650
                this.triangleData[var51] = var6.readUnsignedByte();
            }

            if (var16 == 1) { // L: 651
                this.materials[var51] = (short)(var7.readUnsignedShort() - 1);
            }

            if (this.textures != null && this.materials[var51] != -1) { // L: 652
                this.textures[var51] = (byte)(var8.readUnsignedByte() - 1);
            }
        }

        var2.offset = var34; // L: 654
        var3.offset = var29; // L: 655
        var51 = 0; // L: 656
        var52 = 0; // L: 657
        var53 = 0; // L: 658
        var54 = 0; // L: 659

        int var56;
        for (var55 = 0; var55 < var10; ++var55) { // L: 660
            var56 = var3.readUnsignedByte(); // L: 661
            if (var56 == 1) { // L: 662
                var51 = var2.readShortSmart() + var54; // L: 663
                var52 = var2.readShortSmart() + var51; // L: 665
                var53 = var2.readShortSmart() + var52; // L: 667
                var54 = var53; // L: 668
                this.trianglesX[var55] = var51; // L: 669
                this.trianglesY[var55] = var52; // L: 670
                this.trianglesZ[var55] = var53; // L: 671
            }

            if (var56 == 2) { // L: 673
                var52 = var53; // L: 674
                var53 = var2.readShortSmart() + var54; // L: 675
                var54 = var53; // L: 676
                this.trianglesX[var55] = var51; // L: 677
                this.trianglesY[var55] = var52; // L: 678
                this.trianglesZ[var55] = var53; // L: 679
            }

            if (var56 == 3) { // L: 681
                var51 = var53; // L: 682
                var53 = var2.readShortSmart() + var54; // L: 683
                var54 = var53; // L: 684
                this.trianglesX[var55] = var51; // L: 685
                this.trianglesY[var55] = var52; // L: 686
                this.trianglesZ[var55] = var53; // L: 687
            }

            if (var56 == 4) { // L: 689
                int var57 = var51; // L: 690
                var51 = var52; // L: 691
                var52 = var57; // L: 692
                var53 = var2.readShortSmart() + var54; // L: 693
                var54 = var53; // L: 694
                this.trianglesX[var55] = var51; // L: 695
                this.trianglesY[var55] = var57; // L: 696
                this.trianglesZ[var55] = var53; // L: 697
            }
        }

        var2.offset = var41; // L: 700
        var3.offset = var42; // L: 701
        var4.offset = var43; // L: 702
        var5.offset = var44; // L: 703
        var6.offset = var45; // L: 704
        var7.offset = var46; // L: 705

        for (var55 = 0; var55 < var11; ++var55) { // L: 706
            var56 = this.textureTypes[var55] & 255; // L: 707
            if (var56 == 0) { // L: 708
                this.texturesX[var55] = (short)var2.readUnsignedShort(); // L: 709
                this.texturesY[var55] = (short)var2.readUnsignedShort(); // L: 710
                this.texturesZ[var55] = (short)var2.readUnsignedShort(); // L: 711
            }
        }

        var2.offset = var26; // L: 714
        var55 = var2.readUnsignedByte(); // L: 715
        if (var55 != 0) { // L: 716
            new ModelData0();
            var2.readUnsignedShort(); // L: 718
            var2.readUnsignedShort(); // L: 719
            var2.readUnsignedShort(); // L: 720
            var2.readInt(); // L: 721
        }

    } // L: 723


    void method3815(byte[] var1) {
        boolean var2 = false; // L: 726
        boolean var3 = false; // L: 727
        Buffer var4 = new Buffer(var1); // L: 728
        Buffer var5 = new Buffer(var1); // L: 729
        Buffer var6 = new Buffer(var1); // L: 730
        Buffer var7 = new Buffer(var1); // L: 731
        Buffer var8 = new Buffer(var1); // L: 732
        var4.offset = var1.length - 18; // L: 733
        int var9 = var4.readUnsignedShort(); // L: 734
        int var10 = var4.readUnsignedShort(); // L: 735
        int var11 = var4.readUnsignedByte(); // L: 736
        int var12 = var4.readUnsignedByte(); // L: 737
        int var13 = var4.readUnsignedByte(); // L: 738
        int var14 = var4.readUnsignedByte(); // L: 739
        int var15 = var4.readUnsignedByte(); // L: 740
        int var16 = var4.readUnsignedByte(); // L: 741
        int var17 = var4.readUnsignedShort(); // L: 742
        int var18 = var4.readUnsignedShort(); // L: 743
        int var19 = var4.readUnsignedShort(); // L: 744
        int var20 = var4.readUnsignedShort(); // L: 745
        byte var21 = 0; // L: 746
        int var45 = var21 + var9; // L: 748
        int var23 = var45; // L: 749
        var45 += var10; // L: 750
        int var24 = var45; // L: 751
        if (var13 == 255) { // L: 752
            var45 += var10;
        }

        int var25 = var45; // L: 753
        if (var15 == 1) { // L: 754
            var45 += var10;
        }

        int var26 = var45; // L: 755
        if (var12 == 1) { // L: 756
            var45 += var10;
        }

        int var27 = var45; // L: 757
        if (var16 == 1) { // L: 758
            var45 += var9;
        }

        int var28 = var45; // L: 759
        if (var14 == 1) { // L: 760
            var45 += var10;
        }

        int var29 = var45; // L: 761
        var45 += var20; // L: 762
        int var30 = var45; // L: 763
        var45 += var10 * 2; // L: 764
        int var31 = var45; // L: 765
        var45 += var11 * 6; // L: 766
        int var32 = var45; // L: 767
        var45 += var17; // L: 768
        int var33 = var45; // L: 769
        var45 += var18; // L: 770
        int var10000 = var45 + var19; // L: 772
        this.verticesCount = var9; // L: 773
        this.trianglesCount = var10; // L: 774
        this.texturesCount = var11; // L: 775
        this.verticesX = new float[var9]; // L: 776
        this.verticesY = new float[var9]; // L: 777
        this.verticesZ = new float[var9]; // L: 778
        this.trianglesX = new int[var10]; // L: 779
        this.trianglesY = new int[var10]; // L: 780
        this.trianglesZ = new int[var10]; // L: 781
        if (var11 > 0) { // L: 782
            this.textureTypes = new byte[var11]; // L: 783
            this.texturesX = new short[var11]; // L: 784
            this.texturesY = new short[var11]; // L: 785
            this.texturesZ = new short[var11]; // L: 786
        }

        if (var16 == 1) { // L: 788
            this.vertexData = new int[var9];
        }

        if (var12 == 1) { // L: 789
            this.types = new byte[var10]; // L: 790
            this.textures = new byte[var10]; // L: 791
            this.materials = new short[var10]; // L: 792
        }

        if (var13 == 255) { // L: 794
            this.priorities = new byte[var10];
        } else {
            this.priority = (byte)var13; // L: 795
        }

        if (var14 == 1) { // L: 796
            this.alphas = new byte[var10];
        }

        if (var15 == 1) { // L: 797
            this.triangleData = new int[var10];
        }

        this.colors = new short[var10]; // L: 798
        var4.offset = var21; // L: 799
        var5.offset = var32; // L: 800
        var6.offset = var33; // L: 801
        var7.offset = var45; // L: 802
        var8.offset = var27; // L: 803
        int var35 = 0; // L: 804
        int var36 = 0; // L: 805
        int var37 = 0; // L: 806

        int var38;
        int var39;
        int var40;
        int var41;
        int var42;
        for (var38 = 0; var38 < var9; ++var38) { // L: 807
            var39 = var4.readUnsignedByte(); // L: 808
            var40 = 0; // L: 809
            if ((var39 & 1) != 0) { // L: 810
                var40 = var5.readShortSmart();
            }

            var41 = 0; // L: 811
            if ((var39 & 2) != 0) { // L: 812
                var41 = var6.readShortSmart();
            }

            var42 = 0; // L: 813
            if ((var39 & 4) != 0) { // L: 814
                var42 = var7.readShortSmart();
            }

            this.verticesX[var38] = var35 + var40; // L: 815
            this.verticesY[var38] = var36 + var41; // L: 816
            this.verticesZ[var38] = var37 + var42; // L: 817
            var35 = (int) this.verticesX[var38]; // L: 818
            var36 = (int) this.verticesY[var38]; // L: 819
            var37 = (int) this.verticesZ[var38]; // L: 820
            if (var16 == 1) { // L: 821
                this.vertexData[var38] = var8.readUnsignedByte();
            }
        }

        var4.offset = var30; // L: 823
        var5.offset = var26; // L: 824
        var6.offset = var24; // L: 825
        var7.offset = var28; // L: 826
        var8.offset = var25; // L: 827

        for (var38 = 0; var38 < var10; ++var38) { // L: 828
            this.colors[var38] = (short)var4.readUnsignedShort(); // L: 829
            if (var12 == 1) { // L: 830
                var39 = var5.readUnsignedByte(); // L: 831
                if ((var39 & 1) == 1) { // L: 832
                    this.types[var38] = 1; // L: 833
                    var2 = true; // L: 834
                } else {
                    this.types[var38] = 0; // L: 836
                }

                if ((var39 & 2) == 2) { // L: 837
                    this.textures[var38] = (byte)(var39 >> 2); // L: 838
                    this.materials[var38] = this.colors[var38]; // L: 839
                    this.colors[var38] = 127; // L: 840
                    if (this.materials[var38] != -1) { // L: 841
                        var3 = true;
                    }
                } else {
                    this.textures[var38] = -1; // L: 844
                    this.materials[var38] = -1; // L: 845
                }
            }

            if (var13 == 255) { // L: 848
                this.priorities[var38] = var6.readByte();
            }

            if (var14 == 1) { // L: 849
                this.alphas[var38] = var7.readByte();
            }

            if (var15 == 1) { // L: 850
                this.triangleData[var38] = var8.readUnsignedByte();
            }
        }

        var4.offset = var29; // L: 852
        var5.offset = var23; // L: 853
        var38 = 0; // L: 854
        var39 = 0; // L: 855
        var40 = 0; // L: 856
        var41 = 0; // L: 857

        int var43;
        int var44;
        for (var42 = 0; var42 < var10; ++var42) { // L: 858
            var43 = var5.readUnsignedByte(); // L: 859
            if (var43 == 1) { // L: 860
                var38 = var4.readShortSmart() + var41; // L: 861
                var39 = var4.readShortSmart() + var38; // L: 863
                var40 = var4.readShortSmart() + var39; // L: 865
                var41 = var40; // L: 866
                this.trianglesX[var42] = var38; // L: 867
                this.trianglesY[var42] = var39; // L: 868
                this.trianglesZ[var42] = var40; // L: 869
            }

            if (var43 == 2) { // L: 871
                var39 = var40; // L: 872
                var40 = var4.readShortSmart() + var41; // L: 873
                var41 = var40; // L: 874
                this.trianglesX[var42] = var38; // L: 875
                this.trianglesY[var42] = var39; // L: 876
                this.trianglesZ[var42] = var40; // L: 877
            }

            if (var43 == 3) { // L: 879
                var38 = var40; // L: 880
                var40 = var4.readShortSmart() + var41; // L: 881
                var41 = var40; // L: 882
                this.trianglesX[var42] = var38; // L: 883
                this.trianglesY[var42] = var39; // L: 884
                this.trianglesZ[var42] = var40; // L: 885
            }

            if (var43 == 4) { // L: 887
                var44 = var38; // L: 888
                var38 = var39; // L: 889
                var39 = var44; // L: 890
                var40 = var4.readShortSmart() + var41; // L: 891
                var41 = var40; // L: 892
                this.trianglesX[var42] = var38; // L: 893
                this.trianglesY[var42] = var44; // L: 894
                this.trianglesZ[var42] = var40; // L: 895
            }
        }

        var4.offset = var31; // L: 898

        for (var42 = 0; var42 < var11; ++var42) { // L: 899
            this.textureTypes[var42] = 0; // L: 900
            this.texturesX[var42] = (short)var4.readUnsignedShort(); // L: 901
            this.texturesY[var42] = (short)var4.readUnsignedShort(); // L: 902
            this.texturesZ[var42] = (short)var4.readUnsignedShort(); // L: 903
        }

        if (this.textures != null) { // L: 905
            boolean var46 = false; // L: 906

            for (var43 = 0; var43 < var10; ++var43) { // L: 907
                var44 = this.textures[var43] & 255; // L: 908
                if (var44 != 255) { // L: 909
                    if (this.trianglesX[var43] == (this.texturesX[var44] & '\uffff') && this.trianglesY[var43] == (this.texturesY[var44] & '\uffff') && this.trianglesZ[var43] == (this.texturesZ[var44] & '\uffff')) { // L: 910
                        this.textures[var43] = -1;
                    } else {
                        var46 = true; // L: 911
                    }
                }
            }

            if (!var46) { // L: 914
                this.textures = null;
            }
        }

        if (!var3) { // L: 916
            this.materials = null;
        }

        if (!var2) { // L: 917
            this.types = null;
        }

    } // L: 918

    final int method3811(ModelData var1, int var2) {
        int var3 = -1; // L: 1023
        int var4 = (int) var1.verticesX[var2]; // L: 1024
        int var5 = (int) var1.verticesY[var2]; // L: 1025
        int var6 = (int) var1.verticesZ[var2]; // L: 1026

        for (int var7 = 0; var7 < this.verticesCount; ++var7) { // L: 1027
            if (var4 == this.verticesX[var7] && var5 == this.verticesY[var7] && var6 == this.verticesZ[var7]) { // L: 1028
                var3 = var7; // L: 1029
                break;
            }
        }

        if (var3 == -1) { // L: 1033
            this.verticesX[this.verticesCount] = var4; // L: 1034
            this.verticesY[this.verticesCount] = var5; // L: 1035
            this.verticesZ[this.verticesCount] = var6; // L: 1036
            if (var1.vertexData != null) {
                this.vertexData[this.verticesCount] = var1.vertexData[var2]; // L: 1037
            }

            if (var1.field2095 != null) { // L: 1038
                this.field2095[this.verticesCount] = var1.field2095[var2]; // L: 1039
                this.field2096[this.verticesCount] = var1.field2096[var2]; // L: 1040
            }

            var3 = this.verticesCount++; // L: 1042
        }

        return var3; // L: 1044
    }

    public ModelData copyModelData() {
        ModelData var1 = new ModelData(); // L: 1112
        if (this.types != null) { // L: 1113
            var1.types = new byte[this.trianglesCount]; // L: 1114

            for (int var2 = 0; var2 < this.trianglesCount; ++var2) { // L: 1115
                var1.types[var2] = this.types[var2];
            }
        }

        var1.verticesCount = this.verticesCount; // L: 1117
        var1.trianglesCount = this.trianglesCount; // L: 1118
        var1.texturesCount = this.texturesCount; // L: 1119
        var1.verticesX = this.verticesX; // L: 1120
        var1.verticesY = this.verticesY; // L: 1121
        var1.verticesZ = this.verticesZ; // L: 1122
        var1.trianglesX = this.trianglesX; // L: 1123
        var1.trianglesY = this.trianglesY; // L: 1124
        var1.trianglesZ = this.trianglesZ; // L: 1125
        var1.priorities = this.priorities; // L: 1126
        var1.alphas = this.alphas; // L: 1127
        var1.textures = this.textures; // L: 1128
        var1.colors = this.colors; // L: 1129
        var1.materials = this.materials; // L: 1130
        var1.priority = this.priority; // L: 1131
        var1.textureTypes = this.textureTypes; // L: 1132
        var1.texturesX = this.texturesX; // L: 1133
        var1.texturesY = this.texturesY; // L: 1134
        var1.texturesZ = this.texturesZ; // L: 1135
        var1.vertexData = this.vertexData; // L: 1136
        var1.triangleData = this.triangleData; // L: 1137
        var1.vertexLabels = this.vertexLabels; // L: 1138
        var1.faceLabelsAlpha = this.faceLabelsAlpha; // L: 1139
        var1.vertexNormals = this.vertexNormals; // L: 1140
        var1.faceNormals = this.faceNormals; // L: 1141
        var1.ambient = this.ambient; // L: 1142
        var1.contrast = this.contrast; // L: 1143
        return var1; // L: 1144
    }

    public ModelData method3847(int[][] var1, int var2, int var3, int var4, boolean var5, int var6) {
        this.calculateBounds(); // L: 1148
        int var7 = var2 + this.field2310; // L: 1149
        int var8 = var2 + this.field2315; // L: 1150
        int var9 = var4 + this.field2313; // L: 1151
        int var10 = var4 + this.field2312; // L: 1152
        if (var7 >= 0 && var8 + 128 >> 7 < var1.length && var9 >= 0 && var10 + 128 >> 7 < var1[0].length) { // L: 1153
            var7 >>= 7; // L: 1154
            var8 = var8 + 127 >> 7; // L: 1155
            var9 >>= 7; // L: 1156
            var10 = var10 + 127 >> 7; // L: 1157
            if (var3 == var1[var7][var9] && var3 == var1[var8][var9] && var3 == var1[var7][var10] && var3 == var1[var8][var10]) { // L: 1158
                return this;
            } else {
                ModelData var11 = new ModelData(); // L: 1161
                var11.verticesCount = this.verticesCount; // L: 1162
                var11.trianglesCount = this.trianglesCount; // L: 1163
                var11.texturesCount = this.texturesCount; // L: 1164
                var11.verticesX = this.verticesX; // L: 1165
                var11.verticesZ = this.verticesZ; // L: 1166
                var11.trianglesX = this.trianglesX; // L: 1167
                var11.trianglesY = this.trianglesY; // L: 1168
                var11.trianglesZ = this.trianglesZ; // L: 1169
                var11.types = this.types; // L: 1170
                var11.priorities = this.priorities; // L: 1171
                var11.alphas = this.alphas; // L: 1172
                var11.textures = this.textures; // L: 1173
                var11.colors = this.colors; // L: 1174
                var11.materials = this.materials; // L: 1175
                var11.priority = this.priority; // L: 1176
                var11.textureTypes = this.textureTypes; // L: 1177
                var11.texturesX = this.texturesX; // L: 1178
                var11.texturesY = this.texturesY; // L: 1179
                var11.texturesZ = this.texturesZ; // L: 1180
                var11.vertexData = this.vertexData; // L: 1181
                var11.triangleData = this.triangleData; // L: 1182
                var11.vertexLabels = this.vertexLabels; // L: 1183
                var11.faceLabelsAlpha = this.faceLabelsAlpha; // L: 1184
                var11.ambient = this.ambient; // L: 1185
                var11.contrast = this.contrast; // L: 1186
                var11.verticesY = new float[var11.verticesCount]; // L: 1187
                int var12;
                int var13;
                int var14;
                int var15;
                int var16;
                int var17;
                int var18;
                int var19;
                int var20;
                int var21;
                if (var6 == 0) { // L: 1192
                    for (var12 = 0; var12 < var11.verticesCount; ++var12) { // L: 1193
                        var13 = (int) (var2 + this.verticesX[var12]); // L: 1194
                        var14 = (int) (var4 + this.verticesZ[var12]); // L: 1195
                        var15 = var13 & 127; // L: 1196
                        var16 = var14 & 127; // L: 1197
                        var17 = var13 >> 7; // L: 1198
                        var18 = var14 >> 7; // L: 1199
                        var19 = var1[var17][var18] * (128 - var15) + var1[var17 + 1][var18] * var15 >> 7; // L: 1200
                        var20 = var1[var17][var18 + 1] * (128 - var15) + var15 * var1[var17 + 1][var18 + 1] >> 7; // L: 1201
                        var21 = var19 * (128 - var16) + var20 * var16 >> 7; // L: 1202
                        var11.verticesY[var12] = var21 + this.verticesY[var12] - var3; // L: 1203
                    }
                } else {
                    for (var12 = 0; var12 < var11.verticesCount; ++var12) { // L: 1207
                        var13 = ((int) -this.verticesY[var12] << 16) / super.height; // L: 1208
                        if (var13 < var6) { // L: 1209
                            var14 = (int) (var2 + this.verticesX[var12]); // L: 1210
                            var15 = (int) (var4 + this.verticesZ[var12]); // L: 1211
                            var16 = var14 & 127; // L: 1212
                            var17 = var15 & 127; // L: 1213
                            var18 = var14 >> 7; // L: 1214
                            var19 = var15 >> 7; // L: 1215
                            var20 = var1[var18][var19] * (128 - var16) + var1[var18 + 1][var19] * var16 >> 7; // L: 1216
                            var21 = var1[var18][var19 + 1] * (128 - var16) + var16 * var1[var18 + 1][var19 + 1] >> 7; // L: 1217
                            int var22 = var20 * (128 - var17) + var21 * var17 >> 7; // L: 1218
                            var11.verticesY[var12] = (var6 - var13) * (var22 - var3) / var6 + this.verticesY[var12]; // L: 1219
                        }
                    }
                }

                var11.invalidate(); // L: 1223
                return var11; // L: 1224
            }
        } else {
            return this;
        }
    }


    void method3859() {
        int[] var1;
        int var2;
        int var10002;
        int var3;
        int var4;
        if (this.vertexData != null) { // L: 1228
            var1 = new int[256]; // L: 1229
            var2 = 0; // L: 1230

            for (var3 = 0; var3 < this.verticesCount; ++var3) { // L: 1231
                var4 = this.vertexData[var3]; // L: 1232
                var10002 = var1[var4]++; // L: 1233
                if (var4 > var2) { // L: 1234
                    var2 = var4;
                }
            }

            this.vertexLabels = new int[var2 + 1][]; // L: 1236

            for (var3 = 0; var3 <= var2; ++var3) { // L: 1237
                this.vertexLabels[var3] = new int[var1[var3]]; // L: 1238
                var1[var3] = 0; // L: 1239
            }

            for (var3 = 0; var3 < this.verticesCount; this.vertexLabels[var4][var1[var4]++] = var3++) { // L: 1241 1243
                var4 = this.vertexData[var3]; // L: 1242
            }

            this.vertexData = null; // L: 1245
        }

        if (this.triangleData != null) { // L: 1247
            var1 = new int[256]; // L: 1248
            var2 = 0; // L: 1249

            for (var3 = 0; var3 < this.trianglesCount; ++var3) { // L: 1250
                var4 = this.triangleData[var3]; // L: 1251
                var10002 = var1[var4]++; // L: 1252
                if (var4 > var2) { // L: 1253
                    var2 = var4;
                }
            }

            this.faceLabelsAlpha = new int[var2 + 1][]; // L: 1255

            for (var3 = 0; var3 <= var2; ++var3) { // L: 1256
                this.faceLabelsAlpha[var3] = new int[var1[var3]]; // L: 1257
                var1[var3] = 0; // L: 1258
            }

            for (var3 = 0; var3 < this.trianglesCount; this.faceLabelsAlpha[var4][var1[var4]++] = var3++) { // L: 1260 1262
                var4 = this.triangleData[var3]; // L: 1261
            }

            this.triangleData = null; // L: 1264
        }

    } // L: 1266


    public void method3820() {
        for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1269
            int var2 = (int) this.verticesX[var1]; // L: 1270
            this.verticesX[var1] = this.verticesZ[var1]; // L: 1271
            this.verticesZ[var1] = -var2; // L: 1272
        }

        this.invalidate(); // L: 1274
    } // L: 1275


    public void method3854() {
        for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1278
            this.verticesX[var1] = -this.verticesX[var1]; // L: 1279
            this.verticesZ[var1] = -this.verticesZ[var1]; // L: 1280
        }

        this.invalidate(); // L: 1282
    } // L: 1283


    public void method3822() {
        for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1286
            int var2 = (int) this.verticesZ[var1]; // L: 1287
            this.verticesZ[var1] = this.verticesX[var1]; // L: 1288
            this.verticesX[var1] = -var2; // L: 1289
        }

        this.invalidate(); // L: 1291
    } // L: 1292


    public void method3823(int var1) {
        int var2 = ModelData_sine[var1]; // L: 1295
        int var3 = ModelData_cosine[var1]; // L: 1296

        for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 1297
            int var5 = var2 * ((int)this.verticesZ[var4]) + var3 * ((int)this.verticesX[var4]) >> 16; // L: 1298
            this.verticesZ[var4] = var3 * ((int)this.verticesZ[var4]) - var2 * ((int)this.verticesX[var4]) >> 16; // L: 1299
            this.verticesX[var4] = var5; // L: 1300
        }

        this.invalidate(); // L: 1302
    } // L: 1303



    public void changeOffset(int var1, int var2, int var3) {
        for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 1306
            float[] var10000 = this.verticesX; // L: 1307
            var10000[var4] += var1;
            var10000 = this.verticesY; // L: 1308
            var10000[var4] += var2;
            var10000 = this.verticesZ; // L: 1309
            var10000[var4] += var3;
        }

        this.invalidate(); // L: 1311
    } // L: 1312



    public void recolor(short var1, short var2) {
        for (int var3 = 0; var3 < this.trianglesCount; ++var3) { // L: 1315
            if (this.colors[var3] == var1) { // L: 1316
                this.colors[var3] = var2;
            }
        }

    } // L: 1318



    public void retexture(short var1, short var2) {
        if (this.materials != null) { // L: 1321
            for (int var3 = 0; var3 < this.trianglesCount; ++var3) { // L: 1322
                if (this.materials[var3] == var1) {
                    this.materials[var3] = var2; // L: 1323
                }
            }

        }
    } // L: 1325


    public void method3841() {
        int var1;
        for (var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1328
            this.verticesZ[var1] = -this.verticesZ[var1];
        }

        for (var1 = 0; var1 < this.trianglesCount; ++var1) { // L: 1329
            int var2 = this.trianglesX[var1]; // L: 1330
            this.trianglesX[var1] = this.trianglesZ[var1]; // L: 1331
            this.trianglesZ[var1] = var2; // L: 1332
        }

        this.invalidate(); // L: 1334
    } // L: 1335



    public void resize(int var1, int var2, int var3) {
        for (int var4 = 0; var4 < this.verticesCount; ++var4) { // L: 1338
            this.verticesX[var4] = this.verticesX[var4] * var1 / 128; // L: 1339
            this.verticesY[var4] = var2 * this.verticesY[var4] / 128; // L: 1340
            this.verticesZ[var4] = var3 * this.verticesZ[var4] / 128; // L: 1341
        }

        this.invalidate(); // L: 1343
    } // L: 1344



    public void calculateVertexNormals() {
        if (this.vertexNormals == null) { // L: 1347
            this.vertexNormals = new VertexNormal[this.verticesCount]; // L: 1348

            int var1;
            for (var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1349
                this.vertexNormals[var1] = new VertexNormal();
            }

            for (var1 = 0; var1 < this.trianglesCount; ++var1) { // L: 1350
                int var2 = this.trianglesX[var1]; // L: 1351
                int var3 = this.trianglesY[var1]; // L: 1352
                int var4 = this.trianglesZ[var1]; // L: 1353
                int var5 = (int) (this.verticesX[var3] - this.verticesX[var2]); // L: 1354
                int var6 = (int) (this.verticesY[var3] - this.verticesY[var2]); // L: 1355
                int var7 = (int) (this.verticesZ[var3] - this.verticesZ[var2]); // L: 1356
                int var8 = (int) (this.verticesX[var4] - this.verticesX[var2]); // L: 1357
                int var9 = (int) (this.verticesY[var4] - this.verticesY[var2]); // L: 1358
                int var10 = (int) (this.verticesZ[var4] - this.verticesZ[var2]); // L: 1359
                int var11 = var6 * var10 - var9 * var7; // L: 1360
                int var12 = var7 * var8 - var10 * var5; // L: 1361

                int var13;
                for (var13 = var5 * var9 - var8 * var6; var11 > 8192 || var12 > 8192 || var13 > 8192 || var11 < -8192 || var12 < -8192 || var13 < -8192; var13 >>= 1) { // L: 1362 1363 1366
                    var11 >>= 1; // L: 1364
                    var12 >>= 1; // L: 1365
                }

                int var14 = (int)Math.sqrt((double)(var11 * var11 + var12 * var12 + var13 * var13)); // L: 1368
                if (var14 <= 0) { // L: 1369
                    var14 = 1;
                }

                var11 = var11 * 256 / var14; // L: 1370
                var12 = var12 * 256 / var14; // L: 1371
                var13 = var13 * 256 / var14; // L: 1372
                byte var15;
                if (this.types == null) { // L: 1374
                    var15 = 0;
                } else {
                    var15 = this.types[var1]; // L: 1375
                }

                if (var15 == 0) { // L: 1376
                    VertexNormal var16 = this.vertexNormals[var2]; // L: 1378
                    var16.x += var11; // L: 1379
                    var16.y += var12; // L: 1380
                    var16.z += var13; // L: 1381
                    ++var16.magnitude; // L: 1382
                    var16 = this.vertexNormals[var3]; // L: 1383
                    var16.x += var11; // L: 1384
                    var16.y += var12; // L: 1385
                    var16.z += var13; // L: 1386
                    ++var16.magnitude; // L: 1387
                    var16 = this.vertexNormals[var4]; // L: 1388
                    var16.x += var11; // L: 1389
                    var16.y += var12; // L: 1390
                    var16.z += var13; // L: 1391
                    ++var16.magnitude; // L: 1392
                } else if (var15 == 1) { // L: 1394
                    if (this.faceNormals == null) { // L: 1395
                        this.faceNormals = new FaceNormal[this.trianglesCount];
                    }

                    FaceNormal var17 = this.faceNormals[var1] = new FaceNormal(); // L: 1396
                    var17.x = var11; // L: 1397
                    var17.y = var12; // L: 1398
                    var17.z = var13; // L: 1399
                }
            }

        }
    } // L: 1402



    void invalidate() {
        this.vertexNormals = null; // L: 1405
        this.vertexVertices = null; // L: 1406
        this.faceNormals = null; // L: 1407
        this.isBoundsCalculated = false; // L: 1408
    } // L: 1409



    void calculateBounds() {
        if (!this.isBoundsCalculated) { // L: 1412
            super.height = 0; // L: 1413
            this.field2309 = 0; // L: 1414
            this.field2310 = 999999; // L: 1415
            this.field2315 = -999999; // L: 1416
            this.field2312 = -99999; // L: 1417
            this.field2313 = 99999; // L: 1418

            for (int var1 = 0; var1 < this.verticesCount; ++var1) { // L: 1419
                int var2 = (int) this.verticesX[var1]; // L: 1420
                int var3 = (int) this.verticesY[var1]; // L: 1421
                int var4 = (int) this.verticesZ[var1]; // L: 1422
                if (var2 < this.field2310) { // L: 1423
                    this.field2310 = var2;
                }

                if (var2 > this.field2315) { // L: 1424
                    this.field2315 = var2;
                }

                if (var4 < this.field2313) { // L: 1425
                    this.field2313 = var4;
                }

                if (var4 > this.field2312) { // L: 1426
                    this.field2312 = var4;
                }

                if (-var3 > super.height) { // L: 1427
                    super.height = -var3;
                }

                if (var3 > this.field2309) { // L: 1428
                    this.field2309 = var3;
                }
            }

            this.isBoundsCalculated = true; // L: 1430
        }
    } // L: 1431

    public final Model toModel(int var1, int var2, int var3, int var4, int var5) {
        this.calculateVertexNormals(); // L: 1490
        int var6 = (int)Math.sqrt((double)(var5 * var5 + var3 * var3 + var4 * var4)); // L: 1491
        int var7 = var6 * var2 >> 8; // L: 1492
        Model var8 = new Model(); // L: 1493
        var8.faceColors1 = new int[this.trianglesCount]; // L: 1494
        var8.faceColors2 = new int[this.trianglesCount]; // L: 1495
        var8.faceColors3 = new int[this.trianglesCount]; // L: 1496
        if (this.texturesCount > 0 && this.textures != null) { // L: 1497
            int[] var9 = new int[this.texturesCount]; // L: 1498

            int var10;
            for (var10 = 0; var10 < this.trianglesCount; ++var10) { // L: 1499
                if (this.textures[var10] != -1) { // L: 1500
                    ++var9[this.textures[var10] & 255];
                }
            }

            var8.field2569 = 0; // L: 1502

            for (var10 = 0; var10 < this.texturesCount; ++var10) { // L: 1503
                if (var9[var10] > 0 && this.textureTypes[var10] == 0) { // L: 1504
                    ++var8.field2569;
                }
            }

            var8.field2570 = new int[var8.field2569]; // L: 1506
            var8.field2571 = new int[var8.field2569]; // L: 1507
            var8.field2572 = new int[var8.field2569]; // L: 1508
            var10 = 0; // L: 1509

            int var11;
            for (var11 = 0; var11 < this.texturesCount; ++var11) { // L: 1510
                if (var9[var11] > 0 && this.textureTypes[var11] == 0) { // L: 1511
                    var8.field2570[var10] = this.texturesX[var11] & '\uffff'; // L: 1512
                    var8.field2571[var10] = this.texturesY[var11] & '\uffff'; // L: 1513
                    var8.field2572[var10] = this.texturesZ[var11] & '\uffff'; // L: 1514
                    var9[var11] = var10++; // L: 1515
                } else {
                    var9[var11] = -1; // L: 1517
                }
            }

            var8.field2566 = new byte[this.trianglesCount]; // L: 1519

            for (var11 = 0; var11 < this.trianglesCount; ++var11) { // L: 1520
                if (this.textures[var11] != -1) { // L: 1521
                    var8.field2566[var11] = (byte)var9[this.textures[var11] & 255];
                } else {
                    var8.field2566[var11] = -1; // L: 1522
                }
            }
        }

        for (int var16 = 0; var16 < this.trianglesCount; ++var16) { // L: 1525
            byte var17;
            if (this.types == null) { // L: 1527
                var17 = 0;
            } else {
                var17 = this.types[var16]; // L: 1528
            }

            byte var18;
            if (this.alphas == null) { // L: 1530
                var18 = 0;
            } else {
                var18 = this.alphas[var16]; // L: 1531
            }

            short var12;
            if (this.materials == null) { // L: 1533
                var12 = -1;
            } else {
                var12 = this.materials[var16]; // L: 1534
            }

            if (var18 == -2) { // L: 1535
                var17 = 3;
            }

            if (var18 == -1) { // L: 1536
                var17 = 2;
            }

            VertexNormal var13;
            int var14;
            FaceNormal var19;
            if (var12 == -1) { // L: 1537
                if (var17 != 0) { // L: 1538
                    if (var17 == 1) { // L: 1555
                        var19 = this.faceNormals[var16]; // L: 1556
                        var14 = (var4 * var19.y + var5 * var19.z + var3 * var19.x) / (var7 / 2 + var7) + var1; // L: 1557
                        var8.faceColors1[var16] = method3834(this.colors[var16] & '\uffff', var14); // L: 1558
                        var8.faceColors3[var16] = -1; // L: 1559
                    } else if (var17 == 3) { // L: 1561
                        var8.faceColors1[var16] = 128; // L: 1562
                        var8.faceColors3[var16] = -1; // L: 1563
                    } else {
                        var8.faceColors3[var16] = -2; // L: 1566
                    }
                } else {
                    int var15 = this.colors[var16] & '\uffff'; // L: 1541
                    if (this.vertexVertices != null && this.vertexVertices[this.trianglesX[var16]] != null) { // L: 1542
                        var13 = this.vertexVertices[this.trianglesX[var16]];
                    } else {
                        var13 = this.vertexNormals[this.trianglesX[var16]]; // L: 1543
                    }

                    var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1544
                    var8.faceColors1[var16] = method3834(var15, var14); // L: 1545
                    if (this.vertexVertices != null && this.vertexVertices[this.trianglesY[var16]] != null) { // L: 1546
                        var13 = this.vertexVertices[this.trianglesY[var16]];
                    } else {
                        var13 = this.vertexNormals[this.trianglesY[var16]]; // L: 1547
                    }

                    var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1548
                    var8.faceColors2[var16] = method3834(var15, var14); // L: 1549
                    if (this.vertexVertices != null && this.vertexVertices[this.trianglesZ[var16]] != null) { // L: 1550
                        var13 = this.vertexVertices[this.trianglesZ[var16]];
                    } else {
                        var13 = this.vertexNormals[this.trianglesZ[var16]]; // L: 1551
                    }

                    var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1552
                    var8.faceColors3[var16] = method3834(var15, var14); // L: 1553
                }
            } else if (var17 != 0) { // L: 1570
                if (var17 == 1) { // L: 1586
                    var19 = this.faceNormals[var16]; // L: 1587
                    var14 = (var4 * var19.y + var5 * var19.z + var3 * var19.x) / (var7 / 2 + var7) + var1; // L: 1588
                    var8.faceColors1[var16] = method3817(var14); // L: 1589
                    var8.faceColors3[var16] = -1; // L: 1590
                } else {
                    var8.faceColors3[var16] = -2; // L: 1593
                }
            } else {
                if (this.vertexVertices != null && this.vertexVertices[this.trianglesX[var16]] != null) { // L: 1573
                    var13 = this.vertexVertices[this.trianglesX[var16]];
                } else {
                    var13 = this.vertexNormals[this.trianglesX[var16]]; // L: 1574
                }

                var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1575
                var8.faceColors1[var16] = method3817(var14); // L: 1576
                if (this.vertexVertices != null && this.vertexVertices[this.trianglesY[var16]] != null) { // L: 1577
                    var13 = this.vertexVertices[this.trianglesY[var16]];
                } else {
                    var13 = this.vertexNormals[this.trianglesY[var16]]; // L: 1578
                }

                var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1579
                var8.faceColors2[var16] = method3817(var14); // L: 1580
                if (this.vertexVertices != null && this.vertexVertices[this.trianglesZ[var16]] != null) {
                    var13 = this.vertexVertices[this.trianglesZ[var16]]; // L: 1581
                } else {
                    var13 = this.vertexNormals[this.trianglesZ[var16]]; // L: 1582
                }

                var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1; // L: 1583
                var8.faceColors3[var16] = method3817(var14); // L: 1584
            }
        }

        this.method3859(); // L: 1597
        var8.verticesCount = this.verticesCount; // L: 1598
        var8.verticesX = this.verticesX; // L: 1599
        var8.verticesY = this.verticesY; // L: 1600
        var8.verticesZ = this.verticesZ; // L: 1601
        var8.indicesCount = this.trianglesCount; // L: 1602
        var8.indices1 = this.trianglesX; // L: 1603
        var8.indices2 = this.trianglesY; // L: 1604
        var8.indices3 = this.trianglesZ; // L: 1605
        var8.faceRenderPriorities = this.priorities; // L: 1606
        var8.faceAlphas = this.alphas; // L: 1607
        var8.field2568 = this.priority; // L: 1608
        var8.vertexLabels = this.vertexLabels; // L: 1609
        var8.faceLabelsAlpha = this.faceLabelsAlpha; // L: 1610
        var8.faceTextures = this.materials; // L: 1611
        var8.field2575 = this.field2095; // L: 1612
        var8.field2594 = this.field2096; // L: 1613
        return var8; // L: 1614
    }

    static void method3862(ModelData var0, ModelData var1, int var2, int var3, int var4, boolean var5) {
        var0.calculateBounds(); // L: 1434
        var0.calculateVertexNormals(); // L: 1435
        var1.calculateBounds(); // L: 1436
        var1.calculateVertexNormals(); // L: 1437
        ++field2308; // L: 1438
        int var6 = 0; // L: 1439
        float[] var7 = var1.verticesX; // L: 1440
        int var8 = var1.verticesCount; // L: 1441

        int var9;
        for (var9 = 0; var9 < var0.verticesCount; ++var9) { // L: 1442
            VertexNormal var10 = var0.vertexNormals[var9]; // L: 1443
            if (var10.magnitude != 0) { // L: 1444
                int var11 = (int) (var0.verticesY[var9] - var3); // L: 1445
                if (var11 <= var1.field2309) { // L: 1446
                    int var12 = (int) (var0.verticesX[var9] - var2); // L: 1447
                    if (var12 >= var1.field2310 && var12 <= var1.field2315) { // L: 1448
                        int var13 = (int) (var0.verticesZ[var9] - var4); // L: 1449
                        if (var13 >= var1.field2313 && var13 <= var1.field2312) { // L: 1450
                            for (int var14 = 0; var14 < var8; ++var14) { // L: 1451
                                VertexNormal var15 = var1.vertexNormals[var14]; // L: 1452
                                if (var12 == var7[var14] && var13 == var1.verticesZ[var14] && var11 == var1.verticesY[var14] && var15.magnitude != 0) { // L: 1453
                                    if (var0.vertexVertices == null) { // L: 1454
                                        var0.vertexVertices = new VertexNormal[var0.verticesCount];
                                    }

                                    if (var1.vertexVertices == null) { // L: 1455
                                        var1.vertexVertices = new VertexNormal[var8];
                                    }

                                    VertexNormal var16 = var0.vertexVertices[var9]; // L: 1456
                                    if (var16 == null) { // L: 1457
                                        var16 = var0.vertexVertices[var9] = new VertexNormal(var10);
                                    }

                                    VertexNormal var17 = var1.vertexVertices[var14]; // L: 1458
                                    if (var17 == null) {
                                        var17 = var1.vertexVertices[var14] = new VertexNormal(var15); // L: 1459
                                    }

                                    var16.x += var15.x; // L: 1460
                                    var16.y += var15.y; // L: 1461
                                    var16.z += var15.z; // L: 1462
                                    var16.magnitude += var15.magnitude; // L: 1463
                                    var17.x += var10.x; // L: 1464
                                    var17.y += var10.y; // L: 1465
                                    var17.z += var10.z; // L: 1466
                                    var17.magnitude += var10.magnitude; // L: 1467
                                    ++var6; // L: 1468
                                    field2314[var9] = field2308; // L: 1469
                                    field2290[var14] = field2308; // L: 1470
                                }
                            }
                        }
                    }
                }
            }
        }

        if (var6 >= 3 && var5) { // L: 1474
            for (var9 = 0; var9 < var0.trianglesCount; ++var9) { // L: 1475
                if (field2314[var0.trianglesX[var9]] == field2308 && field2314[var0.trianglesY[var9]] == field2308 && field2314[var0.trianglesZ[var9]] == field2308) { // L: 1476
                    if (var0.types == null) { // L: 1477
                        var0.types = new byte[var0.trianglesCount];
                    }

                    var0.types[var9] = 2; // L: 1478
                }
            }

            for (var9 = 0; var9 < var1.trianglesCount; ++var9) { // L: 1481
                if (field2308 == field2290[var1.trianglesX[var9]] && field2308 == field2290[var1.trianglesY[var9]] && field2308 == field2290[var1.trianglesZ[var9]]) { // L: 1482
                    if (var1.types == null) { // L: 1483
                        var1.types = new byte[var1.trianglesCount];
                    }

                    var1.types[var9] = 2; // L: 1484
                }
            }

        }
    } // L: 1487


    static final int method3834(int var0, int var1) {
        var1 = (var0 & 127) * var1 >> 7; // L: 1618
        if (var1 < 2) { // L: 1619
            var1 = 2;
        } else if (var1 > 126) { // L: 1620
            var1 = 126;
        }

        return (var0 & 65408) + var1; // L: 1621
    }


    static final int method3817(int var0) {
        if (var0 < 2) { // L: 1625
            var0 = 2;
        } else if (var0 > 126) {
            var0 = 126; // L: 1626
        }

        return var0; // L: 1627
    }

    public short[] getFaceColors() {
        return colors;
    }

}
