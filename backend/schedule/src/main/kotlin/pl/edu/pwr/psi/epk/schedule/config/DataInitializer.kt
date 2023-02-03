package pl.edu.pwr.psi.epk.schedule.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import pl.edu.pwr.psi.epk.schedule.model.*
import pl.edu.pwr.psi.epk.schedule.model.Calendar
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
    fun createStop(name: String, latitude: Double, longitude: Double, onDemand: Boolean = false) =
        stopRepository.save(Stop(name, Coordinates(latitude, longitude), onDemand))

    fun createLine(name: String, color: String? = null) = lineRepository.save(Line(name, color))

    fun createCalendar(days: Set<DayOfWeek>, from: LocalDate = LocalDate.now()) =
        calendarRepository.save(Calendar(days, from.atStartOfDay()))

    fun createRoute(line: Line, name: String, stops: List<Stop>): Route {
        val route = routeRepository.save(Route(line, name))
        route.stops = stops
        route.stops.forEach { it.routes.add(route)}
        stopRepository.saveAll(route.stops)
        line.routes.add(route)
        lineRepository.save(line)
        return routeRepository.save(route)
    }

    fun createRouteServiceWithStops(route: Route, calendar: Calendar, halts: List<Pair<Stop, Duration>>): RouteService {
        val routeService = routeServiceRepository.save(RouteService(route, calendar))
        val routeServiceStops =
            routeServiceStopRepository.saveAllAndFlush(halts.map { RouteServiceStop(routeService, it.first, it.second) })
        routeService.routeServiceStops = routeServiceStops
        return routeServiceRepository.saveAndFlush(routeService)
    }

    fun createRide(routeService: RouteService,  bus: Bus, date: LocalDate = LocalDate.now()): Ride {
        val now = LocalDateTime.now()
        routeService.routeServiceStops = routeServiceStopRepository.findAllByRouteService(routeService)
        routeService.rides = rideRepository.findAllByRouteService(routeService)
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
            ride = rideRepository.save(ride)
            ride.rideStops = passedStops.map { RideStop(ride, it) }
            ride.rideStops.forEach { it.timeDeviation = Duration.ZERO }
        }

        if (ride.plannedEndTime.isBefore(now) || ride.rideStops.filterNotNull().size == routeService.routeServiceStops.filterNotNull().size)
            ride.endTime = ride.plannedEndTime
        else if (ride.plannedStartTime.isBefore(now)) {
            val timeDeviation = Duration.ofMinutes(2)
            ride.rideStops.last().timeDeviation = timeDeviation
            if(ride.rideStops.size==1)
                ride.startTime = ride.startTime?.plus(timeDeviation)
        }

        rideStopRepository.saveAll(ride.rideStops)
        ride = rideRepository.save(ride)
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
                routeA01, calendar, listOf(
                    Pair(stop121, Duration.ofHours(it).plusMinutes(15)),
                    Pair(stop69, Duration.ofHours(it).plusMinutes(17)),
                    Pair(stop70, Duration.ofHours(it).plusMinutes(18)),
                    Pair(stop72, Duration.ofHours(it).plusMinutes(20))
                ))
        }

        val routeA03Services = (0L..23).map {
            createRouteServiceWithStops(
                routeA03, calendar, listOf(
                    Pair(stop72, Duration.ofHours(it).plusMinutes(20)),
                    Pair(stop88, Duration.ofHours(it).plusMinutes(23)),
                    Pair(stop310, Duration.ofHours(it).plusMinutes(33))
                ))
        }

        val routeA05Services = (0L..23).map {
            createRouteServiceWithStops(
                routeA05, calendar, listOf(
                    Pair(stop310, Duration.ofHours(it).plusMinutes(37)),
                    Pair(stop89, Duration.ofHours(it).plusMinutes(47)),
                    Pair(stop101, Duration.ofHours(it).plusMinutes(48)),
                    Pair(stop72, Duration.ofHours(it).plusMinutes(50)),
                    Pair(stop70, Duration.ofHours(it).plusMinutes(52)),
                    Pair(stop69, Duration.ofHours(it).plusMinutes(53)),
                    Pair(stop121, Duration.ofHours(it).plusMinutes(55))
                )
            )
        }

        val route4501Services = (0L..22).map {hours -> (0..5).map {
            val minutes = it*10L
            createRouteServiceWithStops(
                route4501, calendar, listOf(
                    Pair(stop71, Duration.ofHours(hours).plusMinutes(minutes + 0)),
                    Pair(stop136, Duration.ofHours(hours).plusMinutes(minutes + 2)),
                    Pair(stop113, Duration.ofHours(hours).plusMinutes(minutes + 4)),
                    Pair(stop246, Duration.ofHours(hours).plusMinutes(minutes + 8)),
                    Pair(stop248, Duration.ofHours(hours).plusMinutes(minutes + 10)),
                    Pair(stop260, Duration.ofHours(hours).plusMinutes(minutes + 13)),
                    Pair(stop278, Duration.ofHours(hours).plusMinutes(minutes + 20)),
                    Pair(stop280, Duration.ofHours(hours).plusMinutes(minutes + 22)),
                    Pair(stop282, Duration.ofHours(hours).plusMinutes(minutes + 23))
                ))
        }}.flatten()

        val buses = busRepository.saveAll((1..21).map{Bus(300+it, true)})

        val ridesToday =
            (routeA01Services+routeA03Services+routeA05Services).map { createRide(it, buses[0]) } +
                    route4501Services.map { createRide(it, buses[1]) }
        val ridesTomorrow =
            (routeA01Services+routeA03Services+routeA05Services).map { createRide(it, buses[0], LocalDate.now().plusDays(1)) } +
                    route4501Services.map { createRide(it, buses[1], LocalDate.now().plusDays(1)) }
    }
}