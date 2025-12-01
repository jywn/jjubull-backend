package com.jjubull.resourceserver.statistic.repository;

import com.jjubull.resourceserver.statistic.domain.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface StatisticRepository extends JpaRepository<Statistic, Long>, StatisticRepositoryCustom {
    Statistic findByDate(LocalDate date);
}
