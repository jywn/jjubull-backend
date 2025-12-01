package com.jjubull.resourceserver.schedule.domain;

import com.jjubull.resourceserver.ship.domain.Ship;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @Column(name = "schedule_departure")
    private LocalDateTime departure;

    @Column(name = "schedule_current_head_count")
    private int currentHeadCount;

    @Column(name = "schedule_tide")
    private int tide;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    private Ship ship;

    @Builder
    private Schedule(LocalDateTime departure, int currentHeadCount, int tide, Status status, Type type, Ship ship) {
        this.departure = departure;
        this.currentHeadCount = currentHeadCount;
        this.tide = tide;
        this.status = status;
        this.type = type;
        this.ship = ship;
    }

    public static Schedule create(LocalDateTime departure, int currentHeadCount, int tide, Status status, Type type, Ship ship) {
        return Schedule.builder()
                .departure(departure)
                .currentHeadCount(currentHeadCount)
                .tide(tide)
                .status(status)
                .type(type)
                .ship(ship)
                .build();
    }
}
