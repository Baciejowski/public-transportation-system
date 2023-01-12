package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.io.Serializable
import java.util.*


class HaltId(var stop: Stop, var service: Service):Serializable

@Entity
@Table(name = "Halts")
@IdClass(HaltId::class)
class Halt (
    @Id
    @ManyToOne
    @JoinColumn(name = "stops_id", nullable = false)
    var stop: Stop,

    @Id
    @ManyToOne
    @JoinColumn(name = "services_service_id", nullable = false)
    var service: Service,

    var departureTime: Date
)