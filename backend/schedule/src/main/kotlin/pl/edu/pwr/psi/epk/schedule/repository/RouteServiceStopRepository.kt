package pl.edu.pwr.psi.epk.schedule.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.schedule.model.RouteServiceStop

interface RouteServiceStopRepository: JpaRepository<RouteServiceStop, Long>