package pl.edu.pwr.psi.epk.schedule.model

import jakarta.persistence.*

@Entity
@Table(name = "Lines")
class Line(

    @Id
    @Column(length = 3)
    var line: String,

    @Column(length = 30)
    var name: String,

    @Column(length = 7)
    var color: String?,

    @OneToMany(mappedBy = "line")
    var routes: Set<Route> = mutableSetOf(),

)
