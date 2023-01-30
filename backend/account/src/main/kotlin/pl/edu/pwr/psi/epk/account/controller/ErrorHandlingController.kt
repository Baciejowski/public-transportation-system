package pl.edu.pwr.psi.epk.account.controller

import jakarta.persistence.EntityNotFoundException
import org.apache.http.auth.InvalidCredentialsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.pwr.psi.epk.account.dto.ErrorDto
import pl.edu.pwr.psi.epk.account.exception.ExpiredTokenException
import pl.edu.pwr.psi.epk.account.exception.MalformedTokenException
import pl.edu.pwr.psi.epk.account.exception.RefreshTokenReuseAttemptException

@ControllerAdvice
class ErrorHandlingController {

    @ExceptionHandler(value = [Exception::class])
    protected fun handleError(
        e: Exception
    ): ResponseEntity<ErrorDto> = when(e) {
        is IllegalArgumentException, is EntityNotFoundException, is MalformedTokenException, is InvalidCredentialsException ->
            ResponseEntity.badRequest()
                .body(ErrorDto(HttpStatus.BAD_REQUEST, e.message))

        is ExpiredTokenException, is RefreshTokenReuseAttemptException ->
            ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorDto(HttpStatus.UNAUTHORIZED, e.message))

        else -> ResponseEntity.internalServerError()
            .body(ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Something unexpected happened: ${e.message}"))
    }

}
