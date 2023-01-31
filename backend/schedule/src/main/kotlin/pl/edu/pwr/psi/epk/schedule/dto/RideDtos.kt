package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Ride
import java.time.LocalDateTime

data class RideDTO(
    val lineDTO: LineDTO,
    val route: RouteManifestDTO,
    val busSideNumber: Int,
    val plannedStartTime: LocalDateTime,
    val plannedEndTime: LocalDateTime,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?
) {
    companion object{
        fun fromRide(ride: Ride) = RideDTO(
            LineDTO.fromLine(ride.routeService.route.line),
            RouteManifestDTO.fromRoute(ride.routeService.route),
            ride.bus.sideNumber,
            ride.plannedStartTime,
            ride.plannedEndTime,
            ride.startTime,
            ride.endTime
        )
    }
}