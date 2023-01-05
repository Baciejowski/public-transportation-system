package pl.edu.pwr.psi.epk.account.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pwr.psi.epk.account.dto.LoginDto
import pl.edu.pwr.psi.epk.account.dto.RegisterDto
import pl.edu.pwr.psi.epk.account.dto.TokenReadDto
import pl.edu.pwr.psi.epk.account.model.TokenPair
import pl.edu.pwr.psi.epk.account.service.TokenService
import pl.edu.pwr.psi.epk.account.service.UserService
import java.time.Duration
import java.time.LocalDateTime

@RestController
@RequestMapping("/auth")
class AuthController(private val userService: UserService, private val tokenService: TokenService) {

    @PostMapping("/register")
    fun register(@RequestBody registerDto: RegisterDto): ResponseEntity<TokenReadDto> {
        val user = userService.registerUser(registerDto)
        val tokenPair = tokenService.createTokenPair(user)
        return buildCookieTokenResponse(tokenPair)
    }

    @PostMapping("/login")
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<TokenReadDto> {
        val user = userService.authenticateUser(loginDto)
        val tokenPair = tokenService.createTokenPair(user)
        return buildCookieTokenResponse(tokenPair)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestHeader("Authorization", required = true) authHeader: String): ResponseEntity<TokenReadDto> {
        val refreshToken = authHeader.substring("bearer".length).trim()
        val tokenPair = tokenService.createTokenPairUsingRefreshToken(refreshToken)
        return buildCookieTokenResponse(tokenPair)
    }

    private fun buildCookieTokenResponse(tokenPair: TokenPair): ResponseEntity<TokenReadDto> {
        val authCookie = ResponseCookie.from("AuthCookie", tokenPair.accessToken.token)
            .httpOnly(true)
            .sameSite("None")
            .secure(false)
            .maxAge(Duration.between(LocalDateTime.now(), tokenPair.accessToken.expiresAt).seconds)
            .build()
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, authCookie.toString())
            .body(TokenReadDto.fromTokenPair(tokenPair))
    }

}
