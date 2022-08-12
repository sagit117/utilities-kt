# utilities

## Класс для вспомогательных методов

### UtilitiesLibrary

```kotlin
/**
 * Генерация случайного хэша заданной длины
 *
 * @param length длина хеша
 * @return строка хэша
 */
UtilitiesLibrary().randomCode(length) // Генерация случайного хэша заданной длины

/**
 * Класс для хранения и валидации Email
 *
 * @property email
 * @throws EmailException если email не пройдет валидацию
 */
UtilitiesLibrary.Values.Email(email) // Value класс для хранения и валидации email
object EmailSerializer: KSerializer<UtilitiesLibrary.Values.Email> // Сериализатор для UtilitiesLibrary.Values.Email(email)
```

### extension String

```kotlin
/**
 * Проверка на корректность email
 *
 * @return email
 *
 * @throws EmailException если email не проходит проверку
 */
fun String.isEmail(): String

/** Логирование строки */
fun String.log(moduleName: String): Logger = Logger(moduleName, this)
```

### connectors 

```kotlin
/** DSL клиент для http запросов */
open class ClientRequest(val client: HttpClient)

suspend inline fun requestGet(path: String, crossinline init: Resolver.() -> Unit)
suspend inline fun <reified T : Any> requestPost(path: String, body: T, crossinline init: Resolver.() -> Unit)

/** Postgres exposed connector */
object PostgresConnector {
    fun init(
        driverClassName: String,
        jdbcURL: String,
        user: String,
        password: String,
        initTables: Transaction.() -> Unit, // метод инициации таблиц
    )

    /**
     * Запрос к БД
     *
     * @param block функция запроса
     */
    fun <T> dbQuery(block: suspend () -> T): T = runBlocking {
        newSuspendedTransaction(Dispatchers.IO) {
            block()
        }
    }
}
```

### regex patterns

```kotlin
fun emailPattern(): Pattern = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)
```

### exception

```kotlin
/** Email пользователя не проходит проверку */
class EmailException(msg: String = "Email is required!"): Throwable(msg)
```

### ktor plugins

```kotlin
/**
 * Плагин формирует ID запроса и сохраняет значение в аттрибуты вызова.
 * Можно запросить call.attributes[AttributeKeys.RESPONSE_ID.value]
 */
val ResponseIdPlugin = createApplicationPlugin(name = "ResponseIdPlugin")

/** Аттрибуты вызова */
enum class AttributeKeys(val value: AttributeKey<String>) {
    RESPONSE_ID(AttributeKey("ResponseID"))
}
```

### ktor templating

```kotlin
/**
 * Класс для наследования базового слоя web интерфейса
 *
 * @property page страница, которая должна обрисовываться внутри слоя
 * @property styleUrl список css файлов для применения
 * @property pageTitle заголовок страницы
 */
abstract class BaseLayout: Template<HTML> {
    protected val content = TemplatePlaceholder<Template<FlowContent>>()
    lateinit var page: Template<FlowContent>
    lateinit var pageTitle: String

    /** css files for page */
    val styleUrl: MutableSet<String> = mutableSetOf()

    /** js files for page */
    val scriptUrl: MutableSet<String> = mutableSetOf()

    fun HTML.defaultHead()
}

/**
 * Базовый класс для страниц.
 */
abstract class BasePage: Template<FlowContent> {
    val content = TemplatePlaceholder<Template<FlowContent>>()
}

/** Компонент выводит customElements <flash-message> */
class FlashMessage(
    private val flashMessageDTO: FlashMessageDTO,
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.YYYY hh:mm:ss")
) : Template<FlowContent> {
    override fun FlowContent.apply() {
        flashMessage {}
    }
}
```