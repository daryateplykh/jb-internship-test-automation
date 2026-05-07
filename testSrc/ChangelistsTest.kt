import com.intellij.driver.sdk.ui.components.UiComponent.Companion.waitFound
import com.intellij.driver.sdk.ui.components.common.ideFrame
import com.intellij.driver.sdk.ui.components.elements.checkBox
import com.intellij.driver.sdk.ui.components.elements.tree
import com.intellij.driver.sdk.ui.components.settings.settingsDialog
import com.intellij.ide.starter.driver.engine.runIdeWithDriver
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.minutes
import org.junit.jupiter.api.Assertions.assertTrue

class ChangelistsTest {

    @Test
    fun `create changelists automatically should be selected`() {
        val testContext = Setup.setupTestContext()

        testContext.runIdeWithDriver().useDriverAndCloseIde {
            ideFrame {
                waitForIndicators(5.minutes)

                openSettingsDialog()

                settingsDialog {
                    val tree = tree()
                    tree.waitFound()
                    tree.clickPath("Version Control", "Changelists")

                    val checkbox = checkBox { byText("Create changelists automatically") }
                    checkbox.waitFound()

                    if (!checkbox.isSelected()) {
                        checkbox.click()
                    }

                    assertTrue(checkbox.isSelected()) {
                        "Checkbox should be selected"
                    }

                    okButton.click()
                }
            }
        }
    }
}