package pl.edu.pwr.psi.epk.ticket.service

import feign.FeignException
import org.springframework.stereotype.Service
import pl.edu.pwr.psi.epk.ticket.client.AccountApiService
import pl.edu.pwr.psi.epk.ticket.exception.ApiCallException
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import pl.edu.pwr.psi.epk.ticket.model.ticket.TicketFactory
import pl.edu.pwr.psi.epk.ticket.repository.OfferedTicketRepository
import pl.edu.pwr.psi.epk.ticket.repository.TicketRepository

@Service
class TicketService(
    val offeredTicketRepository: OfferedTicketRepository,
    val ticketRepository: TicketRepository,
    val accountApiService: AccountApiService
) {

    fun buyTicket(passengerId: Long, offeredTicketId: Long): Ticket {
        val offeredTicket = offeredTicketRepository.findById(offeredTicketId).get()
        if (!offeredTicket.offer.isCurrentlyValid())
            throw IllegalArgumentException("Offer is not valid.")

        val ticket = TicketFactory.createTicket(offeredTicket, passengerId)
        val savedTicket = ticketRepository.save(ticket)

        try {
            accountApiService.deduceBalance(passengerId, offeredTicket.price)
        } catch (ex: FeignException) {
            throw ApiCallException("Error when trying to deduce balance using api service. $ex")
        } catch (ex: Exception) {
            throw ApiCallException("Unexpected error when trying to deduce balance using api service. $ex")
        }

        return savedTicket
    }
}
