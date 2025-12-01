package com.jjubull.resourceserver.reservation.repository;

import com.jjubull.resourceserver.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {
}
