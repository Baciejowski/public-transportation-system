package pl.edu.pwr.psi.epk.ticket.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient("account")
interface AccountApiService {

    @PostMapping("/account/balance/deduce")
    fun deduceBalance(
        @RequestHeader("user-id", required = true) passengerId: Long,
        @RequestBody amount: Double
    )

}
