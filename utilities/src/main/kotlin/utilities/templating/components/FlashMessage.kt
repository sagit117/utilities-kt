package utilities.templating.components

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.role
import java.time.format.DateTimeFormatter

/** Компонент выводит customElements <flash-message> */
class FlashMessage(
    private val flashMessageDTO: FlashMessageDTO,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY hh:mm:ss")
) : Template<FlowContent> {
    override fun FlowContent.apply() {
        flashMessage {
            title = flashMessageDTO.title
            message = flashMessageDTO.message
            date = flashMessageDTO.date
            type = flashMessageDTO.type

            div {
                classes = setOf("flash-message-container", flashMessageDTO.type.realValue)
                role = "alert"

                div {
                    classes = setOf("flash-message-head")
                    role = "heading"

                    div {
                        classes = setOf("flash-message-title")

                        +flashMessageDTO.title
                    }

                    div {
                        classes = setOf("flash-message-date")

                        +flashMessageDTO.date.format(dateTimeFormatter)
                    }
                }

                div {
                    classes = setOf("flash-message-body")

                    +flashMessageDTO.message
                }
            }
        }
    }
}
