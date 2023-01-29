package pl.edu.pwr.psi.epk.ticket.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer
import java.time.LocalDateTime

interface TicketOfferRepository : JpaRepository<TicketOffer, Long> {

    @Query("select t from TicketOffer t where ?1 between t.offerStart and t.offerEnd")
    fun findOfferByDate(now: LocalDateTime): List<TicketOffer>
}