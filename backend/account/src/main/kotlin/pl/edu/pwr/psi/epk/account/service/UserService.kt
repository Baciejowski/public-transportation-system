package pl.edu.pwr.psi.epk.account.service

import pl.edu.pwr.psi.epk.account.dto.LoginDto
import pl.edu.pwr.psi.epk.account.dto.RegisterDto
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.User

interface UserService {

    fun getUserById(id: Long): User
    fun getUserByEmail(email: String): User
    fun userExistsByEmail(email: String): Boolean
    fun registerUser(registerDto: RegisterDto): User
    fun authenticateUser(loginDto: LoginDto): User

    fun deduceBalance(passengerId: Long, amount: Double): Passenger

}
