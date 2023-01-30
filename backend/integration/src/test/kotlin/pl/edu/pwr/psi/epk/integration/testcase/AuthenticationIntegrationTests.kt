package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.http.HttpStatus
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.NR_PASSENGER_MARK

class AuthenticationIntegrationTests : TestBase() {

    @Test
    fun `Passenger can register`() {
        val marksToken = NR_PASSENGER_MARK.registers()
        NR_PASSENGER_MARK.validateTokenEquality(marksToken)
    }

    @Test
    fun `User can log in`() {
        val johnsToken = PASSENGER_JOHN.logsIn()
        PASSENGER_JOHN.validateTokenEquality(johnsToken)
    }

    @Test
    fun `User can refresh their token`() {
        val oldToken = PASSENGER_JOHN.logsIn()
        Thread.sleep(1000)
        val newToken = PASSENGER_JOHN.refreshesToken()
        assertAll(
            { assertThat(oldToken.accessToken).isNotEqualTo(newToken.accessToken) },
            { assertThat(oldToken.refreshToken).isNotEqualTo(newToken.refreshToken) },
            { assertThat(oldToken.accessTokenExpirationDate).isBefore(newToken.accessTokenExpirationDate) },
            { assertThat(oldToken.refreshTokenExpirationDate).isBefore(newToken.refreshTokenExpirationDate) },
        )
    }

    @Test
    fun `Passenger can't register using existing e-mail address`() {
        val errorResponse = PASSENGER_JOHN.registersExpectingError()
        assertAll(
            { assertThat(errorResponse.status).isEqualTo(HttpStatus.BAD_REQUEST) },
            { assertThat(errorResponse.message).contains("email", "already", "exists") },
        )
    }

    @Test
    fun `Passenger gets informed when his email is not registered`() {
        PASSENGER_JOHN.email = "non-existing@mail.com"
        val errorResponse = PASSENGER_JOHN.logsInExpectingError()
        assertAll(
            { assertThat(errorResponse.status).isEqualTo(HttpStatus.BAD_REQUEST) },
            { assertThat(errorResponse.message).contains("not", "find", "user", "email", PASSENGER_JOHN.email) },
        )
    }

    @Test
    fun `Passenger gets informed when providing invalid login credentials`() {
        PASSENGER_JOHN.password = "wrong-password"
        val errorResponse = PASSENGER_JOHN.logsInExpectingError()
        assertAll(
            { assertThat(errorResponse.status).isEqualTo(HttpStatus.BAD_REQUEST) },
            { assertThat(errorResponse.message).contains("not", "authenticate", "user", "email", PASSENGER_JOHN.email) },
        )
    }

}