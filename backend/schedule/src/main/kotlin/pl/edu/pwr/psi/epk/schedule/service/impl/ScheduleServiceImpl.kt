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

        val allDepartures = routeServiceStopRepository.findAllByStop_Id(stopId)
        println(allDepartures.size)

        val departuresOfTodaysRides = allDepartures
            .filter { routeServiceStop ->
                routeServiceStop.routeService.calendar.isServiceDay(
                    routeServiceStop.routeService.routeServiceStops.minOf {today+it.plannedDepartureTime }) }
            .filter { dateTime.isBefore(today+it.plannedDepartureTime) }
            .sortedBy { today+it.plannedDepartureTime }
            .map { routeServiceStop ->
                val route = routeServiceStop.routeService.route
                val rideStart = routeServiceStop.routeService.routeServiceStops.minOf {today+it.plannedDepartureTime }
                val ride = routeServiceStop.routeService.rides.firstOrNull{ it.plannedStartTime == rideStart }
                var delay = Duration.ZERO
                if(ride?.startTime != null) {
                    delay = ride.rideStops.last().timeDeviation
                }
                StopDepartureDTO(
                    LineManifestDTO.fromLine(route.line),
                    RouteManifestDTO.fromRoute(route),
                    today+routeServiceStop.plannedDepartureTime,
                    delay
                )
            }

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
                        routeServiceStop.routeService.routeServiceStops.minOf { tomorrow+it.plannedDepartureTime }) }
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
            .map { route -> Pair(route.rideStops.last(), route.rideStops.size) }
            .filter { it.first.timeDeviation.abs().toMinutes()>=1 }
            .sortedBy { it.first.timeDeviation.abs() }
            .map { (rideStop, nextIndex) ->
                val lastStop = rideStop.routeServiceStop.stop
                val nextStop = rideStop.routeServiceStop.routeService.routeServiceStops[nextIndex].stop
                val departed =
                    rideStop.ride.plannedStartTime.toLocalDate().atStartOfDay() +
                            rideStop.routeServiceStop.plannedDepartureTime +
                            rideStop.timeDeviation
                DeviationDTO(
                    rideStop.timeDeviation,
                    LineManifestDTO.fromLine(rideStop.ride.routeService.route.line),
                    StopManifestDTO.fromStop(lastStop),
                    departed,
                    StopManifestDTO.fromStop(nextStop),
                )
            }
}