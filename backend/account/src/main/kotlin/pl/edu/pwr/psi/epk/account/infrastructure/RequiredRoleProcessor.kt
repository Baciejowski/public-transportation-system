package pl.edu.pwr.psi.epk.account.infrastructure

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import pl.edu.pwr.psi.epk.account.model.Role

@Aspect
@Component
class RequiredRoleProcessor {

    @Around("@annotation(pl.edu.pwr.psi.epk.account.infrastructure.RequiredRole)")
    fun process(call: ProceedingJoinPoint): ResponseEntity<*> {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val annotation = (call.signature as MethodSignature).method.getAnnotation(RequiredRole::class.java)

        return try {
            val role = Role.valueOf(request.getHeader("user-role"))
            if (role == Role.MODERATOR || annotation.roles.contains(role))
                call.proceed() as ResponseEntity<*>
            else
                ResponseEntity.status(HttpStatus.FORBIDDEN).build<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.FORBIDDEN).build<Any>()
        }

    }

}