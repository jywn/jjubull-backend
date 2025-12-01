package com.jjubull.resourceserver.statistic.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "statistics")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statistic {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statistic_id")
    private Long id;

    @Column(name = "statistic_daily_visited")
    private int dailyVisited;

    @Column(name = "statistic_daily_reserved")
    private int dailyReserved;

    @Column(name = "statistic_daily_deposited")
    private int dailyDeposited;

    @Column(name = "statistic_daily_sales")
    private int dailySales;

    @Column(name = "statistic_deposit_expired")
    private int depositExpired;

    @Column(name = "statistic_deposit_24hour")
    private int deposit24Hour;

    @Column(name = "statistic_date")
    private LocalDate date;

    @Builder
    private Statistic(int dailyVisited, int dailyReserved, int dailyDeposited, int dailySales,
                      int depositExpired, int deposit24Hour, LocalDate date) {
        this.dailyVisited = dailyVisited;
        this.dailyReserved = dailyReserved;
        this.dailyDeposited = dailyDeposited;
        this.dailySales = dailySales;
        this.depositExpired = depositExpired;
        this.deposit24Hour = deposit24Hour;
        this.date = date;
    }

    public static Statistic create(int dailyVisited, int dailyReserved, int dailyDeposited, int dailySales,
                                   int depositExpired, int deposit24Hour, LocalDate date) {
        return Statistic.builder()
                .dailyVisited(dailyVisited)
                .dailyReserved(dailyReserved)
                .dailyDeposited(dailyDeposited)
                .dailySales(dailySales)
                .depositExpired(depositExpired)
                .deposit24Hour(deposit24Hour)
                .date(date).build();
    }

    public void addVisited() {
        this.dailyVisited++;
    }

    public void addReserved() {
        this.dailyReserved++;
    }

    public void addDeposited() {
        this.dailyDeposited++;
    }

    public void addSales(int sales) {
        this.dailySales += sales;
    }

    //입금 마감
    public void addDepositExpired() {
        this.depositExpired++;
    }

    //입금마감 24시간전
    public void addDeposit24Hour() {
        this.deposit24Hour++;
    }
}
