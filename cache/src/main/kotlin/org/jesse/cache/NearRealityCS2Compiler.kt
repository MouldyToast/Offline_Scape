//package org.jesse.cache
//
//import com.github.michaelbull.logging.InlineLogger
//import io.netty.buffer.ByteBuf
//import io.netty.buffer.ByteBufAllocator
//import me.filby.neptune.clientscript.compiler.ConstantLoader
//import me.filby.neptune.clientscript.compiler.SymbolMapper
//import me.filby.neptune.clientscript.compiler.TsvSymbolLoader
//import me.filby.neptune.clientscript.compiler.command.*
//import me.filby.neptune.clientscript.compiler.command.debug.DumpCommandHandler
//import me.filby.neptune.clientscript.compiler.command.debug.ScriptCommandHandler
//import me.filby.neptune.clientscript.compiler.configuration.ClientScriptCompilerFeatureSet
//import me.filby.neptune.clientscript.compiler.trigger.ClientTriggerType
//import me.filby.neptune.clientscript.compiler.type.DbColumnType
//import me.filby.neptune.clientscript.compiler.type.ParamType
//import me.filby.neptune.clientscript.compiler.type.ScriptVarType
//import me.filby.neptune.clientscript.compiler.writer.BinaryScriptWriter
//import me.filby.neptune.runescript.compiler.ScriptCompiler
//import me.filby.neptune.runescript.compiler.codegen.script.RuneScript
//import me.filby.neptune.runescript.compiler.diagnostics.DiagnosticsHandler
//import me.filby.neptune.runescript.compiler.type.MetaType
//import me.filby.neptune.runescript.compiler.type.PrimitiveType
//import me.filby.neptune.runescript.compiler.type.Type
//import me.filby.neptune.runescript.compiler.type.wrapped.*
//import me.filby.neptune.runescript.compiler.writer.ScriptWriter
//import mgi.tools.jagcached.cache.Cache
//import mgi.tools.jagcached.cache.Group
//import java.io.File
//import java.nio.file.Path
//import kotlin.io.path.exists
//import kotlin.io.path.isDirectory
//
//class NearRealityCS2Compiler(
//    private val cs2SourcesDir: File,
//    private val cs2DependenciesDirs: List<File>,
//    private val symbolDirs: List<File>
//) {
//
//    constructor(
//        cs2SourcesDir: File,
//        cs2DependenciesDir: File,
//        symbolPaths: File
//    ) : this(cs2SourcesDir, listOf(cs2DependenciesDir), listOf(symbolPaths))
//
//    fun compileTo(cache: Cache): List<CacheOperation.LazyBinaryWrite.Group> {
//        val symbolMapper = SymbolMapper()
//        val sourceCS2Names = cs2SourcesDir.listFiles()?.filter { it.extension == "cs2" }?.map { it.nameWithoutExtension }?: emptyList()
//        val writer = BinaryScriptCacheWriter(cache, symbolMapper, sourceCS2Names)
//        val libraryPaths = cs2DependenciesDirs.map(File::toPath)
//        val sourcesPath = listOf(cs2SourcesDir.toPath())
//        val symbolPaths = symbolDirs.map(File::toPath)
//
//        val compiler = CS2Compiler(sourcesPath, libraryPaths, symbolPaths, writer, symbolMapper, cache, featureSet)
//        compiler.diagnosticsHandler = DiagnosticsHandler.BaseDiagnosticsHandler()
//        compiler.setup()
//        compiler.run()
//        return writer.lazyBinaryWriteOps.onEach { op ->
//            with(op) {
//                cache.apply()
//            }
//        }
//    }
//
//    private val featureSet = ClientScriptCompilerFeatureSet(
//        dbFindReturnsCount = true,
//        ccCreateAssertNewArg = false,
//        prefixPostfixExpressions = false,
//    )
//
//    private class CS2Compiler(
//        sourcePath: List<Path>,
//        libraryPaths: List<Path>,
//        private val symbolPaths: List<Path>,
//        scriptWriter: ScriptWriter,
//        private val mapper: SymbolMapper,
//        private val cache: Cache,
//        private val featureSet: ClientScriptCompilerFeatureSet
//    ) : ScriptCompiler(sourcePath, libraryPaths, scriptWriter = scriptWriter, features = featureSet) {
//
//        fun setup() {
//            triggers.registerAll<ClientTriggerType>()
//
//            // register types
//            types.registerAll<ScriptVarType>()
//            types.changeOptions("long") {
//                allowDeclaration = false
//            }
//
//            // special types for commands
//            types.register("hook", MetaType.Hook(MetaType.Unit))
//            types.register("stathook", MetaType.Hook(ScriptVarType.STAT))
//            types.register("invhook", MetaType.Hook(ScriptVarType.INV))
//            types.register("varphook", MetaType.Hook(VarPlayerType(MetaType.Any)))
//            types.register("dbcolumn", DbColumnType(MetaType.Any))
//            types.register("clientopnpc", MetaType.Script(ClientTriggerType.CLIENTOPNPC, MetaType.Unit, MetaType.Unit))
//            types.register("clientoploc", MetaType.Script(ClientTriggerType.CLIENTOPLOC, MetaType.Unit, MetaType.Unit))
//            types.register("clientopobj", MetaType.Script(ClientTriggerType.CLIENTOPOBJ, MetaType.Unit, MetaType.Unit))
//            types.register(
//                "clientopplayer",
//                MetaType.Script(ClientTriggerType.CLIENTOPPLAYER, MetaType.Unit, MetaType.Unit),
//            )
//            types.register("clientoptile", MetaType.Script(ClientTriggerType.CLIENTOPTILE, MetaType.Unit, MetaType.Unit))
//
//            // allow assignment of namedobj to obj
//            types.addTypeChecker { left, right -> left == ScriptVarType.OBJ && right == ScriptVarType.NAMEDOBJ }
//
//            // treat varp as alias of varp<int>
//            types.addTypeChecker { left, right ->
//                (left is VarPlayerType && left.inner == PrimitiveType.INT && right == ScriptVarType.VARP) ||
//                        (left == ScriptVarType.VARP && right is VarPlayerType && right.inner == PrimitiveType.INT)
//            }
//
//            // register the dynamic command handlers
//            if (featureSet.ccCreateAssertNewArg) {
//                addDynamicCommandHandler("cc_create", CcCreateCommandHandler(), dot = true)
//            }
//            addDynamicCommandHandler("enum", EnumCommandHandler())
//            addDynamicCommandHandler("oc_param", ParamCommandHandler(ScriptVarType.OBJ))
//            addDynamicCommandHandler("nc_param", ParamCommandHandler(ScriptVarType.NPC))
//            addDynamicCommandHandler("lc_param", ParamCommandHandler(ScriptVarType.LOC))
//            addDynamicCommandHandler("struct_param", ParamCommandHandler(ScriptVarType.STRUCT))
//
//            if (featureSet.dbFindReturnsCount) {
//                addDynamicCommandHandler("db_find", DbFindCommandHandler(true))
//                addDynamicCommandHandler("db_find_refine", DbFindCommandHandler(true))
//            } else {
//                addDynamicCommandHandler("db_find", DbFindCommandHandler(false))
//                addDynamicCommandHandler("db_find_with_count", DbFindCommandHandler(true))
//                addDynamicCommandHandler("db_find_refine", DbFindCommandHandler(false))
//                addDynamicCommandHandler("db_find_refine_with_count", DbFindCommandHandler(true))
//            }
//            addDynamicCommandHandler("db_getfield", DbGetFieldCommandHandler())
//
//            addDynamicCommandHandler("event_opbase", PlaceholderCommand(PrimitiveType.STRING, "event_opbase"))
//            addDynamicCommandHandler("event_mousex", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 1))
//            addDynamicCommandHandler("event_mousey", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 2))
//            addDynamicCommandHandler("event_com", PlaceholderCommand(ScriptVarType.COMPONENT, Int.MIN_VALUE + 3))
//            addDynamicCommandHandler("event_op", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 4))
//            addDynamicCommandHandler("event_comsubid", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 5))
//            addDynamicCommandHandler("event_com2", PlaceholderCommand(ScriptVarType.COMPONENT, Int.MIN_VALUE + 6))
//            addDynamicCommandHandler("event_comsubid2", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 7))
//            addDynamicCommandHandler("event_keycode", PlaceholderCommand(PrimitiveType.INT, Int.MIN_VALUE + 8))
//            addDynamicCommandHandler("event_keychar", PlaceholderCommand(PrimitiveType.CHAR, Int.MIN_VALUE + 9))
//
//            addDynamicCommandHandler("dump", DumpCommandHandler())
//            addDynamicCommandHandler("script", ScriptCommandHandler())
//
//            // symbol loaders
//            addSymConstantLoaders()
//
//            addSymLoader("bugtemplate", ScriptVarType.BUG_TEMPLATE)
//            addSymLoader("graphic", ScriptVarType.GRAPHIC)
//            addSymLoader("fontmetrics", ScriptVarType.FONTMETRICS)
//            addSymLoader("stat", ScriptVarType.STAT)
//            addSymLoader("synth", ScriptVarType.SYNTH)
//            addSymLoader("locshape", ScriptVarType.LOC_SHAPE)
//            addSymLoader("model", ScriptVarType.MODEL)
//            addSymLoader("interface", ScriptVarType.INTERFACE)
//            addSymLoader("toplevelinterface", ScriptVarType.TOPLEVELINTERFACE)
//            addSymLoader("overlayinterface", ScriptVarType.OVERLAYINTERFACE)
//            addSymLoader("component", ScriptVarType.COMPONENT)
//            addSymLoader("category", ScriptVarType.CATEGORY)
//            addSymLoader("wma", ScriptVarType.MAPAREA)
//            addSymLoader("mapelement", ScriptVarType.MAPELEMENT)
//            addSymLoader("loc", ScriptVarType.LOC)
//            addSymLoader("npc", ScriptVarType.NPC)
//            addSymLoader("obj", ScriptVarType.NAMEDOBJ)
//            addSymLoader("inv", ScriptVarType.INV)
//            addSymLoader("enum", ScriptVarType.ENUM)
//            addSymLoader("struct", ScriptVarType.STRUCT)
//            addSymLoader("seq", ScriptVarType.SEQ)
//            addSymLoader("dbtable", ScriptVarType.DBTABLE)
//            addSymLoader("dbrow", ScriptVarType.DBROW)
//            addSymLoader("dbcolumn") { DbColumnType(it) }
//            addSymLoader("param") { ParamType(it) }
//            addSymLoader("varp") { VarPlayerType(it) }
//            addSymLoader("varc") { VarClientType(it) }
//            addSymLoader("varbit", VarBitType)
//            addSymLoader("varclan") { VarClanType(it) }
//            addSymLoader("varclansetting") { VarClanSettingsType(it) }
//            addSymLoader("stringvector", ScriptVarType.STRINGVECTOR)
//        }
//
//        /**
//         * Looks for `constant.sym` and all `sym` files in `/constant` and registers them
//         * with a [ConstantLoader].
//         */
//        private fun addSymConstantLoaders() {
//            for (symbolPath in symbolPaths) {
//                // look for {symbol_path}/constant.sym
//                val constantsFile = symbolPath.resolve("constant.sym")
//                if (constantsFile.exists()) {
//                    addSymbolLoader(ConstantLoader(constantsFile))
//                }
//
//                // look for {symbol_path}/constant/**.sym
//                val constantDir = symbolPath.resolve("constant")
//                if (constantDir.exists() && constantDir.isDirectory()) {
//                    val files = constantDir
//                        .toFile()
//                        .walkTopDown()
//                        .filter { it.isFile && it.extension == "sym" }
//                    for (file in files) {
//                        addSymbolLoader(ConstantLoader(file.toPath()))
//                    }
//                }
//            }
//        }
//
//        /**
//         * Helper for loading external symbols from `sym` files with a specific [type].
//         */
//        private fun addSymLoader(name: String, type: Type) {
//            addSymLoader(name) { type }
//        }
//
//        /**
//         * Helper for loading external symbols from `sym` files with subtypes.
//         */
//        private fun addSymLoader(name: String, typeSuppler: (subTypes: Type) -> Type) {
//            for (symbolPath in symbolPaths) {
//                // look for {symbol_path}/{name}.sym
//                val typeFile = symbolPath.resolve("$name.sym")
//                if (typeFile.exists()) {
//                    addSymbolLoader(TsvSymbolLoader(mapper, typeFile, typeSuppler))
//                }
//
//                // look for {symbol_path}/{name}/**.sym
//                val typeDir = symbolPath.resolve(name)
//                if (typeDir.exists() && typeDir.isDirectory()) {
//                    val files = typeDir
//                        .toFile()
//                        .walkTopDown()
//                        .filter { it.isFile && it.extension == "sym" }
//                    for (file in files) {
//                        addSymbolLoader(TsvSymbolLoader(mapper, file.toPath(), typeSuppler))
//                    }
//                }
//            }
//        }
//    }
//
//
//    private class BinaryScriptCacheWriter(
//        cache: Cache,
//        symbolMapper: SymbolMapper,
//        private val sourceCS2Names: List<String>
//    ) : BinaryScriptWriter(symbolMapper, ByteBufAllocator.DEFAULT) {
//        private val logger = InlineLogger(BinaryScriptCacheWriter::class)
//        val lazyBinaryWriteOps = mutableListOf<CacheOperation.LazyBinaryWrite.Group>()
//
//        override fun outputScript(script: RuneScript, data: ByteBuf) {
//            if (sourceCS2Names.contains(script.fullName)) {
//                val dataArray = ByteArray(data.readableBytes())
//                data.readBytes(dataArray)
//                lazyBinaryWriteOps.add(CacheOperation.LazyBinaryWrite.Group(CacheArchiveType.CLIENTSCRIPTS) {
//                    val scriptId = idProvider.get(script.symbol)
//                    val entry = mgi.tools.jagcached.cache.File(0, "", mgi.utilities.ByteBuffer(dataArray.clone()))
//                    Group(scriptId, script.name, 999, entry)
//                })
//                logger.debug("Added lazy write op for script ${script.name}")
//            } else
//                logger.debug("Skipping packing of script ${script.name} because it is not a source script")
//        }
//    }
//
//
//}