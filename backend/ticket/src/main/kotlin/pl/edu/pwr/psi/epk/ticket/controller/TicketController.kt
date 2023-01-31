package pl.edu.pwr.psi.epk.ticket.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import pl.edu.pwr.psi.epk.ticket.repository.TicketRepository
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
    val ticketRepository: TicketRepository,
    val ticketService: TicketService
) {

    @GetMapping
    fun getUserTickets(
        @RequestHeader("user-id", required = true) passengerId: Long
    ): ResponseEntity<List<Ticket>> {

        return ResponseEntity.ok(
            ticketRepository.findAllByPassengerId(passengerId)
        )
    }

    @PatchMapping("/punch")
    fun punchTicket(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestParam ticketId: Long,
        @RequestParam rideId: Long
    ): ResponseEntity<Ticket> {

        return ResponseEntity.ok(
            ticketService.punchTicket(passengerId, ticketId, rideId)
        )
    }

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
            ticketService.buyTicket(passengerId, offeredTicketId)
        )
    }

    @GetMapping("/validate")
    fun validateTicket(
        @RequestHeader("user-id", required = true) inspectorId: Long,
        @RequestParam ticketId: Long,
        @RequestParam rideId: Long
    ): ResponseEntity<Boolean> {

        return ResponseEntity.ok(
            ticketService.validateTicket(ticketId, rideId)
        )
    }

}

