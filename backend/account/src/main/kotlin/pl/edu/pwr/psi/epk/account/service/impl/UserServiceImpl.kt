package pl.edu.pwr.psi.epk.account.service.impl

import jakarta.persistence.EntityNotFoundException
import org.apache.http.auth.InvalidCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pl.edu.pwr.psi.epk.account.dto.LoginDto
import pl.edu.pwr.psi.epk.account.dto.RegisterDto
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.User
import pl.edu.pwr.psi.epk.account.repository.UserRepository
import pl.edu.pwr.psi.epk.account.service.UserService

@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val encoder: PasswordEncoder
): UserService {

    override fun getUserById(id: Long): User = userRepository.findById(id)
        .orElseThrow { throw EntityNotFoundException("Could not find user with id '$id'.") }

    override fun getUserByEmail(email: String): User = userRepository.findByEmail(email)
        ?: throw EntityNotFoundException("Could not find user with email '$email'.")

    override fun userExistsByEmail(email: String): Boolean = userRepository.existsByEmail(email)

    override fun registerUser(registerDto: RegisterDto): User {
        if(userExistsByEmail(registerDto.email))
            throw IllegalArgumentException("User with email '${registerDto.email}' already exists.")

        val userAccount = Passenger(
            registerDto.email,
            encoder.encode(registerDto.password),
            registerDto.firstName,
            registerDto.lastName
        )
        return userRepository.save(userAccount)
    }

    override fun authenticateUser(loginDto: LoginDto): User {
        val user = getUserByEmail(loginDto.email)

        return if(encoder.matches(loginDto.password, user.password) && user.active) user
        else throw InvalidCredentialsException("Could not authenticate user with email '${loginDto.email}'.")
    }

    override fun topUpBalance(passengerId: Long, amount: Double): Passenger {
        val passenger: User = getUserById(passengerId)

        if (passenger !is Passenger)
            throw IllegalArgumentException("User is not a passenger")

        passenger.walletBalance += amount
        return userRepository.save(passenger)
    }

    override fun deduceBalance(passengerId: Long, amount: Double): Passenger {
        val passenger: User = getUserById(passengerId)

        if (passenger !is Passenger)
            throw IllegalArgumentException("User is not a passenger")

        if (passenger.walletBalance < amount)
            throw IllegalArgumentException("Passenger does not have enough money.")

        passenger.walletBalance = passenger.walletBalance - amount

        return userRepository.save(passenger)
    }
}