/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.templating.components.inputin

import kotlinx.html.*
import utilities.UtilitiesLibrary
import utilities.templating.components.Component
import utilities.templating.components.FrontContext

/**
 * Компонент input-in
 *
 * @property labelInput подпись поля ввода,
 * @property typeInput тип поля ввода,
 * @property valueInput значение поля ввода,
 * @property extClass css класс контейнера,
 * @property nameInput имя поля ввода,
 * @property placeholderInput текст подсказка,
 * @property hintInput всплывающая подсказка
 *
 * @see InputType
 */
class InputIn<Context: FrontContext>(
    ctx: Context,
    init: InputIn<Context>.() -> Unit
) : Component<Context, InputIn<Context>>(ctx, init) {
    var labelInput: String = "Input:"
    var typeInput: InputType = InputType.text
    var valueInput: String = ""
    var extClass: Set<String> = mutableSetOf()
    var nameInput: String = ""
    var placeholderInput: String = ""
    var hintInput: String = ""

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
