package pl.edu.pwr.psi.epk.tickets.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping
    fun greet() = ResponseEntity.ok("Hello from ${this.javaClass}")
}
