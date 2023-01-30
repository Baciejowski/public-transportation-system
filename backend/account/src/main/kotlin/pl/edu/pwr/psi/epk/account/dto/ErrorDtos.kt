package pl.edu.pwr.psi.epk.account.dto

import org.springframework.http.HttpStatus

data class ErrorDto(
    val status: HttpStatus,
    val message: String?
)
