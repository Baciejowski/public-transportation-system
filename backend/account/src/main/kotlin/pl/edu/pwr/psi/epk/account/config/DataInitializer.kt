package pl.edu.pwr.psi.epk.account.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.TicketInspector
import pl.edu.pwr.psi.epk.account.repository.UserRepository

@Configuration
@Profile("dev")
class DataInitializer(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val mockUsers = listOf(
            Passenger("joe.doe@mail.com", encoder.encode("secret"), "Joe", "Doe"),
            Passenger("mark.doe@mail.com", encoder.encode("secret"), "Mark", "Doe"),
            Passenger("fred.doe@mail.com", encoder.encode("secret"), "Fred", "Doe"),

            TicketInspector("helen.doe@mail.com", encoder.encode("secret"), "Helen", "Doe"),
            TicketInspector("anne.doe@mail.com", encoder.encode("secret"), "Anne", "Doe"),
            TicketInspector("justine.doe@mail.com", encoder.encode("secret"), "Justine", "Doe")
        )
        userRepository.saveAll(mockUsers)
    }

}