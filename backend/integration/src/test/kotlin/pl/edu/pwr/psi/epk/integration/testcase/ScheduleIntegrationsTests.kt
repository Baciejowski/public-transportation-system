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
    fun `Passenger can get line routes`() {
        PASSENGER_JOHN.logsIn()
        val line = PASSENGER_JOHN.getsLines()[0]
        val lineRoutes = PASSENGER_JOHN.getsLineRoutes(line.id)
        assertThat(lineRoutes).hasSizeGreaterThanOrEqualTo(2)
    }

    @Test
    fun `Passenger gets informed if line route is not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetLineRoutes(Long.MIN_VALUE)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger can get a route`() {
        PASSENGER_JOHN.logsIn()
        val line = PASSENGER_JOHN.getsLines()[0]
        val lineRoute = PASSENGER_JOHN.getsLineRoutes(line.id)[0]
        val route = PASSENGER_JOHN.getsRoute(lineRoute.id)
        assertAll(
            { assertThat(route.id).isEqualTo(lineRoute.id) },
            { assertThat(route.name).isEqualTo(lineRoute.name) },
            { assertThat(route.stops).hasSizeGreaterThanOrEqualTo(2) }
        )
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
    fun `Passenger can get stop lines`() {
        PASSENGER_JOHN.logsIn()
        val stop = PASSENGER_JOHN.getsStops()[0]
        val stopLines = PASSENGER_JOHN.getsStopLines(stop.id)
        assertThat(stopLines).isNotEmpty
    }

    @Test
    fun `Passenger gets informed if stop lines are not found`() {
        PASSENGER_JOHN.logsIn()
        val response = PASSENGER_JOHN.triesToGetStopLines(Long.MIN_VALUE)
        response.expectStatus().isNotFound
    }

    @Test
    fun `Passenger can get stop departures`() {
        PASSENGER_JOHN.logsIn()
        val stop = PASSENGER_JOHN.getsStops()[0]
        val stopDepartures = PASSENGER_JOHN.getsStopDepartures(stop.id, 5)
        assertThat(stopDepartures).hasSizeLessThanOrEqualTo(5)
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