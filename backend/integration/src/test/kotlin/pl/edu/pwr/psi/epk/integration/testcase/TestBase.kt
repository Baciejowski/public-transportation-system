package pl.edu.pwr.psi.epk.integration.testcase

import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Testcontainers
import pl.edu.pwr.psi.epk.integration.actor.ModeratorActor
import pl.edu.pwr.psi.epk.integration.actor.PassengerActor
import pl.edu.pwr.psi.epk.integration.actor.PlannerActor
import pl.edu.pwr.psi.epk.integration.actor.TicketInspectorActor
import pl.edu.pwr.psi.epk.integration.util.TestUtils
import java.io.File
import java.time.Duration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class TestBase {

    val webTestClient = TestUtils.configureWebTestClient()

    companion object {
        private val DOCKER_COMPOSE_ENV = DockerComposeContainer(File(System.getenv("DOCKER_COMPOSE_PATH")))
            .waitingFor(
                "gateway-app", Wait.forHttp("/account/test")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(10))
            )
            .waitingFor(
                "gateway-app", Wait.forHttp("/ticket/test")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(10))
            )
            .waitingFor(
                "gateway-app", Wait.forHttp("/schedule/test")
                    .forStatusCode(200)
                    .withStartupTimeout(Duration.ofMinutes(10))
            )
            .withStartupTimeout(Duration.ofMinutes(5))
            .withLocalCompose(true)

        init {
            DOCKER_COMPOSE_ENV.start()
        }
    }

    @BeforeEach
    fun resetActors() {
        PassengerActor.PASSENGERS.forEach { it.reset() }
        TicketInspectorActor.TICKET_INSPECTORS.forEach { it.reset() }
        PlannerActor.PLANNERS.forEach { it.reset() }
        ModeratorActor.MODERATORS.forEach { it.reset() }
    }

}