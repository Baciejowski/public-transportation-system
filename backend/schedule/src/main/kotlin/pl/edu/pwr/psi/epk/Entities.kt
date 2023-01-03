package pl.edu.pwr.psi.epk

import jakarta.persistence.*

@Embeddable
class Polozenie(
    var szerokosc: Double,
    var dlugosc: Double
)


@Entity
@Table(name = "Przystanki")
class Przystanek(
    @Column(length = 20)
    var nazwa: String,
    @Embedded
    var polozenie: Polozenie,
    var jestNaZadanie: Boolean = false,
    @ManyToMany(mappedBy = "przystanki")
    var trasy: Set<Trasa> = mutableSetOf(),
    @Id
    @GeneratedValue
    var id: Int? = null
)


@Entity
@Table(name = "Trasy")
class Trasa(
    @Column(length = 30)
    var nazwa: String,
    @ManyToOne
    @JoinColumn(name="linie_linia", nullable=false)
    var linia: Linia,
    @ManyToMany
    @JoinTable(
        name = "Przystanki_Trasy",
        joinColumns = [JoinColumn(name = "trasy_id")],
        inverseJoinColumns = [JoinColumn(name = "przystanki_id")]
    )
    var przystanki: Set<Przystanek> = mutableSetOf(),
    @Id
    @GeneratedValue
    var id: Int? = null
)


@Entity
@Table(name = "Linie")
class Linia(
    @Id
    @Column(length = 3)
    var linia: String,
    @Column(length = 30)
    var nazwa: String,
    @Column(length = 7)
    var kolor: String?,
    @OneToMany(mappedBy = "linia")
    var trasy: Set<Trasa> = mutableSetOf()
)
