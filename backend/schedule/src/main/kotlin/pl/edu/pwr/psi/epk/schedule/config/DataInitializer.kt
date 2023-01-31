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
    fun createStop(name: String, latitude: Double, longitude: Double, onDemand: Boolean = false) =
        stopRepository.save(Stop(name, Coordinates(latitude, longitude), onDemand))

    fun createLine(name: String, color: String? = null) = lineRepository.save(Line(name, color))

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

        if (ride.plannedStartTime.isBefore(now)) {
            ride.startTime = ride.plannedStartTime
            val passedStops = ride.routeService.routeServiceStops.filter {
                (date.atStartOfDay() + it.plannedDepartureTime).isBefore(now)
            }
            ride.rideStops = passedStops.map { RideStop(ride, it) }
            ride.rideStops.forEach { it.timeDeviation = Duration.ZERO }
        }

        if (ride.plannedEndTime.isBefore(now))
            ride.endTime = ride.plannedEndTime
        else if (ride.plannedStartTime.isBefore(now))
            ride.rideStops.last().timeDeviation = Duration.ofMinutes(ride.id%6-2)

        ride = rideRepository.save(ride)
        rideStopRepository.saveAll(ride.rideStops)
        routeService.rides += ride
        routeServiceRepository.save(routeService)
        return ride
    }

    override fun run(args: ApplicationArguments?) {
        val stop121 = createStop("Staszica (121)", 50.720208, 16.65726)
        val stop69 = createStop("Batalionów Chłopskich (69)", 50.719418, 16.649913)
        val stop70 = createStop("Młyn (70)", 50.725123, 16.653868)
        val stop72 = createStop("Piłsudskiego (72)", 50.72992, 16.653388)
        val stop88 = createStop("Wrocławska (88)", 50.736085, 16.658693)
        val stop89 = createStop("Wrocławska (89)", 50.7365, 16.658848)
        val stop101 = createStop("Bielawska Stadion (101)", 50.73311, 16.656781)
        val stop310 = createStop("Uciechów I (310)", 50.753622, 16.681590)
        val stop71 = createStop("Piłsudskiego (71)", 0.0, 0.0)
        val stop136 = createStop("Ząbkowicka (136)", 0.0, 0.0)
        val stop113 = createStop("Ogródki działkowe (113, NŻ)", 0.0,0.0, true)
        val stop246 = createStop("Dobrocin I (246)", 0.0, 0.0)
        val stop248 = createStop("Dobrocin Sklep (248)", 0.0, 0.0)
        val stop260 = createStop("Byszów (260)", 0.0, 0.0)
        val stop278 = createStop("Niemcza Os. Podmiejskie (278)", 0.0, 0.0)
        val stop280 = createStop("Niemcza dworzec PKP (280)", 0.0, 0.0)
        val stop282 = createStop("Niemcza Remiza (284)", 0.0, 0.0)

        val lineA = createLine("A", "#00FF00")
        val line45 = createLine("45")

        val routeA01 = createRoute(lineA, "Staszica - Piłsudskiego", listOf(stop121, stop69, stop70, stop72))
        val routeA03 = createRoute(lineA, "Piłsudskiego - Uciechów I", listOf(stop72, stop88, stop310))
        val routeA05 = createRoute(lineA, "Uciechów I - Staszica", listOf(stop310, stop89, stop101, stop72, stop70, stop69, stop121))

        val route4501 =
            createRoute(
                line45,
                "Piłsudskiego - Niemcza Remiza",
                listOf(stop71, stop136, stop113, stop246, stop248, stop260, stop278, stop280, stop282)
            )

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

        val routeA05Services = (0L..23).map {
            createRouteServiceWithStops(
                routeA05, calendar, mapOf(
                    stop310 to Duration.ofHours(it).plusMinutes(37),
                    stop89 to Duration.ofHours(it).plusMinutes(47),
                    stop101 to Duration.ofHours(it).plusMinutes(48),
                    stop72 to Duration.ofHours(it).plusMinutes(50),
                    stop70 to Duration.ofHours(it).plusMinutes(52),
                    stop69 to Duration.ofHours(it).plusMinutes(53),
                    stop121 to Duration.ofHours(it).plusMinutes(55)
                ))
        }

        val route4501Services = (0L..22).map {
            createRouteServiceWithStops(
                route4501, calendar, mapOf(
                    stop71 to Duration.ofHours(it).plusMinutes(0),
                    stop136 to Duration.ofHours(it).plusMinutes(2),
                    stop113 to Duration.ofHours(it).plusMinutes(4),
                    stop246 to Duration.ofHours(it).plusMinutes(8),
                    stop248 to Duration.ofHours(it).plusMinutes(10),
                    stop260 to Duration.ofHours(it).plusMinutes(13),
                    stop278 to Duration.ofHours(it).plusMinutes(20),
                    stop280 to Duration.ofHours(it).plusMinutes(22),
                    stop282 to Duration.ofHours(it).plusMinutes(23)
                ))
        }

        val buses = busRepository.saveAll((1..21).map{Bus(300+it, true)})

        val ridesToday =
            (routeA01Services+routeA03Services+routeA05Services).map { createRide(it, buses[0]) } +
                    route4501Services.map { createRide(it, buses[1]) }
        val ridesTomorrow =
            (routeA01Services+routeA03Services+routeA05Services).map { createRide(it, buses[0], LocalDate.now().plusDays(1)) } +
                    route4501Services.map { createRide(it, buses[1], LocalDate.now().plusDays(1)) }
    }
}