package pl.edu.pwr.psi.epk.gateway.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix="jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenMaxAgeInMinutes: Long = 5,
    var refreshTokenMaxAgeInMinutes: Long = 4320
)
