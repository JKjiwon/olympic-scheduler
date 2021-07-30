package kr.olympicschedule.onair.service;

import kr.olympicschedule.onair.domain.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
class SeleniumServiceTest {

    SeleniumService seleniumService = new SeleniumService(LocalDate.now());

    @Test
    @DisplayName("크롤링 데이터 조회")
    void createCrawlingData() {

        List<Game> games = seleniumService.createCrawlingData();

        System.out.println("games.size() = " + games.size());
        for (Game game : games) {
            System.out.println(game);
        }
    }
}