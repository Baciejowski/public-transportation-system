package pl.edu.pwr.psi.epk.schedule.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import pl.edu.pwr.psi.epk.schedule.model.*
import pl.edu.pwr.psi.epk.schedule.repository.*

@Configuration
class DataInitializer(
    private val lineRepository: LineRepository,
    private val routeRepository: RouteRepository,
    private val stopRepository: StopRepository,
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val stop121 = stopRepository.save(
            Stop("Dzierżoniów Staszica (121)", Coordinates(50.720208, 16.65726)))
        val stop69 = stopRepository.save(
            Stop("Dzierżoniów Batalionów Chłopskich (69)", Coordinates(50.719418, 16.649913)))
        val stop70 = stopRepository.save(
            Stop("Dzierżoniów młyn (70)", Coordinates(50.725123, 16.653868)))
        val stop72 = stopRepository.save(
            Stop("Dzierżoniów Piłsudskiego (72)", Coordinates(50.72992, 16.653388)))

        val lineA = lineRepository.save(
            Line("A"))

        var routeA01 = Route(lineA, "Dzierżoniów Staszica - Dzierżoniów Piłsudskiego")
        routeA01.stops = listOf(stop121, stop69, stop70, stop72)
        routeRepository.save(routeA01)

    }

}