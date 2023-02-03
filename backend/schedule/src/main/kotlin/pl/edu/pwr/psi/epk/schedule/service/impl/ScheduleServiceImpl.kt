package pl.edu.pwr.psi.epk.schedule.service.impl

import org.springframework.stereotype.Service
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.repository.RideRepository
import pl.edu.pwr.psi.epk.schedule.repository.RouteServiceStopRepository
import pl.edu.pwr.psi.epk.schedule.service.ScheduleService
import java.time.Duration
import java.time.LocalDateTime

@Service
class ScheduleServiceImpl(
    private val routeServiceStopRepository: RouteServiceStopRepository,
    private val rideRepository: RideRepository
): ScheduleService {
    override fun getStopDepartures(
        stopId: Long,
        dateTime: LocalDateTime,
        numberOfDepartures: Int
    ): List<StopDepartureDTO> {
        val today = dateTime.toLocalDate().atStartOfDay()
        val tomorrow = today.plusDays(1)

        val allDepartures =
            routeServiceStopRepository
                .findAllByStop_Id(stopId)
                .filter { it.routeService.routeServiceStops.filterNotNull().last() != it }

        val departuresOfTodaysRides = allDepartures
            .filter { routeServiceStop ->
                routeServiceStop.routeService.calendar.isServiceDay(
                    routeServiceStop.routeService.routeServiceStops.filterNotNull().minOf {today+it.plannedDepartureTime }) }
            .sortedBy { today+it.plannedDepartureTime }
            .map { routeServiceStop ->
                val route = routeServiceStop.routeService.route
                val rideStart = routeServiceStop.routeService.routeServiceStops.filterNotNull().minOf {today+it.plannedDepartureTime }
                val ride = routeServiceStop.routeService.rides.firstOrNull{ it.plannedStartTime == rideStart }
                var delay = Duration.ZERO
                if(ride?.startTime != null) {
                    delay = ride.rideStops.filterNotNull().last().timeDeviation
                }
                StopDepartureDTO(
                    LineManifestDTO.fromLine(route.line),
                    RouteManifestDTO.fromRoute(route),
                    today+routeServiceStop.plannedDepartureTime,
                    delay
                )
            }
            // v uncomment in order to simulate bus riding
            .filter { dateTime.isBefore(it.departure+it.deviation) }

        val departuresToday = departuresOfTodaysRides
            .filter { it.departure.toLocalDate() == today.toLocalDate() }

        if(departuresToday.size>=numberOfDepartures) {
            return departuresToday.take(numberOfDepartures)
        }
        else {
            val departuresTomorrow = routeServiceStopRepository
                .findAllByStop_Id(stopId)
                .filter { routeServiceStop ->
                    routeServiceStop.routeService.calendar.isServiceDay(
                        routeServiceStop.routeService.routeServiceStops.filterNotNull().minOf { tomorrow+it.plannedDepartureTime }) }
                .filter { it.plannedDepartureTime.toDaysPart()==0L }
                .map { routeServiceStop ->
                    val route = routeServiceStop.routeService.route
                    StopDepartureDTO(
                        LineManifestDTO.fromLine(route.line),
                        RouteManifestDTO.fromRoute(route),
                        tomorrow+routeServiceStop.plannedDepartureTime,
                        Duration.ZERO
                    )
                }
            return (departuresOfTodaysRides+departuresTomorrow).sortedBy { it.departure }.take(numberOfDepartures)
        }
    }

    override fun getDeviations(): List<DeviationDTO> =
        rideRepository
            .findAllByStartTimeIsNotNullAndEndTimeIsNull()
            .map { route -> Pair(route.rideStops.filterNotNull().last(), route.rideStops.filterNotNull().size) }
            .filter { it.first.timeDeviation.abs().toMinutes()>=1 }
            .sortedBy { it.first.timeDeviation.abs() }
            .map { (lastRideStop, nextIndex) ->
                val lastStop = lastRideStop.routeServiceStop.stop
                val nextStop = lastRideStop.routeServiceStop.routeService.routeServiceStops.filterNotNull()[nextIndex].stop
                val departed =
                    lastRideStop.ride.plannedStartTime.toLocalDate().atStartOfDay() +
                            lastRideStop.routeServiceStop.plannedDepartureTime +
                            lastRideStop.timeDeviation
                DeviationDTO(
                    lastRideStop.timeDeviation,
                    LineManifestDTO.fromLine(lastRideStop.ride.routeService.route.line),
                    StopManifestDTO.fromStop(lastStop),
                    departed,
                    StopManifestDTO.fromStop(nextStop),
                )
            }
}