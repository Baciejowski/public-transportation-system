package pl.edu.pwr.psi.epk.gateway.config

import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.core.annotation.MergedAnnotation
import org.springframework.core.annotation.MergedAnnotations
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.server.ResponseStatusException

class CustomErrorAttributes: DefaultErrorAttributes() {
    @Override
    override fun getErrorAttributes(request: ServerRequest, options: ErrorAttributeOptions): Map<String, Any> {
        val attributes = super.getErrorAttributes(request, options);
        val error: Throwable = getError(request);
        val responseStatusAnnotation: MergedAnnotation<ResponseStatus> = MergedAnnotations
            .from(error.javaClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY).get(ResponseStatus::class.java)
        val errorStatus: HttpStatus = determineHttpStatus(error, responseStatusAnnotation)
        attributes.put("status", errorStatus.value());
        return attributes;
    }

    private fun determineHttpStatus(error: Throwable, responseStatusAnnotation: MergedAnnotation<ResponseStatus>): HttpStatus {
        if (error is ResponseStatusException) {
            return HttpStatus.resolve(error.statusCode.value())!!
        }
        return responseStatusAnnotation.getValue("code", HttpStatus::class.java).orElseGet {
            if (error is java.net.ConnectException || error is java.net.UnknownHostException)
                HttpStatus.SERVICE_UNAVAILABLE;
            else
                HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
