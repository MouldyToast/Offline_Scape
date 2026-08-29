package com.near_reality.cache.custom

import com.zenyte.CacheManager
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.draw.font.Fonts
import mgi.types.config.draw.toBufferedImage
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.items.item
import mgi.types.config.items.loadSpritePixels
import mgi.types.draw.Rasterizer2D
import mgi.types.draw.Rasterizer3D
import mgi.types.draw.model.Model
import mgi.types.draw.model.ModelData
import mgi.types.draw.model.TextureProvider
import mgi.types.draw.sprite.SpritePixels
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetAdapter
import java.awt.dnd.DropTargetDropEvent
import java.io.File
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder
import javax.swing.event.ChangeListener

class ItemSpriteTool(private val cache: Cache) : JFrame("Item Sprite Tool") {
    private val itemIdSpinner = JSpinner()
    private val pitchSlider = JSlider(0, 2048, 0)
    private val rollSlider = JSlider(0, 2048, 0)
    private val yawSlider = JSlider(0, 2048, 0)
    private val zoomSlider = JSlider(0, 4000, 1000)
    private val offsetXSlider = JSlider(-200, 200, 0)
    private val offsetYSlider = JSlider(-200, 200, 0)
    private val imageLabel = JLabel().apply {
        horizontalAlignment = SwingConstants.CENTER
        verticalAlignment   = SwingConstants.CENTER
        border = LineBorder(Color.BLACK)
        // 1) force pack() to honor 360x320:
        preferredSize = Dimension(360, 320)
    }

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout()

        val controls = JPanel()
        controls.layout = BoxLayout(controls, BoxLayout.Y_AXIS)
        controls.add(labeled("Item ID", itemIdSpinner))
        controls.add(labeled("Pitch", pitchSlider))
        controls.add(labeled("Roll", rollSlider))
        controls.add(labeled("Yaw", yawSlider))
        controls.add(labeled("Zoom", zoomSlider))
        controls.add(labeled("Offset X", offsetXSlider))
        controls.add(labeled("Offset Y", offsetYSlider))
        add(controls, BorderLayout.EAST)
        val previewPanel = JPanel(BorderLayout()).apply {
            border = EmptyBorder(4,4,4,4)        // optional padding
            add(imageLabel, BorderLayout.CENTER)
        }
        add(previewPanel, BorderLayout.CENTER)

        val statusLabel = JLabel()
        add(statusLabel, BorderLayout.SOUTH)

        fun updateStatus() {
            statusLabel.text = listOf(
                "Pitch"   to pitchSlider.value,
                "Roll"    to rollSlider.value,
                "Yaw"     to yawSlider.value,
                "Zoom"    to zoomSlider.value,
                "OffsetX" to offsetXSlider.value,
                "OffsetY" to offsetYSlider.value
            ).joinToString("   ") { "${it.first}: ${it.second}" }
        }

        val listener = ChangeListener {
            renderSprite()
            updateStatus()
        }
        arrayOf(pitchSlider, rollSlider, yawSlider, zoomSlider, offsetXSlider, offsetYSlider).forEach {
            it.addChangeListener(listener)
        }
        arrayOf(itemIdSpinner).forEach {
            it.addChangeListener(listener)
        }

        dropTarget = DropTarget().apply {
            addDropTargetListener(object : DropTargetAdapter() {
                override fun drop(event: DropTargetDropEvent) {
                    try {
                        event.acceptDrop(DnDConstants.ACTION_COPY)
                        val files = event.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<java.io.File>
                        cachedModelFile = files.first()
                        val sprite = loadSpritePixelsFromFile(
                            file = files.first(),
                            scale = 10,
                            zoom = zoomSlider.value,
                            modelRoll = rollSlider.value,
                            modelYaw = yawSlider.value,
                            modelPitch = pitchSlider.value,
                            offsetX = offsetXSlider.value,
                            offsetY = offsetYSlider.value,
                        )
                        imageLabel.icon = ImageIcon(sprite.toBufferedImage())
                        imageLabel.text = ""
                        imageLabel.minimumSize = Dimension(360, 320)
                        imageLabel.maximumSize = Dimension(360, 320)
                        imageLabel.revalidate()
                        imageLabel.repaint()
                    } catch (e: Exception) {
                        imageLabel.text = "Error: ${'$'}{e.message}"
                    }
                }
            })
        }
        pack()
        isResizable = false      // lock the window size
        setLocationRelativeTo(null)
    }

    private var cachedModelFile: File? = null

    private fun labeled(name: String, component: JComponent): JPanel {
        return JPanel().apply {
            add(JLabel(name))
            add(component)
        }
    }

    private fun renderSprite() {
        if(itemIdSpinner.value == 0) {
            checkCachedModelFile()
            return
        }
        val itemId = (itemIdSpinner.value as? Int) ?: return
        val original = cache.item(itemId)
        val modified = original.toBuilder()
            .modelPitch(pitchSlider.value)
            .modelRoll(rollSlider.value)
            .modelYaw(yawSlider.value)
            .zoom(zoomSlider.value)
            .offsetX(offsetXSlider.value)
            .offsetY(offsetYSlider.value)
            .build()
        val sprite = modified.loadSpritePixels(cache, scale = 10)
        imageLabel.icon = ImageIcon(sprite.toBufferedImage())
        imageLabel.minimumSize = Dimension(360, 320)
        imageLabel.maximumSize = Dimension(360, 320)
        imageLabel.revalidate()
        imageLabel.repaint()
    }

    private fun checkCachedModelFile(): Int? {
        if(cachedModelFile == null) return null
        val sprite = loadSpritePixelsFromFile(
            file = cachedModelFile!!,
            scale = 10,
            zoom = zoomSlider.value,
            modelRoll = rollSlider.value,
            modelYaw = yawSlider.value,
            modelPitch = pitchSlider.value,
            offsetX = offsetXSlider.value,
            offsetY = offsetYSlider.value,
        )
        imageLabel.icon = ImageIcon(sprite.toBufferedImage())
        imageLabel.text = ""
        imageLabel.revalidate()
        imageLabel.repaint()
        return null
    }
}

