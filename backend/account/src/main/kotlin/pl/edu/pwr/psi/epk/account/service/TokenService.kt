package pl.edu.pwr.psi.epk.account.service

import pl.edu.pwr.psi.epk.account.model.TokenFamily
import pl.edu.pwr.psi.epk.account.model.TokenPair
import pl.edu.pwr.psi.epk.account.model.User

interface TokenService {

    fun getTokenFamilyById(id: String): TokenFamily

    fun createTokenPair(user: User, tokenFamily: TokenFamily? = null): TokenPair

    fun createTokenPairUsingRefreshToken(refreshToken: String): TokenPair

}