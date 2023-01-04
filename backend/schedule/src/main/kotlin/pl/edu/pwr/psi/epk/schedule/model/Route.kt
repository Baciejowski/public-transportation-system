package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
@Table(name = "Routes")
class Route(

    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(length = 30)
    var name: String,

    @ManyToOne
    @JoinColumn(name = "lines_line", nullable = false)
    var line: Line,

    @ManyToMany
    @JoinTable(
        name = "Stops_Routes",
        joinColumns = [JoinColumn(name = "routes_id")],
        inverseJoinColumns = [JoinColumn(name = "stops_id")]
    )
    var stops: Set<Stop> = mutableSetOf(),

)
