package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
class Stop(
    val name: String,

    @Embedded
    val coordinates: Coordinates,

    val onRequest: Boolean = false,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}
