package pl.edu.pwr.psi.epk.ticket.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pwr.psi.epk.ticket.model.TicketOffer
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import java.time.LocalDateTime

// TODO implement the abstraction
data class TicketReadDto(val id: Long, val punchTime: LocalDateTime)
data class OfferedTicketDto(val id: Long, val price: Double)

@RestController
@RequestMapping("/tickets")
class TicketController {

    @Autowired
    private lateinit var ticketOfferRepository: TicketOfferRepository

    @GetMapping
    fun getUserTickets(): ResponseEntity<List<TicketReadDto>> =
        ResponseEntity.ok(
            listOf(
                TicketReadDto(
                    1L,
                    LocalDateTime.MIN
                )
            )
        )

    @PatchMapping
    fun punchTicket(ticketId: Long, rideId: Long): ResponseEntity<TicketReadDto> =
        ResponseEntity.ok(
            TicketReadDto(
                1L,
                LocalDateTime.MIN
            )
        )

    @GetMapping("/offer")
    fun getCurrentOffer(): ResponseEntity<List<TicketOffer>> =
        ResponseEntity.ok(
            ticketOfferRepository.findAll()
        )

    @PostMapping("/offer/buy")
    fun buyTicket(offeredTicketId: Long): TicketReadDto =
        TicketReadDto(
            1L,
            LocalDateTime.MIN
        )
}
