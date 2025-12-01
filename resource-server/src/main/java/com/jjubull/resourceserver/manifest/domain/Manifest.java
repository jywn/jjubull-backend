package com.jjubull.resourceserver.manifest.domain;

import com.jjubull.resourceserver.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manifests")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manifest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manifest_id")
    private Long id;

    @Column(name = "manifest_username")
    private String username;

    @Column(name = "manifest_nickname")
    private String nickname;

    @Column(name = "manifest_birth_date")
    private LocalDate birthDate;

    public enum Sex {
        MALE, FEMALE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "manifest_sex")
    private Sex sex;

    @Column(name = "manifest_address")
    private String address;

    @Column(name = "manifest_phone")
    private String phone;

    @Column(name = "manifest_emergency_contact")
    private String emergencyContact;

    @Column(name = "manifest_expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scehdule_id")
    private Schedule schedule;

    @Builder
    private Manifest(String username, String nickname, LocalDate birthDate, Sex sex, String address, String phone, String emergencyContact, LocalDateTime expiresAt, Schedule schedule) {
        this.username = username;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.expiresAt = expiresAt;
        this.schedule = schedule;
    }

    public static Manifest create(String username, String nickname, LocalDate birthDate, Sex sex, String address, String phone, String emergencyContact, Schedule schedule) {
        return Manifest.builder()
                .username(username)
                .nickname(nickname)
                .birthDate(birthDate)
                .sex(sex)
                .address(address)
                .phone(phone)
                .emergencyContact(emergencyContact)
                .expiresAt(LocalDateTime.now().plusDays(30))
                .schedule(schedule)
                .build();
    }

    public void change(String username, String nickname, LocalDate birthDate, Sex sex, String address, String phone, String emergencyContact) {
        this.username = username;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.sex = sex;
        this.address = address;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
    }
}
