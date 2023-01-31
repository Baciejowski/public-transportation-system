package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class RouteService(
    @ManyToOne
    val route: Route,

    @ManyToOne
    val calendar: Calendar,

    val isLowFloor: Boolean = true

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "routeService")
    var routeServiceStops: List<RouteServiceStop> = mutableListOf()

    @OneToMany(mappedBy = "routeService")
    var rides: List<Ride> = mutableListOf()

}