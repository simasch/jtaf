package ch.jtaf.control.repository

import ch.jtaf.entity.Result
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ResultRepository : JpaRepository<Result, Long> {

    fun findByAthleteIdAndCompetitionIdOrderByPosition(athleteId: Long, competitionId: Long): MutableList<Result>

    fun findByCompetitionIdOrderByPosition(competitionId: Long): List<Result>

    fun findByCompetitionSeriesId(seriesId: Long): List<Result>

    @Modifying
    @Query("delete from Result r where r.athlete.id = :athleteId ")
    fun deleteResultsFromActiveCompetitions(athleteId: Long)

    @Modifying
    @Query("delete from Result r where r.category.id = :categoryId and r.athlete.id = :athleteId")
    fun deleteResultsByCategoryIdAndAthleteId(categoryId: Long, athleteId: Long)
}
