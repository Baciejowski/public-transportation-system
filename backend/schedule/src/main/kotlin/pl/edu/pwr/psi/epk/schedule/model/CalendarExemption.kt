package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*


enum class CalendarExemptionType {
    ADDITIONAL_RIDE, CANCELLED_RIDE
}

@Entity
class CalendarExemption (

    @ManyToOne
    val calendar: Calendar,

    @Enumerated(EnumType.STRING)
    val type: CalendarExemptionType,

    val startTime: LocalDateTime,

    val endTime: LocalDateTime,

    var description: String? = null,

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}