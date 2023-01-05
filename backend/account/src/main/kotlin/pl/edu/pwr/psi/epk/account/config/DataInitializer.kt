package pl.edu.pwr.psi.epk.account.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.TicketInspector
import pl.edu.pwr.psi.epk.account.repository.UserRepository
import java.time.LocalDate

@Configuration
@Profile("dev")
class DataInitializer(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val mockUsers = listOf(
            Passenger("joe.doe@mail.com", encoder.encode("secret"), "Joe", "Doe", LocalDate.of(1999, 12, 23)),
            Passenger("mark.doe@mail.com", encoder.encode("secret"), "Mark", "Doe", LocalDate.of(1979, 3, 12)),
            Passenger("fred.doe@mail.com", encoder.encode("secret"), "Fred", "Doe", LocalDate.of(1981, 5, 7)),

            TicketInspector("helen.doe@mail.com", encoder.encode("secret"), "Helen", "Doe", LocalDate.of(1985, 7, 7)),
            TicketInspector("anne.doe@mail.com", encoder.encode("secret"), "Anne", "Doe", LocalDate.of(1991, 11, 11)),
            TicketInspector("justine.doe@mail.com", encoder.encode("secret"), "Justine", "Doe", LocalDate.of(1998, 2, 20))
        )
        userRepository.saveAll(mockUsers)
    }

}