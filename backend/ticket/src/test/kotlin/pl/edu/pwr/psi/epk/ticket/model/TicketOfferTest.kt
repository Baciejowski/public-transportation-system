package pl.edu.pwr.psi.epk.ticket.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer

import java.time.LocalDateTime
import java.time.ZoneOffset

class TicketOfferTest {

    @Test
    fun testIsCurrentlyValidWhenOfferEndIsNull() {
        val offer = TicketOffer(
            LocalDateTime.now(ZoneOffset.UTC).minusMonths(1)
        )

        assertThat(offer.isCurrentlyValid()).isTrue
    }

    @Test
    fun testIsCurrentlyValidWhenOfferEndIsNotNull() {
        val offer = TicketOffer(
            LocalDateTime.now(ZoneOffset.UTC).minusMonths(1),
            LocalDateTime.now(ZoneOffset.UTC).plusMonths(1)
        )

        assertThat(offer.isCurrentlyValid()).isTrue
    }

    @Test
    fun testIsCurrentlyValidWhenOfferIsNotValid() {
        val offer = TicketOffer(
            LocalDateTime.now(ZoneOffset.UTC).plusMonths(1),
            LocalDateTime.now(ZoneOffset.UTC).plusMonths(2)
        )

        assertThat(offer.isCurrentlyValid()).isFalse
    }

    @Test
    fun testIsCurrentlyValidWhenOfferIsNotValidAndOfferEndIsNull() {
        val offer = TicketOffer(
            LocalDateTime.now(ZoneOffset.UTC).plusMonths(1)
        )

        assertThat(offer.isCurrentlyValid()).isFalse
    }
}