fun main() {
    val cache = CacheManager.loadCache(Cache.openCache("cache/data/cache"))
    ItemDefinitions().load(cache)
    Fonts.loadNamed(cache)
    val textureProvider = TextureProvider(cache, 20, 0.6, 128)
    Rasterizer3D.Rasterizer3D_setTextureLoader(textureProvider)
    Rasterizer3D.Rasterizer3D_setBrightness(0.6)
    SwingUtilities.invokeLater { ItemSpriteTool(cache).isVisible = true }


}


fun loadSpritePixelsStandalone(
    scale: Int = 1,
    outlineType: Int = 1,
    shadowType: Int = 3153952,
    zoomed: Boolean = false,
    model: Model,
    zoom: Int,
    modelPitch: Int,
    modelRoll: Int,
    modelYaw: Int,
    offsetX: Int,
    offsetY: Int,
): SpritePixels {
    val prePixels = Rasterizer2D.Rasterizer2D_pixels
    val preWidth = Rasterizer2D.Rasterizer2D_width
    val preHeight = Rasterizer2D.Rasterizer2D_height

    val clipping = IntArray(4)
    Rasterizer2D.Rasterizer2D_getClipArray(clipping)
    val pixels = SpritePixels(scale * 36, scale * 32)
    Rasterizer2D.Rasterizer2D_replace(pixels.pixels, scale * 36, scale * 32)
    Rasterizer2D.Rasterizer2D_clear()
    Rasterizer3D.Rasterizer3D_setClipFromRasterizer2D()
    Rasterizer3D.setOffset(scale * 16, scale * 16)
    Rasterizer3D.rasterGouraudLowRes = false


    var var16: Int = zoom / scale // L: 400

    if (zoomed) { // L: 401
        var16 = (var16.toDouble() * 1.5).toInt()
    } else if (outlineType == 2) { // L: 402
        var16 = (1.04 * var16.toDouble()).toInt()
    }

    val var17 = var16 * Rasterizer3D.Rasterizer3D_sine[modelPitch] shr 16 // L: 403

    val var18 = var16 * Rasterizer3D.Rasterizer3D_cosine[modelPitch] shr 16 // L: 404

    model.calculateBoundsCylinder() // L: 405

    model.method4272(
        0,
        modelRoll,
        modelYaw,
        modelPitch,
        offsetX,
        model.height / 2 + var17 + offsetY,
        var18 + offsetY
    )

    if (outlineType >= 1)
        pixels.outline(1)

    if (outlineType >= 2)
        pixels.outline(16777215)

    if (shadowType != 0)
        pixels.shadow(shadowType)

    Rasterizer2D.Rasterizer2D_replace(pixels.pixels, scale * 36, scale * 32) // L: 411

    Rasterizer2D.Rasterizer2D_replace(prePixels, preWidth, preHeight) // L: 417
    Rasterizer2D.Rasterizer2D_setClipArray(clipping) // L: 418
    Rasterizer3D.Rasterizer3D_setClipFromRasterizer2D() // L: 419
    Rasterizer3D.rasterGouraudLowRes = true // L: 420
    return pixels
}

fun loadSpritePixelsFromFile(
    file: File,
    scale: Int = 1,
    outlineType: Int = 1,
    shadowType: Int = 3153952,
    zoomed: Boolean = false,
    zoom: Int,
    modelPitch: Int,
    modelRoll: Int,
    modelYaw: Int,
    offsetX: Int,
    offsetY: Int,

    ): SpritePixels {
    return try {
        val bytes = file.readBytes()
        val modelData = ModelData(bytes)
        val model = modelData.toModel(64, 768, -50, -10, -50)
        loadSpritePixelsStandalone(
            scale = scale,
            outlineType = outlineType,
            shadowType = shadowType,
            zoomed = zoomed,
            model = model,
            zoom = zoom,
            modelPitch = modelPitch,
            modelRoll = modelRoll,
            modelYaw = modelYaw,
            offsetX = offsetX,
            offsetY = offsetY
        )
    } catch (e: Exception) {
        println("Error loading model from file: ${e.message}")
        SpritePixels()
    }
}