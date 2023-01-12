package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*


// kurs
@Entity
@Table(name = "Services")
class BusFare (
    @ManyToOne
    @JoinColumn(name = "routes_id", nullable = false)
    var route: Route,

    var isLowFloor: Boolean,

    @ManyToOne
    @JoinColumn(name = "calendars_id", nullable = false)
    var calendar: Calendar,

    @OneToMany
    @OrderColumn(name = "no")
    var halts: List<Halt> = listOf(),

    @OneToMany
    var rides: Set<Ride> = setOf(),

    @Id
    @GeneratedValue
    var serviceId: Int? = null
)