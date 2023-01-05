package pl.edu.pwr.psi.epk.account.service.impl

import io.jsonwebtoken.Claims
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import pl.edu.pwr.psi.epk.account.exception.ExpiredTokenException
import pl.edu.pwr.psi.epk.account.exception.MalformedTokenException
import pl.edu.pwr.psi.epk.account.exception.RefreshTokenReuseAttemptException
import pl.edu.pwr.psi.epk.account.model.TokenFamily
import pl.edu.pwr.psi.epk.account.model.TokenPair
import pl.edu.pwr.psi.epk.account.model.User
import pl.edu.pwr.psi.epk.account.repository.TokenFamilyRepository
import pl.edu.pwr.psi.epk.account.service.TokenService
import pl.edu.pwr.psi.epk.account.service.UserService

@Service
class TokenServiceImpl(
    private val tokenFamilyRepository: TokenFamilyRepository,
    private val userService: UserService,
    private val jwtUtil: JwtUtil
): TokenService {
    override fun getTokenFamilyById(id: String): TokenFamily = tokenFamilyRepository.findById(id)
        .orElseThrow { EntityNotFoundException("Could not find token family with id '$id'.") }

    override fun createTokenPair(user: User, tokenFamily: TokenFamily?): TokenPair {
        val family = tokenFamily ?: TokenFamily()

        val tokenPair = jwtUtil.generateTokenPair(user, family.id)
        family.validToken = tokenPair.refreshToken.token
        tokenFamilyRepository.save(family)

        return tokenPair
    }

    override fun createTokenPairUsingRefreshToken(refreshToken: String): TokenPair {
        val claims: Claims = try {
            jwtUtil.getClaims(refreshToken)
        } catch (e: ExpiredTokenException) {
            e.claims?.get("fid", String::class.java)?.let { fid ->
                tokenFamilyRepository.findById(fid).ifPresent { invalidateTokenFamily(it) }
            }
            throw e
        }

        val familyId: String = claims["fid"]?.toString()
            ?: throw MalformedTokenException("Refresh token must come from a token family")
        val tokenFamily = getTokenFamilyById(familyId)

        if (tokenFamily.isInvalidated)
            throw RefreshTokenReuseAttemptException("Token family is invalidated")

        if (tokenFamily.validToken != refreshToken) {
            invalidateTokenFamily(tokenFamily)
            throw RefreshTokenReuseAttemptException("Refresh token has already been used")
        }

        val user = userService.getUserByEmail(claims.subject)
        return createTokenPair(user, tokenFamily)
    }

    private fun invalidateTokenFamily(tokenFamily: TokenFamily) {
        tokenFamily.isInvalidated = true
        tokenFamilyRepository.save(tokenFamily)
    }

}