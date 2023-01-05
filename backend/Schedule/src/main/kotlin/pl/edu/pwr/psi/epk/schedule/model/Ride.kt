package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.io.Serializable
import java.util.*

class RideId(var service: Service, var bus: Bus, var date: Date): Serializable

@Entity
@Table(name = "Rides")
@IdClass(RideId::class)
class Ride (
    @Id
    @ManyToOne
    @JoinColumn(name = "services_service_id")
    var service: Service,

    @Id
    @ManyToOne
    @JoinColumn(name = "buses_side_number")
    var bus: Bus,

    @Id
    var date: Date
)