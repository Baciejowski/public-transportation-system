package pl.edu.pwr.psi.epk.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import pl.edu.pwr.psi.epk.gateway.service.JwtUtil
import reactor.core.publisher.Mono

@Component
class JwtFilter(
    private val jwtUtil: JwtUtil
): GatewayFilter {

    private val NON_FILTERED_ENDPOINTS = listOf(
        "/account/auth/login", "/account/auth/register", "/account/auth/refresh", "/account/test",
        "/ticket/test",
        "/schedule/test"
    )

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        if (!isRequestSecured(request))
            return chain.filter(exchange)

        val token = try {
            getTokenFromRequest(request)
        } catch (e: Exception) {
            println("Error getting token from request: $e")
            return exchange.response.apply { statusCode = HttpStatus.UNAUTHORIZED }.setComplete()
        }

        val claims = try {
            jwtUtil.getClaims(token)
        } catch (e: Exception) {
            println("Error getting claims for user token: $e")
            return exchange.response.apply { statusCode = HttpStatus.UNAUTHORIZED }.setComplete()
        }

        exchange.request.mutate()
            .header("user-id", claims.subject)
            .header("user-role", claims.getOrDefault("role", "").toString())
            .build()

        return chain.filter(exchange)
    }

    private fun isRequestSecured(request: ServerHttpRequest) = NON_FILTERED_ENDPOINTS.stream()
        .noneMatch { uri: String? -> request.uri.path.contains(uri!!) }

    private fun getTokenFromRequest(request: ServerHttpRequest): String = when {
        request.cookies.containsKey("AuthCookie") ->
            request.cookies["AuthCookie"]!!.first().value
        request.headers.containsKey("Authorization") ->
            parseTokenFromHeader(request.headers["Authorization"]!!.first())
        else ->
            throw IllegalStateException("Could not find auth token in provided request.")
    }

    private fun parseTokenFromHeader(header: String): String = header.substring("bearer".length).trim()

}