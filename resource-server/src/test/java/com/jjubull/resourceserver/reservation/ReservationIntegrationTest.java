package com.jjubull.resourceserver.reservation;

import com.jjubull.common.domain.Grade;
import com.jjubull.common.domain.Provider;
import com.jjubull.common.domain.User;
import com.jjubull.resourceserver.reservation.repository.ReservationRepository;
import com.jjubull.resourceserver.reservation.service.ReservationCommandService;
import com.jjubull.resourceserver.reservation.service.ReservationQueryService;
import com.jjubull.resourceserver.schedule.domain.Schedule;
import com.jjubull.resourceserver.schedule.domain.Status;
import com.jjubull.resourceserver.schedule.domain.Type;
import com.jjubull.resourceserver.schedule.exception.NoPossibleSeatException;
import com.jjubull.resourceserver.schedule.repository.ScheduleRepository;
import com.jjubull.resourceserver.ship.domain.Ship;
import com.jjubull.resourceserver.ship.repository.ShipRepository;
import com.jjubull.resourceserver.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
public class ReservationIntegrationTest {

    @Container
    static MySQLContainer<?> mysql =
            new MySQLContainer<>("mysql:8.4")
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    ReservationCommandService reservationCommandService;
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("현재_17명이면_3명_예약_성공")
    void reservation_success() {

        //given
        Ship ship = Ship.create(20, "TestFish", 10000, "TestNotify");
        shipRepository.save(ship);

        Schedule schedule = Schedule.create(LocalDateTime.now(), 17, 0, Status.WAITING, Type.NORMAL, ship);
        scheduleRepository.save(schedule);

        User user = User.create("TestUser", "TestNick", Grade.BASIC, "TestPhone", "TestSub", Provider.LOCAL);
        userRepository.save(user);

        //when
        reservationCommandService.reserve(schedule.getId(), user.getId(), 3, "TestRequest");

        em.flush();
        em.clear();

        //then
        Assertions.assertThat(scheduleRepository.findById(schedule.getId()).get().getCurrentHeadCount()).isEqualTo(20);
    }

    @Test
    @DisplayName("현재_20명이면_예약_실패")
    void reservation_fail() {

        //given
        Ship ship = Ship.create(20, "TestFish", 10000, "TestNotify");
        shipRepository.save(ship);

        Schedule schedule = Schedule.create(LocalDateTime.now(), 20, 0, Status.WAITING, Type.NORMAL, ship);
        scheduleRepository.save(schedule);

        User user = User.create("TestUser", "TestNick", Grade.BASIC, "TestPhone", "TestSub", Provider.LOCAL);
        userRepository.save(user);

        //when & then
        assertThrows(
                NoPossibleSeatException.class,
                () -> reservationCommandService.reserve(schedule.getId(), user.getId(), 3, "테스트")
        );
    }
}
