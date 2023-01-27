package pl.edu.pwr.psi.epk.gateway.config

import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.pwr.psi.epk.gateway.filter.JwtFilter

@Configuration
class GatewayConfig(
    private val jwtFilter: JwtFilter
) {

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator = builder.routes()
            .route("auth") { p: PredicateSpec ->
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

}