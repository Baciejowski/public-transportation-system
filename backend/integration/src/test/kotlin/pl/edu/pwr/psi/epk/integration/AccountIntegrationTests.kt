package pl.edu.pwr.psi.epk.integration

import org.junit.jupiter.api.Test
import org.springframework.test.annotation.DirtiesContext
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.actor.TicketInspectorActor.Companion.TICKET_INSPECTOR_FELIX
import pl.edu.pwr.psi.epk.integration.actor.PlannerActor.Companion.PLANNER_EMILY
import pl.edu.pwr.psi.epk.integration.actor.ModeratorActor.Companion.MODERATOR_ANGELINA
import pl.edu.pwr.psi.epk.integration.dto.account.PassengerReadDto
import pl.edu.pwr.psi.epk.integration.dto.account.UserReadDto
import pl.edu.pwr.psi.epk.integration.util.TestUtils

class AccountIntegrationTests: TestBase() {

    @Test
    fun `Passenger gets user info`() {
        PASSENGER_JOHN.client = webTestClient
        val registerToken = PASSENGER_JOHN.logsIn()
        PASSENGER_JOHN.client = TestUtils.getWebClientWithAuthorization(webTestClient, registerToken.accessToken)

        val userInfo: PassengerReadDto = PASSENGER_JOHN.getsUserInfo()
        PASSENGER_JOHN.validate(userInfo)
    }

    @Test
    fun `Ticket inspector gets user info`() {
        TICKET_INSPECTOR_FELIX.client = webTestClient
        val registerToken = TICKET_INSPECTOR_FELIX.logsIn()
        TICKET_INSPECTOR_FELIX.client = TestUtils.getWebClientWithAuthorization(webTestClient, registerToken.accessToken)

        val userInfo: UserReadDto = TICKET_INSPECTOR_FELIX.getsUserInfo()
        TICKET_INSPECTOR_FELIX.validate(userInfo)
    }

    @Test
    fun `Planner gets user info`() {
        PLANNER_EMILY.client = webTestClient
        val registerToken = PLANNER_EMILY.logsIn()
        PLANNER_EMILY.client = TestUtils.getWebClientWithAuthorization(webTestClient, registerToken.accessToken)

        val userInfo: UserReadDto = PLANNER_EMILY.getsUserInfo()
        PLANNER_EMILY.validate(userInfo)
    }

    @Test
    fun `Moderator gets user info`() {
        MODERATOR_ANGELINA.client = webTestClient
        val registerToken = MODERATOR_ANGELINA.logsIn()
        MODERATOR_ANGELINA.client = TestUtils.getWebClientWithAuthorization(webTestClient, registerToken.accessToken)

        val userInfo: UserReadDto = MODERATOR_ANGELINA.getsUserInfo()
        MODERATOR_ANGELINA.validate(userInfo)
    }

}