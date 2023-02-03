package pl.edu.pwr.psi.epk.integration.testcase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor.Companion.PASSENGER_JOHN

class ScheduleIntegrationsTests: TestBase() {

    @Test
    fun `Passenger can get all lines`() {
        PASSENGER_JOHN.logsIn()
        val lines = PASSENGER_JOHN.getsLines()
        assertThat(lines).hasSizeGreaterThanOrEqualTo(1)
    }

    @Test
    fun `Passenger gets informed if line route is not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetLineRoutes(Long.MIN_VALUE)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger gets informed if route is not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetRoute(Long.MIN_VALUE)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger can get all stops`() {
        PASSENGER_JOHN.logsIn()
        val stops = PASSENGER_JOHN.getsStops()
        assertThat(stops).hasSizeGreaterThanOrEqualTo(2)
    }

    @Test
    fun `Passenger gets informed if stop lines are not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetStopLines(Long.MIN_VALUE)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger gets informed if stop departures are not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetStopDepartures(Long.MIN_VALUE, 5)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger can get all buses`() {
        PASSENGER_JOHN.logsIn()
        val buses = PASSENGER_JOHN.getsBuses()
        assertThat(buses).isNotEmpty
    }
}