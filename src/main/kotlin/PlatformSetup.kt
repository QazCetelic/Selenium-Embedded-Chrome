import net.lingala.zip4j.ZipFile
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.nio.file.Files

/**
 * Abstract class for platform setup that is extended by the platform specific classes.
 */
sealed class PlatformSetup {
    /**
     * The name used to get the resources for the platform.
     */
    protected abstract val platformName: String
    protected val browserFile = "chromium-browser"
    protected val driverFile  = "chromedriver"

    fun setup(installDirectory: File?, configureOptions: ChromeOptions.() -> Unit): ChromeDriver {
        val installFolder = extractResourcesAt(installDirectory)
        val (browserExe, driver) = getBrowserExeAndDriver(installFolder)
        System.setProperty("webdriver.chrome.driver", driver.path)

        // Configure the browser options
        val options = ChromeOptions()
        configureOptions(options)
        options.setBinary(browserExe)
        return ChromeDriver(options)
    }

    private fun extractResourcesAt(installDirectory: File? = null): File {
        if (installDirectory != null) {
            // Current state: Specified installDirectory might not exist or be a file
            if (!installDirectory.exists()) {
                installDirectory.mkdirs()
            }
            else {
                if (installDirectory.isFile) throw IllegalArgumentException("$installDirectory is a file, not a folder")
            }
            // Current state: installDirectory is an existing directory
        }

        val installFolder =
            installDirectory ?:
            getExistingTempInstall() ?:
            Files.createTempDirectory("selenium-embedded-chrome-driver").toFile()

        if (!verifyInstall(installFolder)) {
            // Current state: installFolder is an existing directory with no installation

            // ...chooses the correct browser binary and driver...
            val zippedFile = File(installFolder, "${platformName}_setup_resources.zip")

            // ...extracts the os-specific compressed resources to the install directory and extracts them...
            val loader: ClassLoader = ClassLoader.getSystemClassLoader()
            val stream = loader.getResourceAsStream("${platformName}_setup_resources.zip")!!
            zippedFile.writeBytes(stream.readBytes())
            ZipFile(zippedFile).extractAll(installFolder.path)
            zippedFile.delete()

            // Current state: installFolder is an existing directory installation set up
        }

        return installFolder
    }

    /**
     * Abstract method implemented by subclasses to resolve the specific paths to the browser binary and driver.
     */
    protected abstract fun getBrowserExeAndDriver(installFolder: File): Pair<File, File>

    /**
     * Attempts to find an existing installation of the browser and driver in a temporary directory.
     * @return The existing installation folder, or null if no existing installation was found
     */
    private fun getExistingTempInstall(): File? {
        val tempFolder = Files.createTempDirectory("").parent.toFile()
        val tempFiles = tempFolder.listFiles() ?: arrayOf()
        var existingInstall: File? = null
        for (file in tempFiles) {
            if (file.isDirectory && file.name.startsWith("selenium-embedded-chrome-driver")) {
                existingInstall = file
                break
            }
        }
        return existingInstall
    }

    /**
     * Does some basic checks to see if everything is installed.
     * Will let some invalid installations through, but it's very unlikely that it'll happen in practice.
     * @return Whether the directory passed some checks.
     */
    private fun verifyInstall(installDirectory: File): Boolean {
        val browser = installDirectory.resolve(browserFile)
        if (!browser.exists()) {
            System.err.println("$browserFile not found in $installDirectory")
            return false
        }
        if (!browser.isDirectory) {
            System.err.println("Expected $browserFile to be a directory")
            return false
        }
        if ((browser.listFiles() ?: arrayOf()).isEmpty()) {
            System.err.println("$browserFile should contain files")
            return false
        }

        val driver = installDirectory.resolve(driverFile)
        if (!driver.exists()) {
            System.err.println("$driverFile not found in $installDirectory")
            return false
        }
        if (!driver.isFile) {
            System.err.println("Expected $driverFile to be a file")
            return false
        }

        return true
    }
}