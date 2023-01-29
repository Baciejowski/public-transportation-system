package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.util.TestUtils
import java.time.LocalDateTime

class TicketIntegrationTests : TestBase() {

    @Test
    fun `Passenger views the offer`() {
        PASSENGER_JOHN.client = webTestClient

        val johnsToken = PASSENGER_JOHN.logsIn()
        PASSENGER_JOHN.client = TestUtils.getWebClientWithAuthorization(webTestClient, johnsToken.accessToken)

        val ticketOffer = PASSENGER_JOHN.getsTicketOffer()

        val offeredTicket = ticketOffer[0]

        Assertions.assertThat(offeredTicket.offerStart).isBefore(LocalDateTime.now())
    }

}