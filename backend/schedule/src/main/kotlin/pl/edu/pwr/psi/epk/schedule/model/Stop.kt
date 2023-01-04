package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
@Table(name = "Stops")
class Stop(

    @Id
    @GeneratedValue
    var id: Int? = null,

    @Column(length = 20)
    var name: String,

    @Embedded
    var coordinates: Coordinates,

    var onRequest: Boolean = false,

    @ManyToMany(mappedBy = "stops")
    var routes: Set<Route> = mutableSetOf(),

)
