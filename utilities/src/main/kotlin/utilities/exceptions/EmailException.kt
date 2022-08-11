package utilities.exceptions

/** Email пользователя не проходит проверку */
class EmailException(msg: String = "Email is required!"): Throwable(msg)
