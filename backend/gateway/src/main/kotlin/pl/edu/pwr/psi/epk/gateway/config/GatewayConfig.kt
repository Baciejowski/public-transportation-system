package pl.edu.pwr.psi.epk.gateway.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.MergedAnnotation
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException
import pl.edu.pwr.psi.epk.gateway.filter.JwtFilter

@Configuration
class GatewayConfig(
    private val jwtFilter: JwtFilter
) {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
            .route("account") { p: PredicateSpec ->
                p.path("/account/**")
                        .filters { f -> f.filter(jwtFilter).rewritePath("^/account", "") }
                        .uri("lb://account")
            }
            .route("ticket") { p: PredicateSpec ->
                p.path("/ticket/**")
                        .filters { f -> f.filter(jwtFilter).rewritePath("^/ticket", "") }
                        .uri("lb://ticket")
            }
            .route("schedule") { p: PredicateSpec ->
                p.path("/schedule/**")
                        .filters { f -> f.filter(jwtFilter).rewritePath("^/schedule", "") }
                        .uri("lb://schedule")
            }
            .build()

    @Bean
    fun errorAttributes(): ErrorAttributes = CustomErrorAttributes()

}
