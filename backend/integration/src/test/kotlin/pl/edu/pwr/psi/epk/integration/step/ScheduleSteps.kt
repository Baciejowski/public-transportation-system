package pl.edu.pwr.psi.epk.integration.step

import org.springframework.test.web.reactive.server.WebTestClient
import pl.edu.pwr.psi.epk.integration.dto.schedule.*

class ScheduleSteps {

    companion object {
        fun getLines(webTestClient: WebTestClient): List<LineDto> =
            webTestClient.get()
                .uri("/schedule/schedule/lines")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(LineDto::class.java)
                .returnResult().responseBody!!

        fun getStops(webTestClient: WebTestClient): List<StopManifestDto> =
            webTestClient.get()
                .uri("/schedule/schedule/stops")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(StopManifestDto::class.java)
                .returnResult().responseBody!!

        fun getBuses(webTestClient: WebTestClient): List<BusDto> =
            webTestClient.get()
                .uri("/schedule/schedule/buses")
                .exchange()
                .expectStatus().isOk
                .expectBodyList(BusDto::class.java)
                .returnResult().responseBody!!

        fun getLineRoutes(webTestClient: WebTestClient, lineId: Long): List<RouteManifestDto> =
            tryGetLineRoutes(webTestClient, lineId)
                .expectStatus().isOk
                .expectBodyList(RouteManifestDto::class.java)
                .returnResult().responseBody!!

        fun tryGetLineRoutes(webTestClient: WebTestClient, lineId: Long): WebTestClient.ResponseSpec =
            webTestClient.get()
                .uri("/schedule/schedule/lines/{lineId}/routes", lineId)
                .exchange()

        fun getRoute(webTestClient: WebTestClient, routeId: Long): RouteDetailDto =
            tryGetRoute(webTestClient, routeId)
                .expectStatus().isOk
                .expectBody(RouteDetailDto::class.java)
                .returnResult().responseBody!!

        fun tryGetRoute(webTestClient: WebTestClient, routeId: Long): WebTestClient.ResponseSpec =
            webTestClient.get()
                .uri("/schedule/schedule/routes/{routeId}", routeId)
                .exchange()

        fun getStopLines(webTestClient: WebTestClient, stopId: Long): List<LineDto> =
            tryGetStopLines(webTestClient, stopId)
                .expectStatus().isOk
                .expectBodyList(LineDto::class.java)
                .returnResult().responseBody!!

        fun tryGetStopLines(webTestClient: WebTestClient, stopId: Long): WebTestClient.ResponseSpec =
            webTestClient.get()
                .uri("/schedule/schedule/stops/{stopId}/lines", stopId)
                .exchange()

        fun getStopDepartures(webTestClient: WebTestClient, stopId: Long, length: Int): List<StopDepartureDto> =
            tryGetStopDepartures(webTestClient, stopId, length)
                .expectStatus().isOk
                .expectBodyList(StopDepartureDto::class.java)
                .returnResult().responseBody!!

        fun tryGetStopDepartures(webTestClient: WebTestClient, stopId: Long, length: Int): WebTestClient.ResponseSpec =
            webTestClient.get()
                .uri("/schedule/schedule/stops/{stopId}/departures?length={length}", stopId, length)
                .exchange()
    }

}