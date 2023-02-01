package pl.edu.pwr.psi.epk.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.account.dto.BalanceTopUpDto
import pl.edu.pwr.psi.epk.account.dto.UserReadDto
import pl.edu.pwr.psi.epk.account.infrastructure.RequiredRole
import pl.edu.pwr.psi.epk.account.model.Role
import pl.edu.pwr.psi.epk.account.service.UserService

@RestController
@RequestMapping("/account")
class AccountController(
    private val userService: UserService
) {

    @GetMapping
    fun getAccountInfo(@RequestHeader("user-id", required = true) userId: Long): ResponseEntity<UserReadDto> =
        ResponseEntity.ok(UserReadDto.fromUser(userService.getUserById(userId)))

    @PostMapping("/balance/top-up")
    @RequiredRole(Role.PASSENGER)
    fun topUpBalance(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestBody balanceTopUpDto: BalanceTopUpDto
    ): ResponseEntity<UserReadDto> {
        return ResponseEntity.ok(UserReadDto.fromUser(userService.topUpBalance(passengerId, balanceTopUpDto.amount)))
    }

    @PostMapping("/balance/deduce")
    @RequiredRole(Role.PASSENGER)
    fun deduceBalance(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestBody amount: Double
    ): ResponseEntity<UserReadDto> {
        return ResponseEntity.ok(UserReadDto.fromUser(userService.deduceBalance(passengerId, amount)))
    }
}
