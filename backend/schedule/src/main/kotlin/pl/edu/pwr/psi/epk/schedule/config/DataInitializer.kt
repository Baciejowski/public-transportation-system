package pl.edu.pwr.psi.epk.schedule.config

import jakarta.transaction.Transactional
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import pl.edu.pwr.psi.epk.schedule.model.*
import pl.edu.pwr.psi.epk.schedule.repository.*
import java.lang.Thread.sleep
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
    fun createStop(name: String, latitude: Double, longitude: Double) =
        stopRepository.save(Stop(name, Coordinates(latitude, longitude)))

    fun createLine(name: String) = lineRepository.save(Line(name))

    fun createCalendar(days: Set<DayOfWeek>, from: LocalDate = LocalDate.now()) =
        calendarRepository.save(Calendar(days, from.atStartOfDay()))

    fun createRoute(line: Line, name: String, stops: List<Stop>): Route {
        val route = Route(line, name)
        route.stops = stops
        routeRepository.save(route)
        route.stops.forEach { it.routes.add(route)}
        stopRepository.saveAll(route.stops)
        line.routes.add(route)
        lineRepository.save(line)
        return route
    }

    fun createRouteServiceWithStops(route: Route, calendar: Calendar, halts: Map<Stop,Duration>): RouteService {
        val routeService = routeServiceRepository.save(RouteService(route, calendar))
        val routeServiceStops =
            routeServiceStopRepository.saveAllAndFlush(halts.map { RouteServiceStop(routeService, it.key, it.value) })
        routeService.routeServiceStops = routeServiceStops
        return routeServiceRepository.saveAndFlush(routeService)
    }

    fun createRide(routeService: RouteService,  bus: Bus, date: LocalDate = LocalDate.now()): Ride {
        val now = LocalDateTime.now()
        routeService.routeServiceStops = routeServiceStopRepository.findAllByRouteService(routeService)
        routeService.rides = rideRepository.findAllByRouteService(routeService)
        println(routeService.routeServiceStops.size)
        var ride = Ride(
            routeService,
            bus,
            date.atStartOfDay() + routeService.routeServiceStops.minOf { it.plannedDepartureTime },
            date.atStartOfDay() + routeService.routeServiceStops.maxOf { it.plannedDepartureTime }
        )
        println("a")

        if (ride.plannedStartTime.isBefore(now)) {
            println("b")
            ride.startTime = ride.plannedStartTime
            println("c")
            val passedStops = ride.routeService.routeServiceStops.filter {
                (date.atStartOfDay() + it.plannedDepartureTime).isBefore(now)
            }
            println("d")
            ride.rideStops = passedStops.map { RideStop(ride, it) }
            println("e")
            ride.rideStops.forEach { it.timeDeviation = Duration.ZERO }
            println("f")
        }

        println("g")

        if (ride.plannedEndTime.isBefore(now))
            ride.endTime = ride.plannedEndTime
        else if (ride.plannedStartTime.isBefore(now))
            ride.rideStops.last().timeDeviation = Duration.ofMinutes(ride.id%6-2)

        println("h")
        ride = rideRepository.save(ride)
        rideStopRepository.saveAll(ride.rideStops)
        println("i")
        routeService.rides += ride
        println("j")
        routeServiceRepository.save(routeService)
        println("k")
        return ride
    }

    override fun run(args: ApplicationArguments?) {
        val stop121 = createStop("Staszica (121)", 50.720208, 16.65726)
        val stop69 = createStop("Batalionów Chłopskich (69)", 50.719418, 16.649913)
        val stop70 = createStop("Młyn (70)", 50.725123, 16.653868)
        val stop72 = createStop("Piłsudskiego (72)", 50.72992, 16.653388)
        val stop88 = createStop("Wrocławska (88)", 50.736085, 16.658693)
        val stop310 = createStop("Uciechów I (310)", 50.753622, 16.681590)

        val lineA = createLine("A")
        val lineB = createLine("B")

        val routeA01 = createRoute(lineA, "Staszica - Piłsudskiego", listOf(stop121, stop69, stop70, stop72))
        val routeA03 = createRoute(lineA, "Piłsudskiego - Uciechów I", listOf(stop72, stop88, stop310))

        val calendar = createCalendar(DayOfWeek.values().toSet())

        val routeA01Services = (0L..23).map {
            createRouteServiceWithStops(
                routeA01, calendar, mapOf(
                    stop121 to Duration.ofHours(it).plusMinutes(15),
                    stop69 to Duration.ofHours(it).plusMinutes(17),
                    stop70 to Duration.ofHours(it).plusMinutes(18),
                    stop72 to Duration.ofHours(it).plusMinutes(20)
                ))
        }

        val routeA03Services = (0L..23).map {
            createRouteServiceWithStops(
                routeA03, calendar, mapOf(
                    stop72 to Duration.ofHours(it).plusMinutes(20),
                    stop88 to Duration.ofHours(it).plusMinutes(23),
                    stop310 to Duration.ofHours(it).plusMinutes(33)
                ))
        }

        val buses = busRepository.saveAll((1..21).map{Bus(300+it, true)})

        val ridesToday =
            (routeA01Services+routeA03Services).map { createRide(it, buses[0]) }
        val ridesTomorrow =
            (routeA01Services+routeA03Services).map { createRide(it, buses[0], LocalDate.now().plusDays(1)) }
    }
}