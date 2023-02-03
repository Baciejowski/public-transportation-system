package pl.edu.pwr.psi.epk.ticket.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.ticket.infrastructure.RequiredRole
import pl.edu.pwr.psi.epk.ticket.infrastructure.Role
import pl.edu.pwr.psi.epk.ticket.model.offer.TicketOffer
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import pl.edu.pwr.psi.epk.ticket.repository.TicketOfferRepository
import pl.edu.pwr.psi.epk.ticket.repository.TicketRepository
import pl.edu.pwr.psi.epk.ticket.service.TicketService
import java.time.LocalDateTime
import java.time.ZoneOffset


@RestController
@RequestMapping("/tickets")
class TicketController(
    val ticketOfferRepository: TicketOfferRepository,
    val ticketRepository: TicketRepository,
    val ticketService: TicketService
) {

    @GetMapping
    @RequiredRole(Role.PASSENGER)
    fun getUserTickets(
        @RequestHeader("user-id", required = true) passengerId: Long
    ): ResponseEntity<List<Ticket>> {

        return ResponseEntity.ok(
            ticketService.getCurrentTickets(passengerId)
        )
    }

    @GetMapping("/all")
    @RequiredRole(Role.PASSENGER)
    fun getAllUserTickets(
        @RequestHeader("user-id", required = true) passengerId: Long
    ): ResponseEntity<List<Ticket>> {

        return ResponseEntity.ok(
            ticketRepository.findAllByPassengerId(passengerId)
        )
    }

    @PatchMapping("/punch")
    @RequiredRole(Role.PASSENGER)
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
    @RequiredRole(Role.PASSENGER)
    fun getCurrentOffer(): ResponseEntity<List<TicketOffer>> =
        ResponseEntity.ok(
            ticketOfferRepository.findOfferByDate(LocalDateTime.now(ZoneOffset.UTC))
        )

    @PostMapping("/offer/buy")
    @RequiredRole(Role.PASSENGER)
    fun buyTickets(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestParam offeredTicketId: Long,
        @RequestParam(required = false, defaultValue = "1") quantity: Int
    ): ResponseEntity<List<Ticket>> {

        return ResponseEntity.ok(
            ticketService.buyTickets(passengerId, offeredTicketId, quantity)
        )
    }

    @GetMapping("/validate")
    @RequiredRole(Role.TICKET_INSPECTOR)
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

