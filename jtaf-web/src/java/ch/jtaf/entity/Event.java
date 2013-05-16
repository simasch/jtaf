package ch.jtaf.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "select e from Event e order by e.name"),
    @NamedQuery(name = "Event.findBySeries", query = "select e from Event e where e.series = :series order by e.name")
})
public class Event {
    public static final String JUMP_THROW = "jump_throw";
    public static final String RUN = "run";
    public static final String RUN_LONG = "run_long";

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private String gender;
    private double a;
    private double b;
    private double c;
    @ManyToOne
    private Series series;

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }
}
