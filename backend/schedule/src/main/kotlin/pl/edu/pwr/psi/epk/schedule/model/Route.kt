package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
class Route(
    @ManyToOne
    val line: Line,

    val name: String,

) {

    @Id
    @GeneratedValue
    val id: Long = 0

    @ManyToMany
    var stops: List<Stop> = mutableListOf()

    @OneToMany(mappedBy = "route")
    val routeServices: Set<RouteService> = mutableSetOf()

}
