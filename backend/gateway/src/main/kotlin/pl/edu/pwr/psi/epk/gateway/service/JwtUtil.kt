package pl.edu.pwr.psi.epk.gateway.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.edu.pwr.psi.epk.gateway.config.properties.JwtProperties


@Component
class JwtUtil {

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    fun getClaims(token: String): Claims = Jwts.parser()
        .setSigningKey(jwtProperties.secret)
        .parseClaimsJws(token)
        .body

}