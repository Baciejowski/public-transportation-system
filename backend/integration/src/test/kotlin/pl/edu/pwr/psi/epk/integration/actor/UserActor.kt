package pl.edu.pwr.psi.epk.integration.actor

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertAll
import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.ErrorDto
import pl.edu.pwr.psi.epk.integration.dto.account.*
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketDto
import pl.edu.pwr.psi.epk.integration.dto.offer.TicketOfferDto
import pl.edu.pwr.psi.epk.integration.dto.schedule.*
import pl.edu.pwr.psi.epk.integration.step.AccountSteps
import pl.edu.pwr.psi.epk.integration.step.AuthenticationSteps
import pl.edu.pwr.psi.epk.integration.step.ScheduleSteps
import pl.edu.pwr.psi.epk.integration.step.TicketSteps
import pl.edu.pwr.psi.epk.integration.util.TestUtils
import java.time.LocalDateTime
import java.time.ZoneOffset

abstract class UserActor {
    var email: String
    var password: String
    var firstName: String
    var lastName: String

    var userId: Long? = null
    var token: TokenReadDto? = null
    var client: WebTestClient = TestUtils.configureWebTestClient()
    val reset: () -> Unit

    abstract val role: Role

    constructor(email: String, password: String, firstName: String, lastName: String) {
        this.reset = {
            this.email = email
            this.password = password
            this.firstName = firstName
            this.lastName = lastName

            this.userId = null
            this.token = null
            this.client = TestUtils.configureWebTestClient()
        }
        this.email = email
        this.password = password
        this.firstName = firstName
        this.lastName = lastName
    }

    private fun saveObtainedToken(tokenDto: TokenReadDto) {
        token = tokenDto
        client = TestUtils.getWebClientWithAuthorization(client, tokenDto.accessToken)
    }

