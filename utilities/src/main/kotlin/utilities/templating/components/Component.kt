package utilities.templating.components

import io.ktor.server.html.*
import kotlinx.html.FlowContent
import kotlin.reflect.full.primaryConstructor

/** Базовый класс для компонентов */
abstract class Component<Context, T>(
    protected open val ctx: Context,
    protected open val init: T.() -> Unit
): Template<FlowContent> {
    protected val content = TemplatePlaceholder<Template<FlowContent>>()
}

/** Метод создания компонентов унаследованных от Component */
inline fun <Context, reified T : Component<Context, T>> createComponent(
    ctx: Context,
    noinline init: T.() -> Unit
): Component<Context, T> = if (T::class.primaryConstructor != null ) {
        T::class.primaryConstructor!!.call(ctx, init)
    } else {
        throw Throwable("T class must have a primary constructor")
    }


