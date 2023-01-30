package pl.edu.pwr.psi.epk.schedule.service

import pl.edu.pwr.psi.epk.schedule.dto.StopDepartureDTO
import java.time.LocalDateTime

interface ScheduleService {
    fun getStopDepartures(stopId: Long, dateTime: LocalDateTime, numberOfDepartures: Int = 7): List<StopDepartureDTO>
}