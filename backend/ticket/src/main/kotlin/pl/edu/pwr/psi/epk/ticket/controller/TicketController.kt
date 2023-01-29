package pl.edu.pwr.psi.epk.ticket.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import pl.edu.pwr.psi.epk.ticket.service.TicketService
import java.time.LocalDateTime
import java.time.ZoneOffset

// TODO implement the abstraction
class TicketReadDto(val id: Long, val punchTime: LocalDateTime)
class OfferedTicketDto(val id: Long, val price: Double)

@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketOfferRepository: TicketOfferRepository,
    val ticketService: TicketService
) {

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
            ticketOfferRepository.findOfferByDate(LocalDateTime.now(ZoneOffset.UTC))
        )

    @PostMapping("/offer/buy")
    fun buyTicket(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestParam offeredTicketId: Long
    ): ResponseEntity<Ticket> {

        return ResponseEntity.ok(
            ticketService.buyTicket(offeredTicketId, passengerId)
        )
    }

}

