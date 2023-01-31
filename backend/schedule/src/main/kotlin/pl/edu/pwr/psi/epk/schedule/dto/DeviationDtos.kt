package pl.edu.pwr.psi.epk.schedule.dto

import java.time.Duration
import java.time.LocalDateTime

data class DeviationDTO(
    val deviation: Duration,
    val line: LineManifestDTO,
    val lastStop: StopManifestDTO,
    val departed: LocalDateTime,
    val nextStop: StopManifestDTO
)