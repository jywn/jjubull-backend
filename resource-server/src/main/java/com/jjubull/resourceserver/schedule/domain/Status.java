package com.jjubull.resourceserver.schedule.domain;

public enum Status {
    WAITING("출항 대기 중입니다."), // 출항 전
    CONFIRMED("안녕하세요, 쭈불입니다. \n" +
            "내일은 기상예보가 갱신되어 좋아졌기에 출항을 확정합니다. 승선명부를 회신해주세요."), // 출항 확정
    DELAYED("안녕하세요, 쭈불입니다. \n" +
            "내일은 기상악화 예보가 있어서 대기 후 기상예보가 갱신되면 출항여부를 안내드리겠습니다."), // 출항 보류
    CANCELED("안녕하세요, 쭈불입니다. \n" +
            "내일은 아쉽게도 기상악화로 인해 출항이 취소되었습니다. 환불계좌를 회신해주세요. 다음에 더 좋은날 뵙겠습니다.\n"); // 출항 취소

    private final String message;

    Status(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
