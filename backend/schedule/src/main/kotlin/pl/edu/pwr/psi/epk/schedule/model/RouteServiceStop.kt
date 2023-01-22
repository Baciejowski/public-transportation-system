package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.time.Duration

@Entity
class RouteServiceStop (

    @ManyToOne
    val routeService: RouteService,

    @ManyToOne
    val stop: Stop,

    val plannedDepartureTime: Duration,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}