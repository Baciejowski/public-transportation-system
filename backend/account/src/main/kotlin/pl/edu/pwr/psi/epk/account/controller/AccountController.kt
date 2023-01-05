package pl.edu.pwr.psi.epk.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.account.dto.UserReadDto
import pl.edu.pwr.psi.epk.account.model.Role
import java.time.LocalDate

@RestController
@RequestMapping("/account")
class AccountController {

    @GetMapping
    fun getAccountInfo(): ResponseEntity<UserReadDto> = ResponseEntity.ok(
        UserReadDto(
            1,
            "mail@mail.com",
            "Joe",
            "Doe",
            LocalDate.MIN,
            Role.PASSENGER,
            0.0
        )
    )

    // TODO: investigate 3rd party payment + dtos
    @PostMapping("/{id}/balance")
    fun topUpBalance(@PathVariable id: String): ResponseEntity<*> = ResponseEntity.ok("OK :)")

}