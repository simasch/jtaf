package ch.jtaf.control.reporting.data

import ch.jtaf.entity.AthleteWithResultsDTO
import ch.jtaf.entity.Category
import java.util.*

class CompetitionRankingCategoryData(val category: Category, val athletes: List<AthleteWithResultsDTO>) {

    fun getAthletesSortedByPointsDesc(): List<AthleteWithResultsDTO> {
        return athletes.sortedByDescending { it.results.sumBy { it.points } }
    }
}