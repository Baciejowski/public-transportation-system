package pl.edu.pwr.psi.epk.integration.endpoint

import org.springframework.http.HttpMethod
import pl.edu.pwr.psi.epk.integration.dto.account.BalanceTopUpDto
import pl.edu.pwr.psi.epk.integration.dto.account.Role

open class Endpoint(
    val service: String,
    val name: String,
    val method: HttpMethod,
    val path: String,
    val dummyPathArgs: Array<String>?,
    val dummyBody: Any?
) {
    
    companion object {
        val EPK_FORBIDDEN_ENDPOINTS_FOR_ROLES: Map<Endpoint, List<Role>> = mapOf(
            AccountEndpoint.BALANCE_TOP_UP to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            AccountEndpoint.BALANCE_DEDUCE to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),

            TicketEndpoint.GET_TICKETS to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            TicketEndpoint.GET_ALL_TICKETS to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            TicketEndpoint.GET_TICKETS_OFFER to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            TicketEndpoint.BUY_TICKET to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            TicketEndpoint.PUNCH_TICKET to listOf(Role.TICKET_INSPECTOR, Role.PLANNER),
            TicketEndpoint.VALIDATE_TICKET to listOf(Role.PASSENGER, Role.PLANNER),

            ScheduleEndpoint.GET_LINES to listOf(Role.TICKET_INSPECTOR),
            ScheduleEndpoint.GET_STOPS to listOf(Role.TICKET_INSPECTOR),
            ScheduleEndpoint.GET_ROUTE to listOf(Role.TICKET_INSPECTOR),
            ScheduleEndpoint.GET_RIDE to listOf(Role.TICKET_INSPECTOR),
            ScheduleEndpoint.GET_DEVIATIONS to listOf(Role.TICKET_INSPECTOR),
            ScheduleEndpoint.GET_BUSES to listOf(Role.TICKET_INSPECTOR),
        )
    }
    
}
class AccountEndpoint(
    name: String,
    method: HttpMethod,
    path: String,
    dummyPathArgs: Array<String>? = null,
    dummyBody: Any? = null
): Endpoint("Account", name, method, path, dummyPathArgs, dummyBody) {

    companion object {
        private const val ACCOUNT_SERVICE_PREFIX = "/account"
        val REGISTER = AccountEndpoint("Register", HttpMethod.POST, "$ACCOUNT_SERVICE_PREFIX/auth/register")
        val LOGIN = AccountEndpoint("Login", HttpMethod.POST, "$ACCOUNT_SERVICE_PREFIX/auth/login")
        val TOKEN_REFRESH = AccountEndpoint("Token refresh", HttpMethod.POST, "$ACCOUNT_SERVICE_PREFIX/auth/refresh")
        val GET_ACCOUNT_INFO = AccountEndpoint("Get account info", HttpMethod.GET, "$ACCOUNT_SERVICE_PREFIX/account")
        val BALANCE_TOP_UP = AccountEndpoint("Balance top up", HttpMethod.POST, "$ACCOUNT_SERVICE_PREFIX/account/balance/top-up", arrayOf(), BalanceTopUpDto(10.0))
        val BALANCE_DEDUCE = AccountEndpoint("Balance deduce", HttpMethod.POST, "$ACCOUNT_SERVICE_PREFIX/account/balance/deduce", arrayOf(), 10.0)
    }
}

class TicketEndpoint(
    name: String,
    method: HttpMethod,
    path: String,
    dummyPathArgs: Array<String>? = null,
    dummyBody: Any? = null
): Endpoint("Ticket", name, method, path, dummyPathArgs, dummyBody) {

    companion object {
        private const val TICKET_SERVICE_PREFIX = "/ticket"
        val GET_TICKETS = TicketEndpoint("Get tickets", HttpMethod.GET, "$TICKET_SERVICE_PREFIX/tickets")
        val GET_ALL_TICKETS = TicketEndpoint("Get tickets", HttpMethod.GET, "$TICKET_SERVICE_PREFIX/tickets/all")
        val GET_TICKETS_OFFER = TicketEndpoint("Get tickets offer", HttpMethod.GET, "$TICKET_SERVICE_PREFIX/tickets/offer")
        val BUY_TICKET = TicketEndpoint("Buy ticket", HttpMethod.POST, "$TICKET_SERVICE_PREFIX/tickets/offer/buy?offeredTicketId={offeredTicketId}", arrayOf("1"))
        val PUNCH_TICKET = TicketEndpoint("Punch ticket", HttpMethod.PATCH, "$TICKET_SERVICE_PREFIX/tickets/punch?ticketId={ticketId}&rideId={rideId}", arrayOf("1", "1"))
        val VALIDATE_TICKET = TicketEndpoint("Validate ticket", HttpMethod.GET, "$TICKET_SERVICE_PREFIX/tickets/validate?ticketId={ticketId}&rideId={rideId}", arrayOf("1", "1"))
    }

}

class ScheduleEndpoint(
    name: String,
    method: HttpMethod,
    path: String,
    dummyPathArgs: Array<String>? = null,
    dummyBody: Any? = null
): Endpoint("Schedule", name, method, path, dummyPathArgs, dummyBody) {

    companion object {
        private const val SCHEDULE_SERVICE_PREFIX = "/schedule/schedule"
        val GET_LINES = ScheduleEndpoint("Get lines", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/lines")
        val GET_STOPS = ScheduleEndpoint("Get stops", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/stops")
        val GET_ROUTE = ScheduleEndpoint("Get route", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/routes/{routeId}", arrayOf("1"))
        val GET_RIDE = ScheduleEndpoint("Get ride", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/rides/{rideId}", arrayOf("1"))
        val GET_DEVIATIONS = ScheduleEndpoint("Get deviations", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/deviations")
        val GET_BUSES = ScheduleEndpoint("Get buses", HttpMethod.GET, "$SCHEDULE_SERVICE_PREFIX/buses")
    }

}


