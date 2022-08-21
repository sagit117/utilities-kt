/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.templating.components.selectin

import io.ktor.server.html.*
import kotlinx.html.*

/**
 * Компонент select in
 *
 * @property data List<SelectInData> данные для выбора
 * @property extClass css класс контейнера,
 * @property labelSelect подпись поля ввода,
 * @property hintSelect всплывающая подсказка,
 * @property idSelectIn id компонента,
 * @property nameSelect имя поля ввода,
 * @property selectedId id выбранной записи
 */
class SelectIn(
    val init: SelectIn.() -> Unit
): Template<FlowContent> {
    var data: List<SelectInData> = listOf()
    var extClass: Set<String> = setOf()
    var labelSelect: String = "Select:"
    var hintSelect: String = ""
    var idSelectIn: String = ""
    var nameSelect: String = ""
    var selectedId: String = ""

    override fun FlowContent.apply() {
        init()

        div {
            classes = extClass
            title = hintSelect

            label {
                htmlFor = idSelectIn
                +labelSelect
            }

            select {
                name = nameSelect
                attributes["autocomplete"] = "on"
                id = idSelectIn

                data.forEach { data ->
                    option {
                        value = data.id
                        selected = data.id == selectedId

                        +data.value
                    }
                }
            }
        }
    }
}

data class SelectInData(val id: String, val value: String)
