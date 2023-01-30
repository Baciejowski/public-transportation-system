package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.dto.offer.OfferedTicket
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketDto
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketOfferDto
import java.time.LocalDateTime

class TicketIntegrationTests : TestBase() {

    @Test
    @Order(1)
    fun `Passenger views the offer`() {
        PASSENGER_JOHN.logsIn()

        val ticketOfferList: List<TicketOfferDto> = PASSENGER_JOHN.getsTicketOffer()
        Assertions.assertThat(ticketOfferList).isNotEmpty

        val offer: TicketOfferDto = ticketOfferList[0]
        Assertions.assertThat(offer.offerStart).isBefore(LocalDateTime.now())

        val offeredTickets = offer.tickets.toList()
        Assertions.assertThat(offeredTickets).isNotEmpty

        val offeredTicket: OfferedTicket = offeredTickets[0]
        Assertions.assertThat(offeredTicket.id).isGreaterThan(0)
    }

    @Test
    @Order(2)
    fun `Passenger buys a ticket`() {
        PASSENGER_JOHN.logsIn()

        val ticketOfferList: List<TicketOfferDto> = PASSENGER_JOHN.getsTicketOffer()
        val offer: TicketOfferDto = ticketOfferList[0]
        val offeredTickets = offer.tickets.toList()
        val offeredTicket: OfferedTicket = offeredTickets[0]

        val balanceBefore = PASSENGER_JOHN.getsUserInfo().accountBalance

        val ticket: TicketDto = PASSENGER_JOHN.buysTicket(offeredTicket.id)
        Assertions.assertThat(ticket.ticketNo).isGreaterThan(0)

        val balanceAfter = PASSENGER_JOHN.getsUserInfo().accountBalance
        Assertions.assertThat(balanceBefore - balanceAfter).isEqualTo(offeredTicket.price)

        val ticketList: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        Assertions.assertThat(ticketList).isNotEmpty
        Assertions.assertThat(ticketList[0].ticketNo).isGreaterThan(0)
    }

    @Test
    @Order(3)
    fun `Passenger views tickets`() {
        PASSENGER_JOHN.logsIn()

        val ticketList: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        Assertions.assertThat(ticketList).isNotEmpty
        Assertions.assertThat(ticketList[0].ticketNo).isGreaterThan(0)
    }

}
