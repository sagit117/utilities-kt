/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.templating.components.flashmessage

import kotlinx.html.FlowContent
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.role
import utilities.templating.components.*
import java.time.format.DateTimeFormatter

/** Компонент выводит customElements <flash-message> */
class FlashMessage<Context: FrontContext>(
    ctx: Context,
    init: FlashMessage<Context>.() -> Unit
) : Component<Context, FlashMessage<Context>>(ctx, init) {
    private val flashMessageDTO: FlashMessageDTO? = ctx.flashMessageDTO
    var dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY hh:mm:ss")

    override fun FlowContent.apply() {
        if (flashMessageDTO == null) return

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
