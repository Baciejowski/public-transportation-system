package pl.edu.pwr.psi.epk.integration.util

import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

class TestUtils {

    companion object {
        fun configureWebTestClient(port: Int = 8080): WebTestClient {
            val httpClient: HttpClient = HttpClient.create()
            val httpConnector: ClientHttpConnector = ReactorClientHttpConnector(httpClient)
            val baseUri = "http://localhost:$port"
            return WebTestClient
                .bindToServer(httpConnector)
                .baseUrl(baseUri)
                .responseTimeout(Duration.ofSeconds(60))
                .build()
        }

        fun getWebClientWithAuthorization(webTestClient: WebTestClient, token: String): WebTestClient =
            webTestClient
                .mutate()
                .defaultHeaders { it.setBearerAuth(token) }
                .build()
    }

}