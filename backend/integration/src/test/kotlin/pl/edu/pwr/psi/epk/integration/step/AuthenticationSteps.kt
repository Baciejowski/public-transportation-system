package pl.edu.pwr.psi.epk.integration.step

import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.ErrorDto
import pl.edu.pwr.psi.epk.integration.dto.account.LoginDto
import pl.edu.pwr.psi.epk.integration.dto.account.RegisterDto
import pl.edu.pwr.psi.epk.integration.dto.account.TokenReadDto
import reactor.core.publisher.Mono

class AuthenticationSteps {

    companion object {

        fun userRegisters(webTestClient: WebTestClient, registerDto: RegisterDto): TokenReadDto {
            return webTestClient.post()
                .uri("/account/auth/register")
                .body(Mono.just(registerDto), RegisterDto::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody(TokenReadDto::class.java)
                .returnResult().responseBody!!
        }

        fun userRegistersExpectingError(webTestClient: WebTestClient, registerDto: RegisterDto): ErrorDto {
            return webTestClient.post()
                .uri("/account/auth/register")
                .body(Mono.just(registerDto), RegisterDto::class.java)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody(ErrorDto::class.java)
                .returnResult().responseBody!!
        }

        fun userLogsIn(webTestClient: WebTestClient, loginDto: LoginDto): TokenReadDto {
            return webTestClient.post()
                .uri("/account/auth/login")
                .body(Mono.just(loginDto), LoginDto::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody(TokenReadDto::class.java)
                .returnResult().responseBody!!
        }

        fun userLogsInExpectingError(webTestClient: WebTestClient, loginDto: LoginDto): ErrorDto {
            return webTestClient.post()
                .uri("/account/auth/login")
                .body(Mono.just(loginDto), LoginDto::class.java)
                .exchange()
                .expectStatus().isBadRequest
                .expectBody(ErrorDto::class.java)
                .returnResult().responseBody!!
        }

        fun userRefreshesToken(webTestClient: WebTestClient): TokenReadDto {
            return webTestClient.post()
                .uri("/account/auth/refresh")
                .exchange()
                .expectStatus().isOk
                .expectBody(TokenReadDto::class.java)
                .returnResult().responseBody!!
        }

    }

}