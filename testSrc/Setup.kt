import com.intellij.ide.starter.di.di
import com.intellij.ide.starter.ide.IDETestContext
import com.intellij.ide.starter.ide.IdeDownloader
import com.intellij.ide.starter.ide.installer.ExistingIdeInstaller
import com.intellij.ide.starter.ide.installer.IdeInstallerFactory
import com.intellij.ide.starter.ide.installer.IdeInstallerFile
import com.intellij.ide.starter.models.IdeInfo
import com.intellij.ide.starter.models.TestCase
import com.intellij.ide.starter.project.GitHubProject
import com.intellij.ide.starter.runner.Starter
//import com.intellij.tools.ide.starter.build.server.idea.ultimate.IdeaUltimate
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.nio.file.Path
import java.nio.file.Paths

// To be used only for local test runs
class Setup {

    companion object {
        init {
            di = DI {
                extend(di)
                bindSingleton<IdeInstallerFactory>(overrides = true) { createInstallerFactory() }
                bindSingleton<IdeDownloader>(overrides = true) { IdeNotDownloader(Paths.get(PATH_TO_INSTALLED_IDE)) }
            }
        }

        private fun createInstallerFactory() = object : IdeInstallerFactory() {
            override fun createInstaller(ideInfo: IdeInfo, downloader: IdeDownloader) =
                ExistingIdeInstaller(Paths.get(PATH_TO_INSTALLED_IDE))
        }

        // This helpers are required to run locally installed IDE instead of downloading one
        class IdeNotDownloader(private val installer: Path) : IdeDownloader {
            override fun downloadIdeInstaller(ideInfo: IdeInfo, installerDirectory: Path): IdeInstallerFile {
                return IdeInstallerFile(installer, "locally-installed-ide")
            }
        }

        private val LOCAL_PATH = System.getProperty("user.home")

        // ----- CONFIGURATION SECTION ----
        private val PATH_TO_INSTALLED_IDE = "C:\\Users\\alexe\\AppData\\Local\\Programs\\IntelliJ IDEA"


        val IDE_TYPE = IdeInfo(
            productCode = "IU",
            platformPrefix = "idea",
            executableFileName = "idea64",
            fullName = "IntelliJ IDEA Ultimate"
        )

        /**
         * Sets up the test context by initializing the necessary objects and configurations.
         */
        fun setupTestContext(): IDETestContext {
            val testCase = TestCase(
                IDE_TYPE,
                GitHubProject.fromGithub(
                    branchName = "main",
                    repoRelativeUrl = "daryateplykh/jb-test-webStorm.git",
                )
            )
            return Starter
                .newContext(testName = "PerformanceTest", testCase = testCase)
                .updateGeneralSettings()
                .addProjectToTrustedLocations()
        }

    }

}