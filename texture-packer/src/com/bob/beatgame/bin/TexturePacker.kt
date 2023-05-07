package com.bob.beatgame.bin

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.backends.headless.HeadlessApplication
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.PixmapPacker
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter
import com.badlogic.gdx.tools.texturepacker.TexturePacker
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.*

fun main(arg: Array<String>) {
    packTextures()
    generateFonts()
}

private fun packTextures() {
    val settings = TexturePacker.Settings()
    settings.maxWidth = 2048
    settings.maxHeight = 2048
    settings.grid = false

    TexturePacker.process(settings, "sprites/", "../android/assets", "sprites")
}

private fun generateFonts() {
    HeadlessApplication(object : ApplicationAdapter() {
        override fun create() {

            val chars = requiredCharacters()

            Gdx.app.log("Font generation", "Creating fonts of size 18, 26, 58, 72 for characters: $chars")

            outputFont(18, 16, chars, 256, bold = true)
            outputFont(26, 23, chars, 256)
            outputFont(58, 38, chars, 512)
            outputFont(72, 46, chars, 512, scale = 2)

            Gdx.app.exit()
        }
    })
}


/**
 * Parse every i18n properties file, and gather up every unique character that is in use.
 * Will remove duplicates and sort somewhat.
 */
private fun requiredCharacters(): String {
    val propertiesDir = FileSystems.getDefault().getPath("../android/assets/i18n")
    val chars: String = Files.newDirectoryStream(propertiesDir)
        .filter { it.toString().endsWith(".properties") }
        .map { Properties().apply {
            load(InputStreamReader(Files.newInputStream(it), Charset.forName("UTF-8")))
        }}
        .fold("0123456789xX") { allChars, properties ->
            val values = properties.values
            val allValuesInOne = values.fold("") { allMessages, message ->
                allMessages + message
            }

            allChars + allValuesInOne
        }

    return chars.toSet().sorted().joinToString("")
}

/**
 * It is hard to change the font-size definition in the scene2d skin, so we instead maintain this
 * as constant while we tweak the sizeInFont until wee are happy with the output.
 */
private fun outputFont(sizeInSkin: Int, sizeInFont: Int, chars: String, pageSize: Int, bold: Boolean = false, scale: Int = 1) {

    Gdx.app.log("Font generation", "Creating fonts of size $sizeInFont. Saving as noto_mono_$sizeInSkin in skin/retrowars-skin_data/")

    val info = BitmapFontWriter.FontInfo()
    info.padding = BitmapFontWriter.Padding(1, 1, 1, 1)
    info.size = sizeInSkin
    info.smooth = false
    info.bold = bold
    info.aa = 0

    val param = FreeTypeFontGenerator.FreeTypeFontParameter()
    param.size = sizeInFont
    param.characters = chars
    param.renderCount = 1
    param.packer = BlockyPixmapPacker(scale, pageSize, pageSize, Pixmap.Format.RGBA8888, 1, false)

    val generator = FreeTypeFontGenerator(Gdx.files.absolute("/usr/share/fonts/truetype/noto/NotoMono-Regular.ttf"))

    val data = generator.generateData(param)

    val imagePages = BitmapFontWriter.writePixmaps(
        param.packer.pages,
        Gdx.files.absolute("../skin/retrowars-skin_data/"),
        "noto_mono_$sizeInSkin"
    )

    BitmapFontWriter.writeFont(
        data,
        imagePages,
        Gdx.files.absolute("../skin/retrowars-skin_data/noto_mono_$sizeInSkin.fnt"),
        info,
        pageSize,
        pageSize
    )

}

class BlockyPixmapPacker(
    private val scale: Int,
    pageWidth: Int,
    pageHeight: Int,
    pageFormat: Pixmap.Format?,
    padding: Int,
    duplicateBorder: Boolean,
): PixmapPacker(pageWidth, pageHeight, pageFormat, padding, duplicateBorder) {

    override fun pack(name: String?, image: Pixmap?): Rectangle {
        if (image != null && scale != 1) {
            scalePixmap(image)
        }
        return super.pack(name, image)
    }

    private fun scalePixmap(image: Pixmap) {
        val small = Pixmap(image.width / scale, image.height / scale, image.format)

        for (y in 0 until image.height / scale) {
            for (x in 0 until image.width / scale) {

                var colour = 0L
                var count = 0
                for (srcY in y * scale until y * scale + scale) {
                    for (srcX in x * scale until x * scale + scale) {
                        colour += image.getPixel(srcX, srcY)
                        count ++
                    }
                }

                val c = Color((colour / count.toFloat()).toInt())
                when {
                    c.a > 0.7f -> c.set(Color.WHITE)
                    c.a > 0.5 -> {
                        c.r = 0.66f
                        c.g = 0.66f
                        c.b = 0.66f
                        c.a = 0.66f
                    }
                    c.a > 0.25 -> {
                        c.r = 0.33f
                        c.g = 0.33f
                        c.b = 0.33f
                        c.a = 0.33f
                    }
                    else -> c.set(Color.CLEAR)
                }

                small.drawPixel(x, y, c.toIntBits())
            }
        }

        image.setColor(Color.CLEAR)
        image.fill()

        for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                image.drawPixel(x, y, small.getPixel(x / scale, y / scale))
            }
        }

        small.dispose()
    }
}
