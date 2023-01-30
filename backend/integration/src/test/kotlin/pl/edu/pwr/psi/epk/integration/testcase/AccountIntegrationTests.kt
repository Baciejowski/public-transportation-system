package pl.edu.pwr.psi.epk.integration.testcase

import org.junit.jupiter.api.Test
import pl.edu.pwr.psi.epk.integration.actor.ModeratorActor.Companion.MODERATOR_ANGELINA
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.actor.PlannerActor.Companion.PLANNER_EMILY
import pl.edu.pwr.psi.epk.integration.actor.TicketInspectorActor.Companion.TICKET_INSPECTOR_FELIX
import pl.edu.pwr.psi.epk.integration.dto.account.PassengerReadDto
import pl.edu.pwr.psi.epk.integration.dto.account.UserReadDto
import pl.edu.pwr.psi.epk.integration.util.TestUtils

class AccountIntegrationTests: TestBase() {

    @Test
    fun `Passenger gets user info`() {
        PASSENGER_JOHN.logsIn()
        val userInfo: PassengerReadDto = PASSENGER_JOHN.getsUserInfo()
        PASSENGER_JOHN.validateEquality(userInfo)
    }

    @Test
    fun `Ticket inspector gets user info`() {
        TICKET_INSPECTOR_FELIX.logsIn()
        val userInfo: UserReadDto = TICKET_INSPECTOR_FELIX.getsUserInfo()
        TICKET_INSPECTOR_FELIX.validateUserEquality(userInfo)
    }

    @Test
    fun `Planner gets user info`() {
        PLANNER_EMILY.logsIn()
        val userInfo: UserReadDto = PLANNER_EMILY.getsUserInfo()
        PLANNER_EMILY.validateUserEquality(userInfo)
    }

    @Test
    fun `Moderator gets user info`() {
        MODERATOR_ANGELINA.logsIn()
        val userInfo: UserReadDto = MODERATOR_ANGELINA.getsUserInfo()
        MODERATOR_ANGELINA.validateUserEquality(userInfo)
    }

}