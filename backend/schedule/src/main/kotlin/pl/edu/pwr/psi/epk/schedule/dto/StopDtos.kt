package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import pl.edu.pwr.psi.epk.schedule.model.Stop

class StopManifestDTO(
    val id: Long,
    val name: String,
    val coordinates: Coordinates,
    val onRequest: Boolean
) {
    companion object {
        fun fromStop(stop: Stop) = StopManifestDTO(stop.id, stop.name, stop.coordinates, stop.onRequest)
    }
}