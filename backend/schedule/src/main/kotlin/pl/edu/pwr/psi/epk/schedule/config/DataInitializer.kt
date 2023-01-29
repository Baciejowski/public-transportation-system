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
    private val rideRepository: RideRepository,
    private val routeServiceStopRepository: RouteServiceStopRepository,
    private val rideStopRepository: RideStopRepository
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        var today = LocalDate.now().atStartOfDay()
        if(today.dayOfWeek.value>5) today = LocalDate.now().atStartOfDay().plusDays(8L-today.dayOfWeek.value)

        val now = LocalDateTime.now()

        val stop121 = stopRepository.save(
            Stop("Staszica (121)", Coordinates(50.720208, 16.65726)))
        val stop69 = stopRepository.save(
            Stop("Batalionów Chłopskich (69)", Coordinates(50.719418, 16.649913)))
        val stop70 = stopRepository.save(
            Stop("Młyn (70)", Coordinates(50.725123, 16.653868)))
        val stop72 = stopRepository.save(
            Stop("Piłsudskiego (72)", Coordinates(50.72992, 16.653388)))
        val stop88 = stopRepository.save(
            Stop("Wrocławska (88)", Coordinates(50.736085, 16.658693))
        )
        val stop310 = stopRepository.save(
            Stop("Uciechów I (310)", Coordinates(50.753622, 16.681590))
        )

        val lineA = lineRepository.save(
            Line("A"))

        val routeA01 = Route(lineA, "Staszica - Piłsudskiego")
        routeA01.stops = listOf(stop121, stop69, stop70, stop72)
        routeRepository.save(routeA01)
        val routeA03 = Route(lineA, "Piłsudskiego - Uciechów I")
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

        val routeServicesA01 = routeServiceRepository.saveAll((0L..15).map{ RouteService(routeA01, calendar) })
        for(i in 0..15){
            val routeService = routeServicesA01[i]
            routeServiceStopRepository.saveAll(listOf(
                RouteServiceStop(routeService, stop121, Duration.ofHours(5L+i).plusMinutes(15)),
                RouteServiceStop(routeService, stop69, Duration.ofHours(5L+i).plusMinutes(17)),
                RouteServiceStop(routeService, stop70, Duration.ofHours(5L+i).plusMinutes(18)),
                RouteServiceStop(routeService, stop72, Duration.ofHours(5L+i).plusMinutes(20))))
        }

        val routeServicesA03 = routeServiceRepository.saveAll((0L..15).map{RouteService(routeA03, calendar)})
        for(i in 0..15){
            val routeService = routeServicesA03[i]
            routeServiceStopRepository.saveAll(listOf(
                RouteServiceStop(routeService, stop72, Duration.ofHours(5L+i).plusMinutes(20)),
                RouteServiceStop(routeService, stop88, Duration.ofHours(5L+i).plusMinutes(23)),
                RouteServiceStop(routeService, stop310, Duration.ofHours(5L+i).plusMinutes(33))))
        }

        val buses = busRepository.saveAll((1..21).map{Bus(300+it, true)})

        //to fix
//        val rides = rideRepository.saveAll((routeServicesA01+routeServicesA03).map{routeService ->
//            val ride = Ride(
//                routeService,
//                buses[0],
//                today+routeService.routeServiceStops.minOf{it.plannedDepartureTime},
//                today+routeService.routeServiceStops.maxOf{it.plannedDepartureTime}
//            )
//            if(ride.plannedStartTime.isBefore(now)){
//                ride.startTime = ride.plannedStartTime
//                ride.rideStops = ride.routeService.routeServiceStops.filter{(today+it.plannedDepartureTime).isBefore(now)}.map{RideStop(ride,it) }
//                ride.rideStops.forEach{it.timeDeviation = Duration.ZERO}
//                rideStopRepository.saveAll(ride.rideStops)
//            }
//            if(ride.plannedEndTime.isBefore(now))
//                ride.endTime = ride.plannedEndTime
//            ride
//        })



    }

}