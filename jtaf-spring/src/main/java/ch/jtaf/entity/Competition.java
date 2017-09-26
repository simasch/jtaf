package ch.jtaf.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "competition")
@NamedQueries({
    @NamedQuery(name = "Competition.findAll", query = "select c from Competition c where c.series_id = :series_id order by c.competitionDate")
})
public class Competition implements Comparable<Competition> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(name = "competitiondate")
    private Date competitionDate;
    @Column(name = "medalpercentage")
    private Integer medalPercentage;
    @Column(name = "alwaysthreemedals")
    private boolean alwaysThreeMedals;
    private boolean locked;
    private Long series_id;
    @Transient
    private int numberOfAthletesWithResults;
    @Transient
    private int numberOfAthletes;

    public Date getCompetitionDate() {
        return competitionDate;
    }

    public void setCompetitionDate(Date competitionDate) {
        this.competitionDate = competitionDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSeries_id() {
        return series_id;
    }

    public void setSeries_id(Long series_id) {
        this.series_id = series_id;
    }

    public int getNumberOfAthletes() {
        return numberOfAthletes;
    }

    public void setNumberOfAthletes(int numberOfAthletes) {
        this.numberOfAthletes = numberOfAthletes;
    }

    public int getNumberOfAthletesWithResults() {
        return numberOfAthletesWithResults;
    }

    public void setNumberOfAthletesWithResults(int numberOfAthletesWithResults) {
        this.numberOfAthletesWithResults = numberOfAthletesWithResults;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Integer getMedalPercentage() {
        return medalPercentage;
    }

    public void setMedalPercentage(Integer medalPercentage) {
        this.medalPercentage = medalPercentage;
    }

    public boolean isAlwaysThreeMedals() {
        return alwaysThreeMedals;
    }

    public void setAlwaysThreeMedals(boolean alwaysThreeMedals) {
        this.alwaysThreeMedals = alwaysThreeMedals;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Competition other = (Competition) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Competition{" + "id=" + id + ", name=" + name + ", competitionDate=" + competitionDate + ", series_id=" + series_id + ", numberOfAthletes=" + numberOfAthletes + '}';
    }

    @Override
    public int compareTo(Competition o) {
        return o.getCompetitionDate().compareTo(competitionDate);
    }
}