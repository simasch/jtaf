package ch.jtaf.control;

import ch.jtaf.control.util.AthleteCompetitionResultComparator;
import ch.jtaf.control.util.AthleteSeriesResultComparator;
import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Category;
import ch.jtaf.entity.Competition;
import ch.jtaf.entity.CompetitionRankingData;
import ch.jtaf.entity.CompetitionRankingCategoryData;
import ch.jtaf.entity.Result;
import ch.jtaf.entity.Series;
import ch.jtaf.entity.SeriesRankingCategoryData;
import ch.jtaf.entity.SeriesRankingData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class RankingService extends AbstractService {

    public CompetitionRankingData getCompetitionRanking(Long competitionid) {
        Competition competition = em.find(Competition.class, competitionid);
        if (competition == null) {
            return null;
        }
        competition.setSeries(null);

        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findByCompetition", Athlete.class);
        q.setParameter("competitionid", competitionid);
        List<Athlete> list = q.getResultList();

        CompetitionRankingData ranking = new CompetitionRankingData();
        ranking.setCompetition(competition);

        Map<Category, List<Athlete>> map = new HashMap<Category, List<Athlete>>();
        for (Athlete a : list) {
            List<Athlete> as = map.get(a.getCategory());
            if (as == null) {
                as = new ArrayList<Athlete>();
            }
            as.add(a);
            map.put(a.getCategory(), as);
        }
        for (Map.Entry<Category, List<Athlete>> entry : map.entrySet()) {
            CompetitionRankingCategoryData rc = new CompetitionRankingCategoryData();
            Category c = entry.getKey();
            c.setEvents(null);
            c.setSeries(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(competition, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        return ranking;
    }

    public SeriesRankingData getSeriesRanking(Long seriesId) {
        Series series = em.find(Series.class, seriesId);
        if (series == null) {
            return null;
        }
        TypedQuery<Athlete> q = em.createNamedQuery("Athlete.findBySeries", Athlete.class);
        q.setParameter("seriesid", seriesId);
        List<Athlete> list = q.getResultList();
        
        TypedQuery<Competition> qc = em.createNamedQuery("Competition.findBySeries", Competition.class);
        qc.setParameter("series", series);
        List<Competition> cs = qc.getResultList();
        series.setCompetitions(cs);

        SeriesRankingData ranking = new SeriesRankingData();
        ranking.setSeries(series);

        Map<Category, List<Athlete>> map = new HashMap<Category, List<Athlete>>();
        for (Athlete a : list) {
            List<Athlete> as = map.get(a.getCategory());
            if (as == null) {
                as = new ArrayList<Athlete>();
            }
            as.add(a);
            map.put(a.getCategory(), as);
        }
        for (Map.Entry<Category, List<Athlete>> entry : map.entrySet()) {
            SeriesRankingCategoryData rc = new SeriesRankingCategoryData();
            Category c = entry.getKey();
            c.setEvents(null);
            c.setSeries(null);
            rc.setCategory(c);
            rc.setAthletes(filterAndSort(series, entry.getValue()));
            ranking.getCategories().add(rc);
        }
        return ranking;
    }

    private List<Athlete> filterAndSort(Competition competition, List<Athlete> list) {
        for (Athlete a : list) {
            a.setCategory(null);
            a.setSeries(null);
            List<Result> rs = new ArrayList<Result>();
            for (Result r : a.getResults()) {
                if (r.getCompetition().equals(competition)) {
                    r.getEvent().setSeries(null);
                    rs.add(r);
                }
            }
            a.setResults(rs);
        }
        Collections.sort(list, new AthleteCompetitionResultComparator(competition));
        return list;
    }

    private List<Athlete> filterAndSort(Series series, List<Athlete> list) {
        for (Athlete a : list) {
            a.setCategory(null);
            a.setSeries(null);
            List<Result> rs = new ArrayList<Result>();
            for (Result r : a.getResults()) {
                if (r.getCompetition().getSeries().equals(series)) {
                    r.getEvent().setSeries(null);
                    rs.add(r);
                }
            }
            a.setResults(rs);
        }
        Collections.sort(list, new AthleteSeriesResultComparator(series));
        return list;
    }
}