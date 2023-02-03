package pl.edu.pwr.psi.epk.schedule.controller

import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import pl.edu.pwr.psi.epk.schedule.dto.ErrorDto

@ControllerAdvice
class ErrorHandlingController {

    @ExceptionHandler(value = [Exception::class])
    protected fun handleError(
        e: Exception
    ): ResponseEntity<ErrorDto> = when(e) {
        is IllegalArgumentException, is EntityNotFoundException ->
            ResponseEntity.badRequest()
                .body(ErrorDto(HttpStatus.BAD_REQUEST, e.message))

        is SecurityException ->
            ResponseEntity.status(HttpStatus.FORBIDDEN).build()

        else -> ResponseEntity.internalServerError()
            .body(ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, "Something unexpected happened: ${e.message}"))
    }

}
