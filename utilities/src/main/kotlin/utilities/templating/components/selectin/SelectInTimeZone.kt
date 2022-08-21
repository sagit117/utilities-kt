/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.templating.components.selectin

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import utilities.UtilitiesLibrary
import utilities.templating.components.Component
import utilities.templating.components.createComponent
import java.util.*

/**
 * Компонент для выбора TimeZone
 */
class SelectInTimeZone<Context>(
    ctx: Context,
    init: SelectInTimeZone<Context>.() -> Unit
): Component<Context, SelectInTimeZone<Context>>(ctx, init) {
    private val timeZoneListID = TimeZone.getAvailableIDs()
    private val currentTimeZoneId = TimeZone.getDefault()

    var extClassSelectTimeZone: Set<String> = setOf()
    var labelSelectTimeZone: String = "Select:"
    var hintSelectTimeZone: String = ""
    var selectedIdTimeZone: String? = null

    override fun FlowContent.apply() {
        val internalId = UtilitiesLibrary.randomCode(10)

        insert(createComponent<Context, SelectIn<Context>>(ctx) {
            labelSelect = labelSelectTimeZone
            extClass = extClassSelectTimeZone
            idSelectIn = "select_$internalId"
            hintSelect = hintSelectTimeZone
            selectedId = selectedIdTimeZone ?: currentTimeZoneId.id
            nameSelect = "timeZone"
            data = timeZoneListID.map {
                SelectInData(id = it, value = TimeZone.getTimeZone(it).displayName)
            }
        }, content)
    }
}
