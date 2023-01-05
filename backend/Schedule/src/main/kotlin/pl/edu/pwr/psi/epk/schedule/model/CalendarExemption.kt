package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.io.Serializable
import java.util.*


class CalendarExemptionId(var calendar: Calendar, var date: Date) : Serializable

enum class CalendarExemptionType {
    ADDITIONAL_RIDE, CANCELLED_RIDE
}

@Entity
@IdClass(CalendarExemptionId::class)
class CalendarExemption (
    @Id
    @ManyToOne
    @JoinColumn(name = "calendars_calendar", nullable = false)
    var calendar: Calendar,

    @Id
    var date: Date,

    @Enumerated(EnumType.ORDINAL)
    var type: CalendarExemptionType
)