package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.NR_PASSENGER_MARK

class AuthenticationIntegrationTests : TestBase() {

    @Test
    fun `Passenger can register`() {
        NR_PASSENGER_MARK.client = webTestClient
        val marksToken = NR_PASSENGER_MARK.registers()
        NR_PASSENGER_MARK.validateTokenEquality(marksToken)
    }

    @Test
    fun `User can log in`() {
        PASSENGER_JOHN.client = webTestClient
        val johnsToken = PASSENGER_JOHN.logsIn()
        PASSENGER_JOHN.validateTokenEquality(johnsToken)
    }

    @Test
    fun `User can refresh their token`() {
        PASSENGER_JOHN.client = webTestClient
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

}