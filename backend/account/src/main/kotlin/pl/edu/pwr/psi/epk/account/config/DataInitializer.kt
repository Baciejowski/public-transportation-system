package pl.edu.pwr.psi.epk.account.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder
import pl.edu.pwr.psi.epk.account.model.Moderator
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.Planner
import pl.edu.pwr.psi.epk.account.model.TicketInspector
import pl.edu.pwr.psi.epk.account.repository.UserRepository

@Configuration
class DataInitializer(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
): ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val john = Passenger("john.doe@mail.com", encoder.encode("secret"), "John", "Doe")
        john.walletBalance = 10.0

        val mockUsers = listOf(
            john,
            Passenger("olivia.may@mail.com", encoder.encode("secret"), "Olivia", "May"),
            Passenger("gregory.trevino@mail.com", encoder.encode("secret"), "Gregory", "Trevino"),
            Passenger("donald.soto@mail.com", encoder.encode("secret"), "Donald", "Soto"),
            Passenger("freddie.stuart@mail.com", encoder.encode("secret"), "Freddy", "Stuart"),
            Passenger("emil.briggs@mail.com", encoder.encode("secret"), "Emil", "Briggs"),

            TicketInspector("felix.carey@mail.com", encoder.encode("secret"), "Felix", "Carey"),
            TicketInspector("stephanie.duran@mail.com", encoder.encode("secret"), "Stephanie", "Duran"),
            TicketInspector("doris.sandoval@mail.com", encoder.encode("secret"), "Doris", "Sandoval"),

            Planner("marcel.durham@mail.com", encoder.encode("secret"), "Marcel", "Durham"),
            Planner("emily.wang@mail.com", encoder.encode("secret"), "Emily", "Wang"),

            Moderator("carter.graves@mail.com", encoder.encode("secret"), "Carter", "Graves"),
            Moderator("angelina.shelton@mail.com", encoder.encode("secret"), "Angelina", "Shelton")
        )
        userRepository.saveAll(mockUsers)
    }

}