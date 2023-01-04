package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Embeddable

@Embeddable
class Coordinates(var latitude: Double, var longitude: Double)
