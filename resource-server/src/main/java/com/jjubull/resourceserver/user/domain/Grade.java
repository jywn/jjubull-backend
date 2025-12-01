package com.jjubull.resourceserver.user.domain;

public enum Grade {
    GUEST("GUEST"),
    BASIC("BASIC"),
    VIP("VIP"),
    ADMIN("ADMIN");

    private final String value;

    Grade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Grade from(String value) {
        for (Grade grade : Grade.values()) {
            if (grade.value.equalsIgnoreCase(value)) {
                return grade;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + value);
    }
}
