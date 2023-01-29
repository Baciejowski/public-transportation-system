package pl.edu.pwr.psi.epk.ticket.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedTicket

interface OfferedTicketRepository : JpaRepository<OfferedTicket, Long>