package pl.edu.pwr.psi.epk.integration.step

import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.account.BalanceTopUpDto
import pl.edu.pwr.psi.epk.integration.dto.account.PassengerReadDto
import pl.edu.pwr.psi.epk.integration.dto.account.UserReadDto
import reactor.core.publisher.Mono

class AccountSteps {

    companion object {

        fun userGetsAccountInfo(webTestClient: WebTestClient): UserReadDto =
            webTestClient.get()
                .uri("/account/account")
                .exchange()
                .expectStatus().isOk
                .expectBody(UserReadDto::class.java)
                .returnResult().responseBody!!

        fun userGetsPassengerInfo(webTestClient: WebTestClient): PassengerReadDto =
            webTestClient.get()
                .uri("/account/account")
                .exchange()
                .expectStatus().isOk
                .expectBody(PassengerReadDto::class.java)
                .returnResult().responseBody!!

        fun passengerTopsUpBalance(webTestClient: WebTestClient, balanceTopUpDto: BalanceTopUpDto): PassengerReadDto =
            webTestClient.post()
                .uri("/account/account/balance/top-up")
                .body(Mono.just(balanceTopUpDto), BalanceTopUpDto::class.java)
                .exchange()
                .expectStatus().isOk
                .expectBody(PassengerReadDto::class.java)
                .returnResult().responseBody!!


    }

}