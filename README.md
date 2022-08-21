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
UtilitiesLibrary.randomCode(length) 

/**
 * Функции хеширования SHA-256
 * 
 * @param input строка для шифрования
 * @return зашифрованная строка
 */
UtilitiesLibrary.hash(input)  

/**
 * Класс для хранения и валидации Email
 *
 * @property email
 * @throws EmailException если email не пройдет валидацию
 */
UtilitiesLibrary.Companion.ValueClasses.Email(email) // Value класс для хранения и валидации email
object EmailSerializer: KSerializer<UtilitiesLibrary.Companion.ValueClasses.Email> // Сериализатор для UtilitiesLibrary.Values.Email(email)
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

/**
 * Закодировать строку в sha256
 */
fun String.sha256()
```

### extension ApplicationCall
```kotlin
/** Возвращает или параметры формы, или null */
suspend fun ApplicationCall.receiveParametersOrNull(): Parameters?
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
class EmailException(msg: String = "Email is required!"): Exception(msg)
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
```
```kotlin
/**
 * Базовый класс для страниц.
 */
abstract class BasePage: Template<FlowContent> {
    val content = TemplatePlaceholder<Template<FlowContent>>()
}
```
```kotlin
/** Компонент выводит customElements <flash-message> */
class FlashMessage<Context: FrontContext>(
    ctx: Context,
    init: FlashMessage<Context>.() -> Unit
) : Component<Context, FlashMessage<Context>>(ctx, init) {
    override fun FlowContent.apply() {
        flashMessage {}
    }
}
```
```kotlin
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
): Component<Context, T>

abstract class FrontContext {
    open var flashMessageDTO: FlashMessageDTO? = null
    open var formParameters: Parameters? = null
}

```
```kotlin
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
class InputIn<Context: FrontContext>(
    ctx: Context,
    init: InputIn<Context>.() -> Unit
) : Component<Context, InputIn<Context>>(ctx, init)
```
```kotlin
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
class SelectIn<Context: FrontContext>(
    ctx: Context,
    init: SelectIn<Context>.() -> Unit
): Component<Context, SelectIn<Context>>(ctx, init)

data class SelectInData(val id: String, val value: String)
```
```kotlin
/**
 * Компонент для выбора TimeZone
 */
class SelectInTimeZone<Context: FrontContext>(
    ctx: Context,
    init: SelectInTimeZone<Context>.() -> Unit
): Component<Context, SelectInTimeZone<Context>>(ctx, init)
```

### JWT

```kotlin
/**
 * Получение верифицированного токена
 *
 * @param secret ключ шифрования
 * @param token токен доступа
 *
 * @throws AlgorithmMismatchException – if the algorithm stated in the token's header it's not equal to the one defined in the JWTVerifier.
 * @throws SignatureVerificationException – if the signature is invalid.
 * @throws TokenExpiredException – if the token has expired.
 * @throws InvalidClaimException – if a claim contained a different value than the expected one.
 * @throws JWTVerificationException
 *
 * @return верифицированный токен
 */
fun checkJWT(secret: String, token: String): DecodedJWT?

/**
 * Генерация JWT
 *
 * @param secret ключ шифрования
 * @param expiresAtMs время жизни токена
 * @param claims список для передачи пар ключ-значения, который попадет в нагрузку
 *
 * @return JWT
 */
fun generateJWT(secret: String, expiresAtMs: Long, claims: List<Pair<String, String>>): String
```

### date 

```kotlin
/**
 * Генерация даты из срока жизни + текущая дата
 *
 * @param expiresAt срок жизни
 *
 * @return дата
 */
fun generateDateFromExpiredAt(expiresAtMs: Long): Date
```