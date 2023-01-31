package pl.edu.pwr.psi.epk.integration.dto.schedule

class RouteManifestDto(
    val id: Long,
    val name: String
)

class RouteDetailDto(
    val id: Long,
    val name: String,
    val stops: List<StopManifestDto>
)
