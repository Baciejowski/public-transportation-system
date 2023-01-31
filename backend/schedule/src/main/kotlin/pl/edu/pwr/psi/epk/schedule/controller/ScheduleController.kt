package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.repository.*
import pl.edu.pwr.psi.epk.schedule.service.ScheduleService
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
    fun getLines() =
        ResponseEntity.ok(lineRepository.findAll().map{LineManifestDTO.fromLine(it)})

    //Screen 4.6.2.1
    @GetMapping("/stops")
    fun getStops() =
        ResponseEntity.ok(stopRepository.findAll().map{StopManifestDTO.fromStop(it)})

    @GetMapping("/buses")
    fun getBuses() =
        ResponseEntity.ok(busRepository.findAll().map{BusDTO.fromBus(it)})

    //Screen 4.6.2.2?
    @GetMapping("/lines/{id}")
    fun getLine(@PathVariable id: Long): ResponseEntity<LineDetailsDTO> {
        val line = lineRepository.findById(id)
        if(line.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(LineDetailsDTO.fromLine(line.get()))
    }

    //Screen 4.6.2.2
    @GetMapping("/routes/{id}")
    fun getRoute(@PathVariable id: Long): ResponseEntity<RouteDetailDTO> {
        val route = routeRepository.findById(id)
        if(route.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RouteDetailDTO.fromRoute(route.get()))
    }

    //Screens 4.6.2.3, 4.7.2.1
    @GetMapping("/stops/{id}")
    fun getStopDetails(@PathVariable id: Long, @RequestParam numberOfDepartures: Optional<Int>): ResponseEntity<StopDetailsDTO> {
        val stop = stopRepository.findById(id)
        if(stop.isEmpty)
            return ResponseEntity.notFound().build()
        val departures =
            if(numberOfDepartures.isEmpty)
                scheduleService.getStopDepartures(id, LocalDateTime.now())
            else
                scheduleService.getStopDepartures(id, LocalDateTime.now(), numberOfDepartures.get())
        return ResponseEntity.ok(StopDetailsDTO.fromStopAndDepartures(stop.get(), departures))
    }

    //Screen 4.8.2.1
    @GetMapping("/deviations")
    fun getDeviations() =
        ResponseEntity.ok(scheduleService.getDeviations())


    @GetMapping("/rides/{id}")
    fun getRideStatus(@PathVariable id: Long) : ResponseEntity<RideDTO> {
        val ride = rideRepository.findById(id)
        if(ride.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(RideDTO.fromRide(ride.get()))
    }
}