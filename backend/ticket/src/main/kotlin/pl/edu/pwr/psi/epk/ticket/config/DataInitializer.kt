package pl.edu.pwr.psi.epk.ticket.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedOneWayTicket
import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedTimeLimitedTicket
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

@Component
class DevDataInitializer(private val tickerOfferRepo: TicketOfferRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        initializeTicketOffer()
    }

    private fun initializeTicketOffer() {
        val ticketOffer = TicketOffer(
            LocalDateTime.now(ZoneOffset.UTC).minusMonths(2),
            LocalDateTime.now(ZoneOffset.UTC).plusMonths(6),
        )

        ticketOffer.tickets += listOf(
            OfferedOneWayTicket(
                2.5,
                false,
                ticketOffer
            ),
            OfferedOneWayTicket(
                1.3,
                true,
                ticketOffer
            ),
            OfferedTimeLimitedTicket(
                16.0,
                false,
                ticketOffer,
                Duration.ofDays(14)
            ),
            OfferedTimeLimitedTicket(
                8.0,
                true,
                ticketOffer,
                Duration.ofDays(14)
            ),
            OfferedTimeLimitedTicket(
                30.0,
                false,
                ticketOffer,
                Duration.ofDays(30)
            ),
            OfferedTimeLimitedTicket(
                16.0,
                true,
                ticketOffer,
                Duration.ofDays(30)
            )
        )

        tickerOfferRepo.save(ticketOffer)
    }

}
