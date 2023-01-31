package pl.edu.pwr.psi.epk.integration.dto.schedule

import java.time.Duration
import java.time.LocalDateTime

class StopManifestDto(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val onRequest: Boolean
)

class StopDepartureDto(
    val line: LineDto,
    val route: RouteManifestDto,
    val departure: LocalDateTime,
    val deviation: Duration
)

class Coordinates(
    val latitude: Double,
    val longitude: Double
)