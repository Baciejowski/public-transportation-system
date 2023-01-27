package pl.edu.pwr.psi.epk.ticket.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pl.edu.pwr.psi.epk.ticket.model.*
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import java.time.Duration
import java.time.LocalDateTime

@Component
class DevDataInitializer(private val tickerOfferRepo: TicketOfferRepository) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        initializeTicketOffer()
    }

    private fun initializeTicketOffer() {
        val ticketOffer = TicketOffer(
            LocalDateTime.now().minusMonths(2),
            LocalDateTime.now().plusMonths(6),
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
