package pl.edu.pwr.psi.epk.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.account.model.TokenFamily

interface TokenFamilyRepository: JpaRepository<TokenFamily, String>