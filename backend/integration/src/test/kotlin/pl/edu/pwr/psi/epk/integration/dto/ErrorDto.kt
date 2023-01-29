package pl.edu.pwr.psi.epk.integration.dto

import org.springframework.http.HttpStatus

data class ErrorDto(
    val status: HttpStatus,
    val message: String?
)