    fun registers(): TokenReadDto {
        val signUpDto = RegisterDto(email, password, firstName, lastName)
        val tokenDto = AuthenticationSteps.userRegisters(client, signUpDto)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    fun registersExpectingError(): ErrorDto {
        val signUpDto = RegisterDto(email, password, firstName, lastName)
        return AuthenticationSteps.userRegistersExpectingError(client, signUpDto)
    }

    fun logsIn(): TokenReadDto {
        val signInDto = LoginDto(email, password)
        val tokenDto = AuthenticationSteps.userLogsIn(client, signInDto)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    fun logsInExpectingError(): ErrorDto {
        val signInDto = LoginDto(email, password)
        return AuthenticationSteps.userLogsInExpectingError(client, signInDto)
    }

    fun refreshesToken(): TokenReadDto {
        client = client.mutate().defaultHeaders { it.setBearerAuth(token!!.refreshToken) }.build()
        val tokenDto = AuthenticationSteps.userRefreshesToken(client)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    open fun getsUserInfo(): UserReadDto = AccountSteps.userGetsAccountInfo(client)

    fun validateTokenEquality(tokenReadDto: TokenReadDto) {
        assertAll(
            { Assertions.assertThat(tokenReadDto.accessToken).isNotNull() },
            { Assertions.assertThat(tokenReadDto.accessTokenExpirationDate).isAfter(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(1)) },
            { Assertions.assertThat(tokenReadDto.refreshToken).isNotNull() },
            { Assertions.assertThat(tokenReadDto.refreshTokenExpirationDate).isAfter(LocalDateTime.now(ZoneOffset.UTC).plusMinutes(1)) },
            { Assertions.assertThat(tokenReadDto.type).isEqualTo("Bearer") }
        )
    }

    open fun validateUserEquality(userReadDto: UserReadDto) {
        assertAll(
            { Assertions.assertThat(userReadDto.id).isNotNull() },
            { Assertions.assertThat(userReadDto.firstName).isEqualTo(this.firstName) },
            { Assertions.assertThat(userReadDto.lastName).isEqualTo(this.lastName) },
            { Assertions.assertThat(userReadDto.email).isEqualTo(this.email) },
            { Assertions.assertThat(userReadDto.role).isEqualTo(this.role) },
        )
    }

}

class PassengerActor(
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : UserActor(email, password, firstName, lastName) {

    override val role: Role = Role.PASSENGER

    companion object {

        // REGISTERED
        var PASSENGER_JOHN = PassengerActor("john.doe@mail.com", "secret", "John", "Doe")
        var PASSENGER_OLIVIA = PassengerActor("olivia.may@mail.com", "secret", "Olivia", "May")
        var PASSENGER_GREGORY = PassengerActor("gregory.trevino@mail.com", "secret", "Gregory", "Trevino")
        var PASSENGER_DONALD = PassengerActor("donald.soto@mail.com", "secret", "Donald", "Soto")
        var PASSENGER_FREDDIE = PassengerActor("freddie.stuart@mail.com", "secret", "Freddy", "Stuart")
        var PASSENGER_EMIL = PassengerActor("emil.briggs@mail.com", "secret", "Emil", "Briggs")

        // NON REGISTERED
        var NR_PASSENGER_MARK = PassengerActor("mark.twain@mail.com", "secret", "Mark", "Twain")

        var PASSENGERS = listOf(PASSENGER_JOHN, PASSENGER_OLIVIA, PASSENGER_GREGORY, PASSENGER_DONALD, PASSENGER_FREDDIE, PASSENGER_EMIL, NR_PASSENGER_MARK)
    }

    override fun getsUserInfo(): PassengerReadDto = AccountSteps.userGetsPassengerInfo(client)

    fun topsUpBalance(amount: Double): PassengerReadDto = AccountSteps.passengerTopsUpBalance(client, BalanceTopUpDto(amount))

    fun getsTicketOffer(): List<TicketOfferDto> = TicketSteps.userGetsTicketOffer(client)

    fun buysTicket(offeredTicketId: Long): TicketDto = TicketSteps.userBuysTicket(client, offeredTicketId)

    fun getsTickets(): List<TicketDto> = TicketSteps.userGetsTickets(client)

    fun getsLines(): List<LineDto> = ScheduleSteps.getLines(client)

    fun getsStops(): List<StopManifestDto> = ScheduleSteps.getStops(client)

    fun getsBuses(): List<BusDto> = ScheduleSteps.getBuses(client)

    fun getsLineRoutes(lineId: Long): List<RouteManifestDto> = ScheduleSteps.getLineRoutes(client, lineId)
    fun triesToGetLineRoutes(lineId: Long): WebTestClient.ResponseSpec =
        ScheduleSteps.tryGetLineRoutes(client, lineId)

    fun getsRoute(routeId: Long): RouteDetailDto = ScheduleSteps.getRoute(client, routeId)
    fun triesToGetRoute(routeId: Long): WebTestClient.ResponseSpec =
        ScheduleSteps.tryGetRoute(client, routeId)

    fun getsStopLines(stopId: Long): List<LineDto> = ScheduleSteps.getStopLines(client, stopId)
    fun triesToGetStopLines(stopId: Long): WebTestClient.ResponseSpec =
        ScheduleSteps.tryGetStopLines(client, stopId)

    fun getsStopDepartures(stopId: Long, length: Int): List<StopDepartureDto> =
        ScheduleSteps.getStopDepartures(client, stopId, length)
    fun triesToGetStopDepartures(stopId: Long, length: Int): WebTestClient.ResponseSpec =
        ScheduleSteps.tryGetStopDepartures(client, stopId, length)

    fun punchesTicket(ticketId: Long, rideId: Long): TicketDto =
        TicketSteps.userPunchesTicket(client, ticketId, rideId)

    fun validateEquality(userReadDto: PassengerReadDto) {
        super.validateUserEquality(userReadDto)
    }

}

class TicketInspectorActor(
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : UserActor(email, password, firstName, lastName) {

    override val role: Role = Role.TICKET_INSPECTOR

    companion object {
        var TICKET_INSPECTOR_FELIX = TicketInspectorActor("felix.carey@mail.com", "secret", "Felix", "Carey")
        var TICKET_INSPECTOR_STEPHANIE = TicketInspectorActor("stephanie.duran@mail.com", "secret", "Stephanie", "Duran")
        var TICKET_INSPECTOR_DORIS = TicketInspectorActor("doris.sandoval@mail.com", "secret", "Doris", "Sandoval")

        var TICKET_INSPECTORS = listOf(TICKET_INSPECTOR_FELIX, TICKET_INSPECTOR_STEPHANIE, TICKET_INSPECTOR_DORIS)
    }

    fun validatesTicket(ticketNo: Long, rideId: Long): Boolean =
        TicketSteps.inspectorValidatesTicket(client, ticketNo, rideId)

}

class PlannerActor(
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : UserActor(email, password, firstName, lastName) {

    override val role: Role = Role.PLANNER

    companion object {
        var PLANNER_MARCEL = PlannerActor("marcel.durham@mail.com", "secret", "Marcel", "Durham")
        var PLANNER_EMILY = PlannerActor("emily.wang@mail.com", "secret", "Emily", "Wang")

        var PLANNERS = listOf(PLANNER_MARCEL, PLANNER_EMILY)
    }

}

class ModeratorActor(
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : UserActor(email, password, firstName, lastName) {

    override val role: Role = Role.MODERATOR

    companion object {
        var MODERATOR_CARTER = ModeratorActor("carter.graves@mail.com", "secret", "Carter", "Graves")
        var MODERATOR_ANGELINA = ModeratorActor("angelina.shelton@mail.com", "secret", "Angelina", "Shelton")

        var MODERATORS = listOf(MODERATOR_CARTER, MODERATOR_ANGELINA)
    }

}
