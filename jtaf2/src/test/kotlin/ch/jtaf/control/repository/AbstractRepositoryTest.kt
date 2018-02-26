package ch.jtaf.control.repository

import ch.jtaf.entity.*
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

abstract class AbstractRepositoryTest {

    val email = "john.doe@jtaf.ch"
    var seriesId = 0L

    @Autowired
    lateinit var em: EntityManager

    @Before
    fun createTestData() {
        val securityGroup = SecurityGroup(name = "ADMIN")
        em.persist(securityGroup)

        val securityUser = SecurityUser(email = email)
        securityUser.groups.add(securityGroup)
        em.persist(securityUser)

        val series = Series(name = "CIS 2018", owner = email)
        em.persist(series)
        seriesId = series.id!!

        val club = Club(abbreviation = "TVT", name = "TV Twann", owner = email)
        em.persist(club)

        val athlete = Athlete(lastName = "Meier", firstName = "Max", yearOfBirth = 2004, club = club, owner = email)
        em.persist(athlete)

        val event = Event(abbreviation = "80", name = "80 m", owner = email)
        em.persist(event)

        val competition = Competition(name = "1. CIS Twann", seriesId = series.id)
        em.persist(competition)
        series.competitions.add(competition)

        val category = Category(abbreviation = "A", name = "Boys", yearFrom = 2000, yearTo = 2004, seriesId = series.id)
        em.persist(category)

        category.events.add(event)
        category.athletes.add(athlete)
    }
}