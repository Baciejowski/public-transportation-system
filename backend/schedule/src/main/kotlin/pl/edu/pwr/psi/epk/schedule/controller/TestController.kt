package pl.edu.pwr.psi.epk.schedule.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.Inet4Address

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping
    fun greet() = ResponseEntity.ok("Hello from ${this.javaClass}! My IP: ${Inet4Address.getLocalHost().hostAddress}.")
}
