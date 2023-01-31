package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Route

data class RouteManifestDTO(
    val id: Long,
    val name: String
) {
    companion object {
        fun fromRoute(route: Route) = RouteManifestDTO(route.id, route.name)
    }
}

data class RouteDetailDTO(
    val id: Long,
    val name: String,
    val stops: List<StopManifestDTO>
) {
    companion object {
        fun fromRoute(route: Route) = RouteDetailDTO(route.id, route.name, route.stops.map{StopManifestDTO.fromStop(it)})
    }
}