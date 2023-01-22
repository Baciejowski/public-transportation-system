package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Embeddable

@Embeddable
class Coordinates(
    val latitude: Double,
    val longitude: Double
)
