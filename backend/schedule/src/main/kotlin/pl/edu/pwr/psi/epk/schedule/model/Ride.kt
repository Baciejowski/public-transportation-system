package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class Ride (

    @ManyToOne
    val routeService: RouteService,

    @ManyToOne
    val bus: Bus,

    val plannedStartTime: LocalDateTime,

    val plannedEndTime: LocalDateTime

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "ride")
    var rideStops: List<RideStop> = mutableListOf()

    var startTime: LocalDateTime? = null

    var endTime: LocalDateTime? = null

}