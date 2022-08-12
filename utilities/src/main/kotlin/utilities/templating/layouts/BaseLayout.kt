package utilities.templating.layouts

import io.ktor.server.html.*
import kotlinx.html.*

/**
 * Класс для наследования базового слоя web интерфейса
 *
 * @property page страница, которая должна обрисовываться внутри слоя
 * @property styleUrl список css файлов для применения
 * @property pageTitle заголовок страницы
 */
abstract class BaseLayout: Template<HTML> {
    protected val content = TemplatePlaceholder<Template<FlowContent>>()
    lateinit var page: Template<FlowContent>
    lateinit var pageTitle: String

    /** css files for page */
    val styleUrl: MutableSet<String> = mutableSetOf()
    /** js files for page */
    val scriptUrl: MutableSet<String> = mutableSetOf()

    fun HTML.defaultHead() {
        return head {
            meta(name = "viewport", content = "width=device-width, initial-scale=1")
            meta(charset = "utf-8")

            link {
                rel = "shortcut icon"
                href = "/static/favicon.png"
                type = "image/png"
            }
            link {
                rel = "manifest"
                href = "/static/manifest.json"
            }

            styleLink(url = "/static/index.css")
            styleUrl.forEach {
                styleLink(it)
            }

            scriptUrl.forEach {
                script {
                    type = "module"
                    src = it
                }
            }

            title(pageTitle)
        }
    }
}
