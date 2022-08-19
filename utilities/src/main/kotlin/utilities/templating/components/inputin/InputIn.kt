/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.templating.components.inputin

import io.ktor.server.html.*
import kotlinx.html.*
import utilities.UtilitiesLibrary

/**
 * Компонент input-in
 *
 * @property labelInput подпись поля ввода,
 * @property typeInput тип поля ввода,
 * @property valueInput значение поля ввода,
 * @property extClass css класс контейнера
 * @property nameInput имя поля ввода
 * @property placeholderInput текст подсказка
 * @property hintInput всплывающая подсказка
 *
 * @see InputType
 */
class InputIn(
    private val labelInput: String = "Input:",
    private val typeInput: InputType = InputType.text,
    private val valueInput: String = "",
    private val extClass: Set<String> = mutableSetOf(),
    private val nameInput: String = "",
    private val placeholderInput: String = "",
    private val hintInput: String = ""
) : Template<FlowContent> {
    override fun FlowContent.apply() {
        val internalId = UtilitiesLibrary.randomCode(10)

        inputIn {
            label = labelInput
            type = typeInput
            value = valueInput
            classes = extClass
            name = nameInput
            placeholder = placeholderInput
            title = hintInput

            div {
                classes = setOf("inputin-container")

                label {
                    htmlFor = "input_$internalId"
                    +labelInput
                }

                input {
                    id = "input_$internalId"
                    type = typeInput
                    value = valueInput
                    name = nameInput
                    placeholder = placeholderInput
                }
            }
        }
    }
}
