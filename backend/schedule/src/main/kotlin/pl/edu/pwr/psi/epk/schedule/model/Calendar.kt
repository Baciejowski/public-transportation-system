package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "Calendars")
class Calendar (
    @ElementCollection
    @OrderColumn(name = "pos")
    var days: List<Boolean>,

    var startDate: Date,

    var endDate: Date,

    @OneToMany
    var calendarExemptions: Set<CalendarExemption>,

    @OneToMany(mappedBy = "serviceId")
    var busFares: Set<BusFare> = setOf(),

    @Id
    @GeneratedValue
    var id: Int? = null
)