package pl.edu.pwr.psi.epk.account.infrastructure

import pl.edu.pwr.psi.epk.account.model.Role

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequiredRole(vararg val roles: Role)
