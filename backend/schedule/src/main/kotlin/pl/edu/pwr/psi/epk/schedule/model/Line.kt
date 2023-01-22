package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
class Line(
    val name: String,
    val color: String? = null,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "line")
    var routes: Set<Route> = mutableSetOf()

}
