package com.jjubull.resourceserver.ship.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ships")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ship {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ship_id")
    private Long id;

    @Column(name = "ship_max_head_count")
    private int maxHeadCount;

    @Column(name = "ship_fish_type")
    private String fishType;

    @Column(name = "ship_price")
    private int price;

    @Column(name = "ship_notification")
    private String notification;

    private Ship(int maxHeadCount, String fishType, int price, String notification) {
        this.maxHeadCount = maxHeadCount;
        this.fishType = fishType;
        this.price = price;
        this.notification = notification;
    }

    public static Ship create(int maxHeadCount, String fishType, int price, String notification) {
        return new Ship(maxHeadCount, fishType, price, notification);
    }

    public void updateMaxHeadCount(int maxHeadCount) {
        this.maxHeadCount = maxHeadCount;
    }

    public void updateFishType(String fishType) {
        this.fishType = fishType;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateNotification(String notification) {
        this.notification = notification;
    }
}
