package pl.edu.pwr.psi.epk.integration.step

import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketDto
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketOfferDto

class TicketSteps {

    companion object {

        fun userGetsTicketOffer(webTestClient: WebTestClient): List<TicketOfferDto> =
            webTestClient.get()
                .uri("/ticket/tickets/offer")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(TicketOfferDto::class.java)
                .returnResult().responseBody!!

        fun userBuysTickets(webTestClient: WebTestClient, offeredTicketId: Long, quantity: Int = 1): List<TicketDto> =
            webTestClient.post()
                .uri("/ticket/tickets/offer/buy?offeredTicketId=$offeredTicketId&quantity=$quantity")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(TicketDto::class.java)
                .returnResult().responseBody!!

        fun userBuysTicketsExpect400(webTestClient: WebTestClient, offeredTicketId: Long, quantity: Int = 1) =
            webTestClient.post()
                .uri("/ticket/tickets/offer/buy?offeredTicketId=$offeredTicketId&quantity=$quantity")
                .exchange()
                .expectStatus().isBadRequest

        fun userGetsTickets(webTestClient: WebTestClient): List<TicketDto> =
            webTestClient.get()
                .uri("/ticket/tickets")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(TicketDto::class.java)
                .returnResult().responseBody!!

        fun userPunchesTicket(webTestClient: WebTestClient, ticketId: Long, rideId: Long): TicketDto =
            webTestClient.patch()
                .uri("/ticket/tickets/punch?ticketId=$ticketId&rideId=$rideId")
                .exchange()
                .expectStatus().isOk
                .expectBody(TicketDto::class.java)
                .returnResult().responseBody!!

        fun inspectorValidatesTicket(webTestClient: WebTestClient, ticketNo: Long, rideId: Long): Boolean =
            webTestClient.get()
                .uri("/ticket/tickets/validate?ticketId=$ticketNo&rideId=$rideId")
                .exchange()
                .expectStatus().isOk
                .expectBody(Boolean::class.java)
                .returnResult().responseBody!!
    }



}