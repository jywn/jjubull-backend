package com.jjubull.resourceserver.ship.repository;

import com.jjubull.resourceserver.ship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipRepository extends JpaRepository<Ship, Long> {
}
