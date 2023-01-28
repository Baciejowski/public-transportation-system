package pl.edu.pwr.psi.epk.schedule.dto

import pl.edu.pwr.psi.epk.schedule.model.Bus

class BusDTO(
    val sideNumber: Int,
    val isLowFloor: Boolean
) {
    companion object {
        fun fromBus(bus: Bus) = BusDTO(bus.sideNumber, bus.isLowFloor)
    }
}