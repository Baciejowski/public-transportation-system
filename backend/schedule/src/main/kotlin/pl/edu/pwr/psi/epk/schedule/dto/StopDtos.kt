package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import pl.edu.pwr.psi.epk.schedule.model.Stop
import java.time.Duration
import java.time.LocalDateTime

data class StopManifestDTO(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val onRequest: Boolean
) {
    companion object {
        fun fromStop(stop: Stop) = StopManifestDTO(stop.id, stop.name, stop.coordinates, stop.onRequest)
    }
}

data class StopDetailsDTO(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val onRequest: Boolean,
    val lines: List<LineManifestDTO>,
    val departures: List<StopDepartureDTO>
) {
    companion object {
        fun fromStopAndDepartures(stop: Stop, departures: List<StopDepartureDTO>) =
            StopDetailsDTO(
                stop.id,
                stop.name,
                stop.coordinates,
                stop.onRequest,
                stop.routes.map { it.line }.distinctBy { it.id }.map { LineManifestDTO.fromLine(it) },
                departures
            )
    }
}

data class StopDepartureDTO(
    val line: LineManifestDTO,
    val route: RouteManifestDTO,
    val departure: LocalDateTime,
    val deviation: Duration
)