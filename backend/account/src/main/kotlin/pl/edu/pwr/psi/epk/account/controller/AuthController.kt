package pl.edu.pwr.psi.epk.account.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pwr.psi.epk.account.dto.LoginDto
import pl.edu.pwr.psi.epk.account.dto.RegisterDto
import pl.edu.pwr.psi.epk.account.dto.TokenReadDto
import java.time.LocalDateTime

@RestController
@RequestMapping("/auth")
class AuthController {

    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<TokenReadDto> = ResponseEntity.ok(
        TokenReadDto("accessToken", LocalDateTime.MIN, "refreshToken", LocalDateTime.MAX)
    )

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<TokenReadDto> = ResponseEntity.ok(
        TokenReadDto("accessToken", LocalDateTime.MIN, "refreshToken", LocalDateTime.MAX)
    )

}
