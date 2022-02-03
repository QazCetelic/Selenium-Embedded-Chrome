import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File

/**
 * @param configureOptions Arguments to pass to the [ChromeOptions].
 * @param installDirectory Installation path for the chrome binaries.
 * If the [installDirectory] is `null`, it will add them to an OS-specified temporary directory (e.g. /tmp).
 * The temporary install directory may be erased by the OS.
 */
fun getEmbeddedChromeDriver(installDirectory: File? = null, configureOptions: (ChromeOptions.() -> Unit) = {}): ChromeDriver {
    val platform = System.getProperty("os.name").lowercase()
    return when {
        platform == "linux"     -> LinuxSetup.setup(installDirectory, configureOptions)
        "windows" in platform   -> TODO("Windows setup not implemented yet")
        "mac" in platform       -> TODO("Mac setup not implemented yet")
        else                    -> throw Exception("Unsupported platform: $platform")
    }
}