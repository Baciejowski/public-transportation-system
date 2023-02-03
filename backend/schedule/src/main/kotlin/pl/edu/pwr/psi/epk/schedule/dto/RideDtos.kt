package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Ride
import java.time.LocalDateTime

data class RideDTO(
    val lineManifestDTO: LineManifestDTO,
    val route: RouteManifestDTO,
    val busSideNumber: Int,
    val plannedStartTime: LocalDateTime,
    val plannedEndTime: LocalDateTime,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val lastStop: StopManifestDTO?,
    val lastStopNo: Int?,
    val nextStopNo: StopManifestDTO?
) {
    companion object{
        fun fromRide(ride: Ride) = RideDTO(
            LineManifestDTO.fromLine(ride.routeService.route.line),
            RouteManifestDTO.fromRoute(ride.routeService.route),
            ride.bus.sideNumber,
            ride.plannedStartTime,
            ride.plannedEndTime,
            ride.startTime,
            ride.endTime,
            if(ride.rideStops.isEmpty()) null else StopManifestDTO.fromStop(ride.rideStops.last().routeServiceStop.stop),
            if(ride.rideStops.isEmpty()) null else ride.rideStops.size,
            if(ride.rideStops.isEmpty() || ride.rideStops.size == ride.routeService.routeServiceStops.size)
                null
            else
                StopManifestDTO.fromStop(ride.routeService.routeServiceStops[ride.rideStops.size].stop)
        )
    }
}

data class RideStopDTO(
    val stopNo: Int
)