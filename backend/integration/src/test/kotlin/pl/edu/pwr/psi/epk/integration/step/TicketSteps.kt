package pl.edu.pwr.psi.epk.integration.step

import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.account.offer.TicketDto
import pl.edu.pwr.psi.epk.integration.dto.account.offer.TicketOfferDto

class TicketSteps {

    companion object {

        fun userGetsTicketOffer(webTestClient: WebTestClient): List<TicketOfferDto> =
            webTestClient.get()
                .uri("/ticket/tickets/offer")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(TicketOfferDto::class.java)
                .returnResult().responseBody!!

        fun userBuysTicket(webTestClient: WebTestClient, offeredTicketId: Long) {
            webTestClient.post()
                .uri("/ticket/tickets/offer/buy")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(TicketDto::class.java)
                .returnResult().responseBody!!
        }

    }



}