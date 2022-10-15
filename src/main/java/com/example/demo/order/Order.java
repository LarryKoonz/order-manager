package com.example.demo.order;

import javax.persistence.*;
import java.time.*;

@Entity
@Table
public class Order {
    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    private long id;
    private String name;
    private ZonedDateTime timeStamp;

    public Order(String name) {
        this.name = name;
    }

    public Order() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimeStamp(ZonedDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean wasOrderedBeforeSpecifiedPeriod(int years, int months, int days, long seconds){
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        Period period = Period.between(currentTime.toLocalDate(), this.timeStamp.toLocalDate());
        Duration duration = Duration.between(currentTime.toLocalTime(), this.timeStamp.toLocalTime());
        return period.getYears() <= years && period.getMonths() <= months && period.getDays() <= days
                && duration.getSeconds() <= seconds;
    }

}
