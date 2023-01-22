package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Bus (
    @Id
    val sideNumber: Int,
    val isLowFloor: Boolean
) {

    @OneToMany(mappedBy = "bus")
    val rides: Set<Ride> = mutableSetOf()

}