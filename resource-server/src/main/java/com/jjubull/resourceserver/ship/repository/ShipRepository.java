package com.jjubull.resourceserver.ship.repository;

import com.jjubull.resourceserver.ship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipRepository extends JpaRepository<Ship, Long> {
}
