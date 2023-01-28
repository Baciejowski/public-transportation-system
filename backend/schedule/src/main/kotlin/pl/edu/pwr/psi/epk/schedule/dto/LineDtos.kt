package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Line

class LineDTO(
    val id: Long,
    val name: String,
    val color: String?
) {
    companion object {
        fun fromLine(line: Line) = LineDTO(line.id, line.name, line.color)
    }
}