package pl.edu.pwr.psi.epk.account.service.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import pl.edu.pwr.psi.epk.account.config.JwtProperties
import pl.edu.pwr.psi.epk.account.exception.ExpiredTokenException
import pl.edu.pwr.psi.epk.account.exception.MalformedTokenException
import pl.edu.pwr.psi.epk.account.model.Token
import pl.edu.pwr.psi.epk.account.model.TokenPair
import pl.edu.pwr.psi.epk.account.model.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@Component
class JwtUtil(
        private val jwtProperties: JwtProperties
) {
    fun generateTokenPair(user: User, familyId: String): TokenPair {
        val accessToken = createTokenForUser(user, familyId, jwtProperties.accessTokenMaxAgeInMinutes)
        val refreshToke = createTokenForUser(user, familyId, jwtProperties.refreshTokenMaxAgeInMinutes)
        return TokenPair(accessToken, refreshToke)
    }

    fun getClaims(token: String?): Claims {
        return try {
            Jwts.parser()
                .setSigningKey(jwtProperties.secret)
                .parseClaimsJws(token).body
        } catch (e: JwtException) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException("Token is expired", e.claims)
                else -> throw MalformedTokenException("Token is malformed. Details: ${e.message}")
            }
        }
    }

    private fun createTokenForUser(
        user: User,
        fid: String,
        maxAgeInMinutes: Long
    ): Token {
        val claims = Jwts.claims()
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plusSeconds(maxAgeInMinutes * 60)))

        val stringToken = Jwts.builder()
            .setClaims(claims)
            .setSubject(user.email)
            .claim("role", user.role)
            .claim("fid", fid)
            .signWith(SignatureAlgorithm.HS512, jwtProperties.secret)
            .compact()

        return Token(stringToken, LocalDateTime.from(claims.expiration.toInstant()))
    }
}