package com.example.demo.order;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private ZonedDateTime timeStamp;
    private LocalDateTime localDateTime; // TODO remove after testing

//    public Order(long id, String name, ZonedDateTime timeStamp){
//        this.id = id;
//        this.name = name;
//        this.timeStamp = timeStamp;
//    }

    public Order(long id, String name, LocalDateTime localDateTime){ // TODO remove after testing
        this.id = id;
        this.name = name;
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() { // TODO remove after testing
        return localDateTime;
    }

    public Order(String name) {
        this.name = name;
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

    public ZonedDateTime getTimeStamp(){
        return this.timeStamp;
    }

    public boolean wasOrderedBeforeSpecifiedPeriod(int years, int months, int days, long seconds){
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Paris"));
        Period period = Period.between(currentTime.toLocalDate(), this.timeStamp.toLocalDate());
        Duration duration = Duration.between(currentTime.toLocalTime(), this.timeStamp.toLocalTime());
        return period.getYears() <= years && period.getMonths() <= months && period.getDays() <= days
                && duration.getSeconds() <= seconds;
    }

}
