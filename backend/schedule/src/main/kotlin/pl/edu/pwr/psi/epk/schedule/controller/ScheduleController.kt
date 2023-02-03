package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.schedule.dto.*
import pl.edu.pwr.psi.epk.schedule.infrastructure.RequiredRole
import pl.edu.pwr.psi.epk.schedule.infrastructure.Role
import pl.edu.pwr.psi.epk.schedule.model.RideStop
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
    val rideRepository: RideRepository,
    val rideStopRepository: RideStopRepository
) {
    //Screen 4.6.2.1
    @GetMapping("/lines")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getLines() =
        ResponseEntity.ok(lineRepository.findAll().map{LineManifestDTO.fromLine(it)})

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

    @GetMapping("/lines/{id}")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
    fun getLine(@PathVariable id: Long): ResponseEntity<LineDetailsDTO> {
        val line = lineRepository.findById(id)
        if(line.isEmpty)
            return ResponseEntity.notFound().build()
        return ResponseEntity.ok(LineDetailsDTO.fromLine(line.get()))
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

    //Screens 4.6.2.3, 4.7.2.1
    @GetMapping("/stops/{id}")
    @RequiredRole(Role.PASSENGER, Role.PLANNER)
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

    @PostMapping("/rides/{id}/stops")
    fun saveRideStop(@PathVariable id: Long, @RequestBody rideStopDTO: RideStopDTO) : ResponseEntity<RideDTO> {
        val ride = rideRepository.findById(id)
        if(ride.isEmpty)
            return ResponseEntity.notFound().build()
        var rideObj = ride.get()
        if(rideStopDTO.stopNo <= rideObj.rideStops.filterNotNull().size)
            return ResponseEntity(HttpStatus.CONFLICT)
        val routeStops = rideObj.routeService.routeServiceStops.filterNotNull()
        if(rideStopDTO.stopNo > routeStops.size)
            return ResponseEntity.badRequest().build()
        if(rideObj.rideStops.filterNotNull().isEmpty())
            rideObj.startTime = LocalDateTime.now()
        for(i in rideObj.rideStops.filterNotNull().size until rideStopDTO.stopNo){
            val rideStop = rideStopRepository.save(RideStop(rideObj,routeStops[i]))
            rideObj.rideStops += rideStop
        }
        if(rideObj.rideStops.filterNotNull().size == routeStops.size)
            rideObj.endTime = LocalDateTime.now()
        rideObj = rideRepository.save(rideObj)
        return ResponseEntity.ok(RideDTO.fromRide(rideObj))
    }

    @GetMapping("/rides")
    fun getCurrentRides(): ResponseEntity<List<RideDTO>> {
        return ResponseEntity.ok((
                rideRepository
                    .findAllByStartTimeIsNotNullAndEndTimeIsNull() +
                        rideRepository
                            .findAll()
                            .filter { it.endTime == null && LocalDateTime.now().isAfter(it.plannedStartTime)}
                ).distinctBy { it.id }.map { RideDTO.fromRide(it) })
    }
}