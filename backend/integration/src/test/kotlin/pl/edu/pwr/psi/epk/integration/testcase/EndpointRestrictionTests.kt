package pl.edu.pwr.psi.epk.integration.testcase

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.http.HttpMethod
import pl.edu.pwr.psi.epk.integration.actor.*
import pl.edu.pwr.psi.epk.integration.dto.account.Role
import pl.edu.pwr.psi.epk.integration.endpoint.Endpoint

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EndpointsRestrictionTests: TestBase() {

    @ParameterizedTest(name = "Endpoint \"{1}\" is forbidden for role \"{0}\".")
    @MethodSource("forbiddenEndpointsProvider")
    fun `Endpoint is restricted for user`(
        actorRole: Role,
        endpointName: String,
        endpointMethod: HttpMethod,
        endpointPath: String,
        pathParams: Array<String>?,
        body: Any? = null
    ) {
        val actor = when (actorRole) {
            Role.PASSENGER -> PassengerActor.PASSENGERS.random()
            Role.TICKET_INSPECTOR -> TicketInspectorActor.TICKET_INSPECTORS.random()
            Role.PLANNER -> PlannerActor.PLANNERS.random()
            Role.MODERATOR -> ModeratorActor.MODERATORS.random()
        }
        actor.logsIn()
        actor.hasNoAccessToEndpoint(endpointMethod, endpointPath, body, *pathParams ?: arrayOf())
    }

    fun forbiddenEndpointsProvider(): List<Arguments> = Endpoint.EPK_FORBIDDEN_ENDPOINTS_FOR_ROLES.map { entry ->
        entry.value.map {
            Arguments.of(
                it,
                entry.key.name,
                entry.key.method,
                entry.key.path,
                entry.key.dummyPathArgs,
                entry.key.dummyBody
            )
        }
    }.flatten()

}