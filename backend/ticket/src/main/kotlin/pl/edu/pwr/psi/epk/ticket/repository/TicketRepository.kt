package pl.edu.pwr.psi.epk.ticket.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import java.time.LocalDateTime

interface TicketRepository : JpaRepository<Ticket, Long> {

    fun findAllByPassengerId(passengerId: Long): List<Ticket>

    @Query("select t from Ticket t where t.passengerId = ?1 and t.punchTime > ?2 or t.punchTime is null")
    fun findAllCurrentTickets(passengerId: Long, fromDate: LocalDateTime): List<Ticket>
}