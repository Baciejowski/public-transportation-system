package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.time.DayOfWeek
import java.time.LocalDate
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

    fun isServiceDay(firstStopDeparture: LocalDateTime): Boolean {
        for(calendarExemption in calendarExemptions)
            if(calendarExemption.startTime.isBefore(firstStopDeparture) && calendarExemption.endTime.isAfter(firstStopDeparture))
                return calendarExemption.type==CalendarExemptionType.ADDITIONAL_RIDE
        return serviceDays.contains(firstStopDeparture.dayOfWeek)
    }

}
