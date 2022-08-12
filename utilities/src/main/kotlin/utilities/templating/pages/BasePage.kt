package utilities.templating.pages

import io.ktor.server.html.*
import kotlinx.html.FlowContent

/**
 * Базовый класс для страниц.
 */
abstract class BasePage: Template<FlowContent> {
    val content = TemplatePlaceholder<Template<FlowContent>>()
}
