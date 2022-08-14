/*
 * Copyright (c) 2022.  sagit117@gmail.com <Pavel Aksenov>
 */

package utilities.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import utilities.date.generateDateFromExpiredAt

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
fun checkJWT(secret: String, token: String): DecodedJWT? {
    val jwtVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .build()

    return jwtVerifier.verify(token)
}

/**
 * Генерация JWT
 *
 * @param secret ключ шифрования
 * @param expiresAtMs время жизни токена
 * @param claims список для передачи пар ключ-значения, который попадет в нагрузку
 *
 * @return JWT
 */
fun generateJWT(secret: String, expiresAtMs: Long, claims: List<Pair<String, String>>): String {
    return JWT.create()
        .also {
            claims.forEach { claim ->
                it.withClaim(claim.first, claim.second)
            }
        }
        .withExpiresAt(generateDateFromExpiredAt(expiresAtMs))
        .sign(Algorithm.HMAC256(secret))
}
