package pl.edu.pwr.psi.epk

import jakarta.persistence.*

@Embeddable
class Polozenie(
    var szerokosc: Double,
    var dlugosc: Double
)
