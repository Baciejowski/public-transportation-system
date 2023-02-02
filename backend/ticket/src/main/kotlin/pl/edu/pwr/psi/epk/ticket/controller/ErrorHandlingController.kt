package pl.edu.pwr.psi.epk.ticket.controller

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.pwr.psi.epk.ticket.dto.ErrorDto
import pl.edu.pwr.psi.epk.ticket.exception.ApiCallException

@ControllerAdvice
class ErrorHandlingController {

    @ExceptionHandler(value = [Exception::class])
    protected fun handleError(
        e: Exception
    ): ResponseEntity<ErrorDto> = when(e) {
        is IllegalArgumentException, is EntityNotFoundException ->
            ResponseEntity.badRequest()
                .body(ErrorDto(HttpStatus.BAD_REQUEST, e.message))
        is ApiCallException ->
            ResponseEntity(ErrorDto(HttpStatus.SERVICE_UNAVAILABLE, e.message), HttpStatus.SERVICE_UNAVAILABLE)
        else -> ResponseEntity.internalServerError()
            .body(ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Something unexpected happened: ${e.message}"))
    }

}
