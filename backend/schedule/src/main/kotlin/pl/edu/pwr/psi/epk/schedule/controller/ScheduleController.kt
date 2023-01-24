package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pwr.psi.epk.schedule.model.Coordinates
import java.time.LocalDate

data class RideReadDto(val serviceId: Int, val busSideNumber: Int, val date: LocalDate)

@RestController
@RequestMapping("/schedule")
class ScheduleController {

    @GetMapping
    fun getSchedule(lineId: String): ResponseEntity<*> = TODO()

    @GetMapping("/rides")
    fun getCurrentRide(sideNumber: String): ResponseEntity<RideReadDto> = TODO()

    @GetMapping("/location")
    fun getCurrentLocation(rideId: String): ResponseEntity<Coordinates> = TODO()

    @PatchMapping("/location/next")
    fun updateCurrentLocation(rideId: String): ResponseEntity<*> = TODO()

}