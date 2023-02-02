package pl.edu.pwr.psi.epk.integration.step

import org.springframework.http.HttpMethod
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono

class EndpointRestrictionSteps {

    companion object {
        fun userHasNoAccessToEndpoint(
            webTestClient: WebTestClient,
            endpointMethod: HttpMethod,
            endpointPath: String,
            body: Any? = null,
            vararg urlArgs: String
        ): WebTestClient.ResponseSpec {
            return when(endpointMethod) {
                HttpMethod.GET -> userExecutesGetOnForbiddenEndpoint(webTestClient, endpointPath, *urlArgs)
                HttpMethod.POST -> userExecutesPostOnForbiddenEndpoint(webTestClient, endpointPath, body, *urlArgs)
                HttpMethod.PATCH -> userExecutesPatchOnForbiddenEndpoint(webTestClient, endpointPath, *urlArgs)
                else -> throw NotImplementedError()
            }
        }

        private fun userExecutesGetOnForbiddenEndpoint(
            webTestClient: WebTestClient,
            endpoint: String,
            vararg urlArgs: String
        ): WebTestClient.ResponseSpec =
            webTestClient
                .get()
                .uri(endpoint, *urlArgs)
                .exchange()
                .expectStatus().isForbidden

        private fun userExecutesPostOnForbiddenEndpoint(
            webTestClient: WebTestClient,
            endpoint: String,
            body: Any?,
            vararg urlArgs: String
        ): WebTestClient.ResponseSpec =
            body?.let {
                webTestClient
                    .post()
                    .uri(endpoint, *urlArgs)
                    .body(Mono.just(body), body.javaClass)
                    .exchange()
                    .expectStatus().isForbidden
            } ?: webTestClient
                    .post()
                    .uri(endpoint, *urlArgs)
                    .exchange()
                    .expectStatus().isForbidden

        private fun userExecutesPatchOnForbiddenEndpoint(
            webTestClient: WebTestClient,
            endpoint: String,
            vararg urlArgs: String
        ): WebTestClient.ResponseSpec =
            webTestClient
                .patch()
                .uri(endpoint, *urlArgs)
                .exchange()
                .expectStatus().isForbidden
    }

}