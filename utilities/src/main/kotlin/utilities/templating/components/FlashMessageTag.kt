package utilities.templating.components

import kotlinx.html.*
import kotlinx.html.attributes.Attribute
import kotlinx.html.attributes.AttributeEncoder
import kotlinx.html.attributes.EnumAttribute
import kotlinx.html.attributes.StringAttribute
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/** HTML разметка для вывода flash-message */
class FlashMessageTag(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) : HTMLTag(
    "flash-message",
    consumer,
    initialAttributes,
    null,
    false,
    false
), FlashMessageAttribute { }

/** Эта функция применяется в html dsl */
@HtmlTagMarker
inline fun SectioningOrFlowContent.flashMessage(
    classes: String? = null,
    title: String = "",
    message: String = "",
    date: LocalDateTime = LocalDateTime.now(),
    type: FlashMessageType = FlashMessageType.INFO,
    crossinline block: FlashMessageTag.() -> Unit = {}): Unit = FlashMessageTag(
    attributesMapOf(
        "class", classes,
        "data-title", title,
        "data-message", message,
        "data-date", date.format(DateTimeFormatter.ISO_DATE_TIME),
        "data-type", type.name,
    ),
    consumer
).visit(block)

interface FlashMessageAttribute : CommonAttributeGroupFacadeFlowInteractivePhrasingContent { }

/** Тема flash message */
@Suppress("unused")
var FlashMessageAttribute.title: String
    get() = StringAttribute()[this, "data-title"]
    set(newValue) {
        StringAttribute()[this, "data-title"] = newValue
    }

/** Сообщение flash message */
@Suppress("unused")
var FlashMessageAttribute.message: String
    get() = StringAttribute()[this, "data-message"]
    set(newValue) {
        StringAttribute()[this, "data-message"] = newValue
    }

/** Дата flash message */
@Suppress("unused")
var FlashMessageAttribute.date: LocalDateTime
    get() = DateTimeAttribute()[this, "data-date"]
    set(newValue) {
        DateTimeAttribute()[this, "data-date"] = newValue
    }

/** Тип flash message */
@Suppress("unused")
var FlashMessageAttribute.type: FlashMessageType
    get() = attributeFlashMessageTypeEnumFlashMessageTypeValues[this, "data-type"]
    set(newValue) {
        attributeFlashMessageTypeEnumFlashMessageTypeValues[this, "data-type"] = newValue
    }

val FlashMessageTypeValues : Map<String, FlashMessageType> =
    FlashMessageType.values().associateBy { it.realValue }
val attributeFlashMessageTypeEnumFlashMessageTypeValues : Attribute<FlashMessageType> =
    EnumAttribute(FlashMessageTypeValues)

data class FlashMessageDTO(
    val title: String,
    val message: String,
    val type: FlashMessageType,
    val date: LocalDateTime = LocalDateTime.now()
)

enum class FlashMessageType(override val realValue : String) : AttributeEnum {
    ERROR("error"),
    WARNING("warning"),
    INFO("info"),
    SUCCESS("success")
}

object DateEncoder : AttributeEncoder<LocalDateTime> {
    override fun decode(attributeName: String, value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
    }

    override fun encode(attributeName: String, value: LocalDateTime): String {
        return value.toString()
    }
}

class DateTimeAttribute : Attribute<LocalDateTime>(DateEncoder)
