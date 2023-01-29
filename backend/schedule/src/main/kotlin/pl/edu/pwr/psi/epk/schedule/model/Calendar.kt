package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDateTime

@Entity
class Calendar (
    @ElementCollection
    val serviceDays: Set<DayOfWeek>,

    val startDate: LocalDateTime,

    val endDate: LocalDateTime? = null,

) {

    @Id
    @GeneratedValue
    val id: Long = 0

    @OneToMany(mappedBy = "calendar")
    val routeServices: Set<RouteService> = mutableSetOf()

    @OneToMany(mappedBy = "calendar")
    val calendarExemptions: Set<CalendarExemption> = mutableSetOf()

}
