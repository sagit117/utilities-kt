/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */
package utilities.templating.components.inputin

import kotlinx.html.*
import kotlinx.html.attributes.Attribute
import kotlinx.html.attributes.EnumAttribute
import kotlinx.html.attributes.StringAttribute

/** HTML разметка для вывода input-in */
class InputInTag(
    initialAttributes: Map<String, String>,
    override val consumer: TagConsumer<*>
) : HTMLTag(
    "input-in",
    consumer,
    initialAttributes,
    null,
    false,
    false
), InputInAttribute { }

/** Эта функция применяется в html dsl */
@HtmlTagMarker
inline fun SectioningOrFlowContent.inputIn(
    classes: String? = null,
    label: String = "input",
    type: InputType = InputType.text,
    value: String = "",
    name: String = "",
    placeholder: String = "",
    crossinline block: InputInTag.() -> Unit = {}): Unit = InputInTag(
    attributesMapOf(
        "class", classes,
        "data-type", type.name,
        "data-label", label,
        "data-value", value,
        "data-name", name,
        "data-placeholder", placeholder,
    ),
    consumer
).visit(block)

interface InputInAttribute : CommonAttributeGroupFacadeFlowInteractivePhrasingContent { }

/** Подпись input */
@Suppress("unused")
var InputInAttribute.label: String
    get() = StringAttribute()[this, "data-label"]
    set(newValue) {
        StringAttribute()[this, "data-label"] = newValue
    }

/** Тип input */
@Suppress("unused")
var InputInAttribute.type: InputType
    get() = attributeInputTypeEnumInputTypeValues[this, "data-type"]
    set(newValue) {
        attributeInputTypeEnumInputTypeValues[this, "data-type"] = newValue
    }

val inputTypeValues : Map<String, InputType> = InputType.values().associateBy { it.realValue }
val attributeInputTypeEnumInputTypeValues : Attribute<InputType> = EnumAttribute(inputTypeValues)

/** Значение input */
@Suppress("unused")
var InputInAttribute.value: String
    get() = StringAttribute()[this, "data-value"]
    set(newValue) {
        StringAttribute()[this, "data-value"] = newValue
    }

/** Name input */
@Suppress("unused")
var InputInAttribute.name: String
    get() = StringAttribute()[this, "data-name"]
    set(newValue) {
        StringAttribute()[this, "data-name"] = newValue
    }

/** placeholder input */
@Suppress("unused")
var InputInAttribute.placeholder: String
    get() = StringAttribute()[this, "data-placeholder"]
    set(newValue) {
        StringAttribute()[this, "data-placeholder"] = newValue
    }
