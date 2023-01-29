package pl.edu.pwr.psi.epk.schedule.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import pl.edu.pwr.psi.epk.schedule.model.*
import pl.edu.pwr.psi.epk.schedule.repository.*
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataInitializer(
    private val lineRepository: LineRepository,
    private val routeRepository: RouteRepository,
    private val stopRepository: StopRepository,
    private val calendarRepository: CalendarRepository,
    private val routeServiceRepository: RouteServiceRepository,
    private val busRepository: BusRepository,
    private val rideRepository: RideRepository
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        var today = LocalDate.now().atStartOfDay()
        if(today.dayOfWeek.value>5) today = LocalDate.now().atStartOfDay().plusDays(8L-today.dayOfWeek.value)

        val now = LocalDateTime.now()

        val stop121 = stopRepository.save(
            Stop("Dzierżoniów Staszica (121)", Coordinates(50.720208, 16.65726)))
        val stop69 = stopRepository.save(
            Stop("Dzierżoniów Batalionów Chłopskich (69)", Coordinates(50.719418, 16.649913)))
        val stop70 = stopRepository.save(
            Stop("Dzierżoniów młyn (70)", Coordinates(50.725123, 16.653868)))
        val stop72 = stopRepository.save(
            Stop("Dzierżoniów Piłsudskiego (72)", Coordinates(50.72992, 16.653388)))
        val stop88 = stopRepository.save(
            Stop("Dzierżoniów Wrocławska (szkoła nr 4) (88)", Coordinates(50.736085, 16.658693))
        )
        val stop310 = stopRepository.save(
            Stop("Uciechów I (310)", Coordinates(50.753622, 16.681590))
        )

        val lineA = lineRepository.save(
            Line("A"))

        val routeA01 = Route(lineA, "Dzierżoniów Staszica - Dzierżoniów Piłsudskiego")
        routeA01.stops = listOf(stop121, stop69, stop70, stop72)
        routeRepository.save(routeA01)
        val routeA03 = Route(lineA, "Dzierżoniów Piłsudskiego - Uciechów I")
        routeA03.stops = listOf(stop72, stop88, stop310)
        routeRepository.save(routeA03)

        val calendar = calendarRepository.save(
            Calendar(
                mutableSetOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY),
                today)
        )

        val routeServicesA01 = routeServiceRepository.saveAll(
            (0L..15).map{
                val routeService = RouteService(routeA01, calendar)
                routeService.routeServiceStops = listOf(
                    RouteServiceStop(routeService, stop121, Duration.ofHours(5+it).plusMinutes(15)),
                    RouteServiceStop(routeService, stop69, Duration.ofHours(5+it).plusMinutes(17)),
                    RouteServiceStop(routeService, stop70, Duration.ofHours(5+it).plusMinutes(18)),
                    RouteServiceStop(routeService, stop72, Duration.ofHours(5+it).plusMinutes(20)),
                )
                routeService
            })

        val routeServicesA03 = routeServiceRepository.saveAll(
            (0L..15).map{
                val routeService = RouteService(routeA01, calendar)
                routeService.routeServiceStops = listOf(
                    RouteServiceStop(routeService, stop72, Duration.ofHours(5+it).plusMinutes(20)),
                    RouteServiceStop(routeService, stop88, Duration.ofHours(5+it).plusMinutes(23)),
                    RouteServiceStop(routeService, stop310, Duration.ofHours(5+it).plusMinutes(33))
                )
                routeService
            })

        val buses = busRepository.saveAll((1..21).map{Bus(300+it, true)})

        val rides = rideRepository.saveAll((routeServicesA01+routeServicesA03).map{routeService ->
            val ride = Ride(
                routeService,
                buses[0],
                today+routeService.routeServiceStops.minOf{it.plannedDepartureTime},
                today+routeService.routeServiceStops.maxOf{it.plannedDepartureTime}
            )
            if(ride.plannedStartTime.isBefore(now)){
                ride.startTime = ride.plannedStartTime
                ride.rideStops = ride.routeService.routeServiceStops.filter{(today+it.plannedDepartureTime).isBefore(now)}.map{RideStop(ride,it) }
                ride.rideStops.forEach{it.timeDeviation = Duration.ZERO}
            }
            if(ride.plannedEndTime.isBefore(now))
                ride.endTime = ride.plannedEndTime
            ride
        })

    }

}