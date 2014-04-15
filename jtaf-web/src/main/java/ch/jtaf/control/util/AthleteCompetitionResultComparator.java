package ch.jtaf.control.util;

import ch.jtaf.entity.Athlete;
import ch.jtaf.entity.Competition;
import java.util.Comparator;

public class AthleteCompetitionResultComparator implements Comparator<Athlete> {

    private Competition competition;
    
    public AthleteCompetitionResultComparator(Competition competition) {
        this.competition = competition;
    }
    
    @Override
    public int compare(Athlete o1, Athlete o2) {
        return Integer.valueOf(o2.getTotalPoints(competition)).compareTo(
                Integer.valueOf(o1.getTotalPoints(competition)));
    }
}