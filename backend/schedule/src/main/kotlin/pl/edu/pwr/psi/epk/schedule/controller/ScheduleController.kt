package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.infrastructure.RequiredRole
import pl.edu.pwr.psi.epk.schedule.infrastructure.Role
import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import pl.edu.pwr.psi.epk.schedule.model.Line
import pl.edu.pwr.psi.epk.schedule.repository.*
import pl.edu.pwr.psi.epk.schedule.service.ScheduleService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("/schedule")
class ScheduleController(
    val lineRepository: LineRepository,
    val routeRepository: RouteRepository,
    val stopRepository: StopRepository,
    val busRepository: BusRepository,
    val scheduleService: ScheduleService,
    val rideRepository: RideRepository
) {
    //Screen 4.6.2.1
    @GetMapping("/lines")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getLines() =
        ResponseEntity.ok(lineRepository.findAll().map{LineDTO.fromLine(it)})

    //Screen 4.6.2.1
    @GetMapping("/stops")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getStops() =
        ResponseEntity.ok(stopRepository.findAll().map{StopManifestDTO.fromStop(it)})

    @GetMapping("/buses")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getBuses() =
        ResponseEntity.ok(busRepository.findAll().map{BusDTO.fromBus(it)})

    //Screen 4.6.2.2?
    @GetMapping("/lines/{id}/routes")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getLineRoutes(@PathVariable id: Long): ResponseEntity<List<RouteManifestDTO>> {
        val line = lineRepository.findById(id)
        if(line.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(line.get().routes.map{RouteManifestDTO.fromRoute(it)})
    }

    //Screen 4.6.2.2
    @GetMapping("/routes/{id}")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getRoute(@PathVariable id: Long): ResponseEntity<RouteDetailDTO> {
        val route = routeRepository.findById(id)
        if(route.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RouteDetailDTO.fromRoute(route.get()))
    }

    @GetMapping("/stops/{id}/lines")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getStopLines(@PathVariable id: Long): ResponseEntity<List<LineDTO>> {
        val stop = stopRepository.findById(id)
        if(stop.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(stop.get().routes.map{it.line}.distinctBy{it.id}.map {LineDTO.fromLine(it)})
    }

    //Screens 4.6.2.3, 4.7.2.1
    @GetMapping("/stops/{id}/departures")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getStopDepartures(@PathVariable id: Long, @RequestParam length: Optional<Int>): ResponseEntity<List<StopDepartureDTO>> {
        val stop = stopRepository.findById(id)
        if(stop.isEmpty)
            return ResponseEntity.notFound().build()
        if(length.isEmpty)
            return ResponseEntity.ok(scheduleService.getStopDepartures(id, LocalDateTime.now()))
        return ResponseEntity.ok(scheduleService.getStopDepartures(id, LocalDateTime.now(), length.get()))
    }

    //Screen 4.8.2.1
    @GetMapping("/deviations")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getDeviations() =
        ResponseEntity.ok(scheduleService.getDeviations())


    @GetMapping("/rides/{id}")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getRideStatus(@PathVariable id: Long) : ResponseEntity<RideDTO> {
        val ride = rideRepository.findById(id)
        if(ride.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RideDTO.fromRide(ride.get()))
    }
}