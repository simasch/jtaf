package ch.jtaf.control.repository

import ch.jtaf.entity.Club
import org.springframework.data.jpa.repository.JpaRepository

interface ClubRepository : JpaRepository<Club, Long> {

    fun findByOrganizationId(organizationId: Long): List<Club>
}
