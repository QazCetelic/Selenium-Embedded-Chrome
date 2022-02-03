import java.io.File

object LinuxSetup: PlatformSetup() {
    override val platformName: String = "linux"

    override fun getBrowserExeAndDriver(installFolder: File): Pair<File, File> {
        val browser = installFolder.resolve(browserFile).resolve("chromium-browser.sh")
        val driver = installFolder.resolve(driverFile)
        return Pair(browser, driver)
    }
}