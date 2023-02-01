package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions
import org.assertj.core.data.TemporalUnitWithinOffset
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.actor.TicketInspectorActor.Companion.TICKET_INSPECTOR_FELIX
import pl.edu.pwr.psi.epk.integration.dto.offer.OfferedTicket
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketDto
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketOfferDto
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
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
    fun `Passenger buys tickets`() {
        PASSENGER_JOHN.logsIn()

        val ticketOfferList: List<TicketOfferDto> = PASSENGER_JOHN.getsTicketOffer()
        val offer: TicketOfferDto = ticketOfferList[0]
        val offeredTickets = offer.tickets.toList()
        val offeredTicket: OfferedTicket = offeredTickets[0]

        val balanceBefore = PASSENGER_JOHN.getsUserInfo().accountBalance

        val tickets: List<TicketDto> = PASSENGER_JOHN.buysTickets(offeredTicket.id, 2)
        Assertions.assertThat(tickets).isNotEmpty

        val ticket = tickets[0]
        Assertions.assertThat(ticket.ticketNo).isGreaterThan(0)

        val balanceAfter = PASSENGER_JOHN.getsUserInfo().accountBalance
        val expectedBalance: Double =
            (balanceBefore.toBigDecimal() - offeredTicket.price.toBigDecimal() * 2.toBigDecimal()).toDouble()

        Assertions.assertThat(expectedBalance).isEqualTo(balanceAfter)

        val ticketList: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        Assertions.assertThat(ticketList).hasSize(2)
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

    @Test
    @Order(4)
    fun `Passenger punches ticket`() {
        PASSENGER_JOHN.logsIn()

        val ticketList: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        val rideId = -1L
        val punchedTicket = PASSENGER_JOHN.punchesTicket(ticketList[0].ticketNo, rideId)

        Assertions.assertThat(punchedTicket.punchTime)
            .isCloseToUtcNow(TemporalUnitWithinOffset(1, ChronoUnit.MINUTES))
    }

    @Test
    @Order(5)
    fun `Ticket inspector validates ticket`() {
        PASSENGER_JOHN.logsIn()
        val punchedTicket = PASSENGER_JOHN.getsTickets().first { it.punchTime != null }

        TICKET_INSPECTOR_FELIX.logsIn()
        val rideId = -1L

        val validationResult: Boolean = TICKET_INSPECTOR_FELIX.validatesTicket(punchedTicket.ticketNo, rideId)

        Assertions.assertThat(validationResult).isEqualTo(true)
    }

    @Test
    @Order(6)
    fun `Passenger tries to buy too many tickets, exceeding balance`() {
        PASSENGER_JOHN.logsIn()

        val ticketOfferList: List<TicketOfferDto> = PASSENGER_JOHN.getsTicketOffer()
        val offer: TicketOfferDto = ticketOfferList[0]
        val offeredTickets = offer.tickets.toList()
        val offeredTicket: OfferedTicket = offeredTickets.last()

        val ticketListBefore: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        val balanceBefore = PASSENGER_JOHN.getsUserInfo().accountBalance

        PASSENGER_JOHN.buysTicketsExpect400(offeredTicket.id, 10)

        val balanceAfter = PASSENGER_JOHN.getsUserInfo().accountBalance
        Assertions.assertThat(balanceBefore).isEqualTo(balanceAfter)

        val ticketListAfter: List<TicketDto> = PASSENGER_JOHN.getsTickets()
        Assertions.assertThat(ticketListBefore).isEqualTo(ticketListAfter)
    }
}
