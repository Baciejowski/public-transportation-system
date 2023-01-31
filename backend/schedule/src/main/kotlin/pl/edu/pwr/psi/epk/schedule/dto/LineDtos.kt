package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Line

data class LineManifestDTO(
    val id: Long,
    val name: String,
    val color: String?
) {
    companion object {
        fun fromLine(line: Line) = LineManifestDTO(line.id, line.name, line.color)
    }
}

data class LineDetailsDTO(
    val id: Long,
    val name: String,
    val color: String?,
    val routes: List<RouteManifestDTO>
) {
    companion object {
        fun fromLine(line: Line) =
            LineDetailsDTO(
                line.id,
                line.name,
                line.color,
                line.routes.map {RouteManifestDTO(it.id, it.name)}
            )
    }
}