package pl.edu.pwr.psi.epk.schedule.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.schedule.model.Ride
import pl.edu.pwr.psi.epk.schedule.model.RouteService

interface RideRepository: JpaRepository<Ride, Long> {
    fun findAllByStartTimeIsNotNullAndEndTimeIsNull(): List<Ride>
    fun findAllByRouteService(routeService: RouteService): List<Ride>
}