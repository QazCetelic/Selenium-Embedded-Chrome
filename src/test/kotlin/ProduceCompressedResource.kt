import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import java.io.File


/**
 * Not a test but script to generate the compressed resources.
 */
fun main() {
    val src = "${System.getProperty("user.dir")}/src"
    val testResources   = File("$src/test/resources")
    val resources       = File("$src/main/resources")

    fun zip(platform: String) {
        val uncompressed = testResources.resolve(platform)
        val chromiumBrowser = uncompressed.resolve("chromium-browser")
        val chromeDriver    = uncompressed.resolve("chromedriver")

        val compressed = ZipFile(resources.resolve("${platform}_setup_resources.zip"))

        val parameters = ZipParameters().apply {
            compressionMethod   = CompressionMethod.DEFLATE
            // compressionLevel    = CompressionLevel.HIGHER
            compressionLevel    = CompressionLevel.PRE_ULTRA
        }
        compressed.addFolder(chromiumBrowser, parameters)
        compressed.addFile(chromeDriver, parameters)
    }

    zip("linux")
}