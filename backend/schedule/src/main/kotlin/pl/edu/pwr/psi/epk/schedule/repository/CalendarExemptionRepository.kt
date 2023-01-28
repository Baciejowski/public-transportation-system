package pl.edu.pwr.psi.epk.schedule.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.schedule.model.CalendarExemption

interface CalendarExemptionRepository: JpaRepository<CalendarExemption, Long>