package pl.edu.pwr.psi.epk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketsApplication

fun main(args: Array<String>) {
    runApplication<TicketsApplication>(*args)
}
