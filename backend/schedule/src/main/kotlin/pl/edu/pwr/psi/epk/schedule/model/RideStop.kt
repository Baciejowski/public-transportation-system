package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
class RideStop(

    @ManyToOne
    val ride: Ride,

    @ManyToOne
    val routeServiceStop: RouteServiceStop

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    val timeDeviation: Duration = run {
        val plannedDepartureDay = LocalDateTime.from(LocalDate.from(ride.plannedStartTime))
        val plannedDepartureDateTime = plannedDepartureDay.plus(routeServiceStop.plannedDepartureTime)
        Duration.between(plannedDepartureDateTime, LocalDateTime.now())
    }

}