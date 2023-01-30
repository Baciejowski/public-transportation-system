package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import pl.edu.pwr.psi.epk.schedule.model.Line
import pl.edu.pwr.psi.epk.schedule.repository.BusRepository
import pl.edu.pwr.psi.epk.schedule.repository.LineRepository
import pl.edu.pwr.psi.epk.schedule.repository.RouteRepository
import pl.edu.pwr.psi.epk.schedule.repository.StopRepository
import pl.edu.pwr.psi.epk.schedule.service.ScheduleService
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class RideReadDto(val serviceId: Int, val busSideNumber: Int, val date: LocalDate)

@RestController
@RequestMapping("/schedule")
class ScheduleController(
    val lineRepository: LineRepository,
    val routeRepository: RouteRepository,
    val stopRepository: StopRepository,
    val busRepository: BusRepository,
    val scheduleService: ScheduleService
) {
    //Screen 4.6.2.1
    @GetMapping("/lines")
    fun getLines() =
        ResponseEntity.ok(lineRepository.findAll().map{LineDTO.fromLine(it)})

    //Screen 4.6.2.1
    @GetMapping("/stops")
    fun getStops() =
        ResponseEntity.ok(stopRepository.findAll().map{StopManifestDTO.fromStop(it)})

    @GetMapping("/buses")
    fun getBuses() =
        ResponseEntity.ok(busRepository.findAll().map{BusDTO.fromBus(it)})

    //Screen 4.6.2.2?
    @GetMapping("/lines/{id}/routes")
    fun getLineRoutes(@PathVariable id: Long): ResponseEntity<List<RouteManifestDTO>> {
        val line = lineRepository.findById(id)
        if(line.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(line.get().routes.map{RouteManifestDTO.fromRoute(it)})
    }

    //Screen 4.6.2.2
    @GetMapping("/routes/{id}")
    fun getRoute(@PathVariable id: Long): ResponseEntity<RouteDetailDTO> {
        val route = routeRepository.findById(id)
        if(route.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RouteDetailDTO.fromRoute(route.get()))
    }

    @GetMapping("/stops/{id}/lines")
    fun getStopLines(@PathVariable id: Long): ResponseEntity<List<LineDTO>> {
        val stop = stopRepository.findById(id)
        if(stop.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(stop.get().routes.map{it.line}.distinctBy{it.id}.map {LineDTO.fromLine(it)})
    }

    //Screens 4.6.2.3, 4.7.2.1
    @GetMapping("/stops/{id}/departures")
    fun getStopDepartures(@PathVariable id: Long, @RequestParam length: Optional<Int>): ResponseEntity<List<StopDepartureDTO>> {
        val stop = stopRepository.findById(id)
        if(stop.isEmpty)
            return ResponseEntity.notFound().build()
        if(length.isEmpty)
            return ResponseEntity.ok(scheduleService.getStopDepartures(id, LocalDateTime.now()))
        return ResponseEntity.ok(scheduleService.getStopDepartures(id, LocalDateTime.now(), length.get()))
    }

    @GetMapping
    fun getSchedule(lineId: String): ResponseEntity<*> = TODO()

    @GetMapping("/rides")
    fun getCurrentRide(sideNumber: String): ResponseEntity<RideReadDto> = TODO()

    @GetMapping("/location")
    fun getCurrentLocation(rideId: String): ResponseEntity<Coordinates> = TODO()

    @PatchMapping("/location/next")
    fun updateCurrentLocation(rideId: String): ResponseEntity<*> = TODO()

}