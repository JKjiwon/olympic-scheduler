package kr.olympicschedule.onair.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameStatus {
    FINISHED("경기 종료"), PLAYING("경기 중"), NOT_YET("경기 예정");
    private final String description;
}
