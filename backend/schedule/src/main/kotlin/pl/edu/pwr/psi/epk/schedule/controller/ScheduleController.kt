package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import pl.edu.pwr.psi.epk.schedule.model.Line
import pl.edu.pwr.psi.epk.schedule.repository.BusRepository
import pl.edu.pwr.psi.epk.schedule.repository.LineRepository
import pl.edu.pwr.psi.epk.schedule.repository.RouteRepository
import pl.edu.pwr.psi.epk.schedule.repository.StopRepository
import java.time.LocalDate

data class RideReadDto(val serviceId: Int, val busSideNumber: Int, val date: LocalDate)

@RestController
@RequestMapping("/schedule")
class ScheduleController(
    val lineRepository: LineRepository,
    val routeRepository: RouteRepository,
    val stopRepository: StopRepository,
    val busRepository: BusRepository
) {
    @GetMapping("/lines")
    fun getLines() =
        ResponseEntity.ok(lineRepository.findAll().map{LineDTO.fromLine(it)})

    @GetMapping("/stops")
    fun getStops() =
        ResponseEntity.ok(stopRepository.findAll().map{StopManifestDTO.fromStop(it)})

    @GetMapping("/buses")
    fun getBuses() =
        ResponseEntity.ok(busRepository.findAll().map{BusDTO.fromBus(it)})

    @GetMapping("/lines/{id}/routes")
    fun getLineRoutes(@PathVariable id: Long): ResponseEntity<List<RouteManifestDTO>> {
        val line = lineRepository.findById(id)
        if(line.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(line.get().routes.map{RouteManifestDTO.fromRoute(it)})
    }

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

    @GetMapping
    fun getSchedule(lineId: String): ResponseEntity<*> = TODO()

    @GetMapping("/rides")
    fun getCurrentRide(sideNumber: String): ResponseEntity<RideReadDto> = TODO()

    @GetMapping("/location")
    fun getCurrentLocation(rideId: String): ResponseEntity<Coordinates> = TODO()

    @PatchMapping("/location/next")
    fun updateCurrentLocation(rideId: String): ResponseEntity<*> = TODO()

}