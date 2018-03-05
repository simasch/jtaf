package ch.jtaf.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
data class Result(
        @Id @GeneratedValue(strategy = IDENTITY)
        var id: Long? = null,

        var result: String = "",
        var points: Int = 0,

        @ManyToOne
        var athlete: Athlete? = null,

        @ManyToOne
        var event: Event? = null,

        @ManyToOne
        var competition: Competition? = null,

        var position: Int = 0
)