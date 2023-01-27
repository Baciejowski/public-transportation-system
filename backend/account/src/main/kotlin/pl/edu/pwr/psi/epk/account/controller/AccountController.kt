package pl.edu.pwr.psi.epk.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.account.dto.UserReadDto
import pl.edu.pwr.psi.epk.account.service.UserService

@RestController
@RequestMapping("/account")
class AccountController(
    private val userService: UserService
) {

    @GetMapping
    fun getAccountInfo(@RequestHeader("user-id", required = true) userId: Long): ResponseEntity<UserReadDto> =
        ResponseEntity.ok(UserReadDto.fromUser(userService.getUserById(userId)))

    // TODO: investigate 3rd party payment + dtos
    @PostMapping("/{id}/balance")
    fun topUpBalance(@PathVariable id: String): ResponseEntity<*> = ResponseEntity.ok("OK :)")

}