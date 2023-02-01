package pl.edu.pwr.psi.epk.ticket.infrastructure

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiredRole(vararg val roles: Role)

enum class Role {
    PASSENGER, TICKET_INSPECTOR, MODERATOR, PLANNER
}
