package utilities.templating.components

import io.ktor.http.*
import io.ktor.server.html.*
import kotlinx.html.FlowContent
import utilities.templating.components.flashmessage.FlashMessageDTO
import kotlin.reflect.full.primaryConstructor

/** Базовый класс для компонентов */
abstract class Component<Context: FrontContext, T>(
    protected open val ctx: Context,
    protected open val init: T.() -> Unit
): Template<FlowContent> {
    protected val content = TemplatePlaceholder<Template<FlowContent>>()
}

/**
 * Метод создания компонентов унаследованных от Component
 * Инициация компонента происходит внутри вызова создания.
 */
inline fun <Context: FrontContext, reified T : Component<Context, T>> createComponent(
    ctx: Context,
    noinline init: T.() -> Unit
): Component<Context, T> = if (T::class.primaryConstructor != null ) {
        val instance = T::class.primaryConstructor!!.call(ctx, init)
        instance.init()
        instance
    } else {
        throw Throwable("T class must have a primary constructor")
    }

abstract class FrontContext {
    open var flashMessageDTO: FlashMessageDTO? = null
    open var formParameters: Parameters? = null
}


