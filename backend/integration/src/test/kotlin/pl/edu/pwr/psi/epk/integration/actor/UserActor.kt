package pl.edu.pwr.psi.epk.integration.actor

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.assertAll
import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.account.*
import pl.edu.pwr.psi.epk.integration.step.AccountSteps
import pl.edu.pwr.psi.epk.integration.step.AuthenticationSteps
import java.time.LocalDateTime
import java.time.ZoneOffset

abstract class UserActor(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
) {
    abstract val role: Role

    var userId: Long? = null
    var token: TokenReadDto? = null
    var client: WebTestClient? = null

    private fun saveObtainedToken(tokenDto: TokenReadDto) {
        token = tokenDto
        client = client!!.mutate().defaultHeaders { it.setBearerAuth(tokenDto.accessToken) }.build()
    }

    fun registers(): TokenReadDto {
        val signUpDto = RegisterDto(email, password, firstName, lastName)
        val tokenDto = AuthenticationSteps.userRegisters(client!!, signUpDto)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    fun logsIn(): TokenReadDto {
        val signInDto = LoginDto(email, password)
        val tokenDto = AuthenticationSteps.userLogsIn(client!!, signInDto)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    fun refreshesToken(): TokenReadDto {
        client = client!!.mutate().defaultHeaders { it.setBearerAuth(token!!.refreshToken) }.build()
        val tokenDto = AuthenticationSteps.userRefreshesToken(client!!)
        saveObtainedToken(tokenDto)
        return tokenDto
    }

    open fun getsUserInfo(): UserReadDto = AccountSteps.userGetsAccountInfo(client!!)

    fun validate(tokenReadDto: TokenReadDto) {
        assertAll(
            { Assertions.assertThat(tokenReadDto.accessToken).isNotNull() },
            { Assertions.assertThat(tokenReadDto.accessTokenExpirationDate).isAfter(LocalDateTime.now(ZoneOffset.UTC)) },
            { Assertions.assertThat(tokenReadDto.refreshToken).isNotNull() },
            { Assertions.assertThat(tokenReadDto.refreshTokenExpirationDate).isAfter(LocalDateTime.now(ZoneOffset.UTC)) },
            { Assertions.assertThat(tokenReadDto.type).isEqualTo("Bearer") }
        )
    }

    open fun validate(userReadDto: UserReadDto) {
        assertAll(
            { Assertions.assertThat(userReadDto.id).isNotNull() },
            { Assertions.assertThat(userReadDto.firstName).isEqualTo(this.firstName) },
            { Assertions.assertThat(userReadDto.lastName).isEqualTo(this.lastName) },
            { Assertions.assertThat(userReadDto.email).isEqualTo(this.email) },
            { Assertions.assertThat(userReadDto.role).isEqualTo(this.role) },
        )
    }

    fun reset() {
        userId = null
        token = null
        client = null
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
        val PASSENGER_JOHN = PassengerActor("john.doe@mail.com", "secret", "John", "Doe")
        val PASSENGER_OLIVIA = PassengerActor("olivia.may@mail.com", "secret", "Olivia", "May")
        val PASSENGER_GREGORY = PassengerActor("gregory.trevino@mail.com", "secret", "Gregory", "Trevino")
        val PASSENGER_DONALD = PassengerActor("donald.soto@mail.com", "secret", "Donald", "Soto")
        val PASSENGER_FREDDIE = PassengerActor("freddie.stuart@mail.com", "secret", "Freddy", "Stuart")
        val PASSENGER_EMIL = PassengerActor("emil.briggs@mail.com", "secret", "Emil", "Briggs")

        // NON REGISTERED
        val NR_PASSENGER_MARK = PassengerActor("mark.twain@mail.com", "secret", "Mark", "Twain")

        val PASSENGERS = listOf(PASSENGER_JOHN, PASSENGER_OLIVIA, PASSENGER_GREGORY, PASSENGER_DONALD, PASSENGER_FREDDIE, PASSENGER_EMIL, NR_PASSENGER_MARK)
    }

    override fun getsUserInfo(): PassengerReadDto = AccountSteps.userGetsPassengerInfo(client!!)

    fun validate(userReadDto: PassengerReadDto) {
        super.validate(userReadDto)
        Assertions.assertThat(userReadDto.accountBalance).isEqualTo(0.0)
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
        val TICKET_INSPECTOR_FELIX = TicketInspectorActor("felix.carey@mail.com", "secret", "Felix", "Carey")
        val TICKET_INSPECTOR_STEPHANIE = TicketInspectorActor("stephanie.duran@mail.com", "secret", "Stephanie", "Duran")
        val TICKET_INSPECTOR_DORIS = TicketInspectorActor("doris.sandoval@mail.com", "secret", "Doris", "Sandoval")

        val TICKET_INSPECTORS = listOf(TICKET_INSPECTOR_FELIX, TICKET_INSPECTOR_STEPHANIE, TICKET_INSPECTOR_DORIS)
    }

}

class PlannerActor(
    email: String,
    password: String,
    firstName: String,
    lastName: String
) : UserActor(email, password, firstName, lastName) {

    override val role: Role = Role.PLANNER

    companion object {
        val PLANNER_MARCEL = PlannerActor("marcel.durham@mail.com", "secret", "Marcel", "Durham")
        val PLANNER_EMILY = PlannerActor("emily.wang@mail.com", "secret", "Emily", "Wang")

        val PLANNERS = listOf(PLANNER_MARCEL, PLANNER_EMILY)
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
        val MODERATOR_CARTER = ModeratorActor("carter.graves@mail.com", "secret", "Carter", "Graves")
        val MODERATOR_ANGELINA = ModeratorActor("angelina.shelton@mail.com", "secret", "Angelina", "Shelton")

        val MODERATORS = listOf(MODERATOR_CARTER, MODERATOR_ANGELINA)
    }

}
