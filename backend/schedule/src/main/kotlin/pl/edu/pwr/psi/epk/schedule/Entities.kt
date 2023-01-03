package pl.edu.pwr.psi.epk.schedule

import jakarta.persistence.*

@Embeddable
class Coordinates(
    var latitude: Double,
    var longitude: Double
)


@Entity
@Table(name = "Stops")
class Stop(
    @Column(length = 20)
    var name: String,
    @Embedded
    var coordinates: Coordinates,
    var onRequest: Boolean = false,
    @ManyToMany(mappedBy = "stops")
    var routes: Set<Route> = mutableSetOf(),
    @Id
    @GeneratedValue
    var id: Int? = null
)


@Entity
@Table(name = "Routes")
class Route(
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
    @Id
    @GeneratedValue
    var id: Int? = null
)


@Entity
@Table(name = "Lines")
class Line(
    @Id
    @Column(length = 3)
    var line: String,
    @Column(length = 30)
    var name: String,
    @Column(length = 7)
    var color: String?,
    @OneToMany(mappedBy = "line")
    var routes: Set<Route> = mutableSetOf()
)
