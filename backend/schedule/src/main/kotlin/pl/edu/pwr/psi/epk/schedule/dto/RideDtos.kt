package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Ride
import java.time.LocalDateTime

data class RideDTO(
    val id: Long,
    val lineManifestDTO: LineManifestDTO,
    val route: RouteManifestDTO,
    val busSideNumber: Int,
    val plannedStartTime: LocalDateTime,
    val plannedEndTime: LocalDateTime,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val lastStop: StopManifestDTO?,
    val lastStopNo: Int?,
    val nextStop: StopManifestDTO?
) {
    companion object{
        fun fromRide(ride: Ride) = RideDTO(
            ride.id,
            LineManifestDTO.fromLine(ride.routeService.route.line),
            RouteManifestDTO.fromRoute(ride.routeService.route),
            ride.bus.sideNumber,
            ride.plannedStartTime,
            ride.plannedEndTime,
            ride.startTime,
            ride.endTime,
            if (ride.rideStops.filterNotNull().isEmpty()) null else StopManifestDTO.fromStop(ride.rideStops.filterNotNull().last().routeServiceStop.stop),
            if (ride.rideStops.filterNotNull().isEmpty()) null else ride.rideStops.filterNotNull().size,
            if (ride.rideStops.filterNotNull().size == ride.routeService.routeServiceStops.filterNotNull().size)
                null
            else
                StopManifestDTO.fromStop(ride.routeService.routeServiceStops.filterNotNull()[ride.rideStops.filterNotNull().size].stop)
        )
    }
}

data class RideStopDTO(
    val stopNo: Int
)