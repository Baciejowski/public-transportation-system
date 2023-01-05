package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "Buses")
class Bus (
    @Id
    var sideNumber: Int,

    var isLowFloor: Boolean,

    @OneToMany
    var rides: List<Ride> = listOf()
) {
    var lastPosition: Coordinates? = null
    var lastSeen: Date? = null
}