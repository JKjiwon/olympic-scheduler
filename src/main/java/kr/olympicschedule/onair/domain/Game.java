package kr.olympicschedule.onair.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Game {
    private Long id; // 실제 DB key
    private LocalDateTime startTime; // 게임 시작 시간
    private String event; // 종목
    private String detail; // 종목 상세 정보
    private List<String> players;
    private GameStatus gameStatus; // 게임상태 : 경기 종료, 경기 중, 경기 예정
    private String videoLink; // 비디오 링크
    private boolean onAir; // 현재 생방송 송출 여부
}
