package kr.olympicschedule.onair.service;

import kr.olympicschedule.onair.domain.Game;
import kr.olympicschedule.onair.domain.GameStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GameServiceTest {
    private static Long id = 0L;
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/driver/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

    @Test
    void SeleniumTest() throws IOException {
        int year = 2021;
        int month = 7;
        int day = 30;
        int hour = 0;
        int minute = 0;

        String crawlingURL = "https://m.sports.naver.com/tokyo2020/schedule/index?type=date&date=2021-07-30&disciplineId=&isKorean=Y";
        driver.get(crawlingURL);

        List<Game> games = new ArrayList<>();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        List<WebElement> weGames = driver.findElements(By.className("GameScheduleList_game_item__2ricE"));

        System.out.println("size: " + games.size());

        for (WebElement weGame : weGames) {
            String time = weGame.findElement(By.className("GameScheduleList_game_time__10dNy")).getText();
            String[] split = time.split(":");
            hour = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);

            LocalDateTime startTime = LocalDateTime.of(year, month, day, hour, minute);

            String title = weGame.findElement(By.className("GameScheduleList_title__2fTpK")).getText();
            String[] splitTitle = titleSplit(title);
            String event = splitTitle[0];
            String detail = splitTitle[1];

            List<String> players = weGame.findElements(By.className("GameScheduleList_link_name__xBm7W"))
                    .stream().map(WebElement::getText).collect(Collectors.toList());

            GameStatus status;
            boolean onAir = false;
            String url = "";
            try {
                WebElement we = weGame.findElement(By.className("GameScheduleList_status_badge__2lkEd"));
                onAir = we.getText().equals("LIVE");

                if (onAir) {
                    url = we.getAttribute("href");
                }

            } catch (Exception e) { }

            if (onAir) {
                status = GameStatus.PLAYING;
            }

            Game ga = Game.builder()
                    .id(++id)
                    .player(players)
                    .startTime(startTime)
                    .event(event)
                    .detail(detail)
                    .onAir(onAir)
                    .link(url)
                    .build();

            games.add(ga);

            System.out.println(ga);
            System.out.println();
        }
    }

    private String[] titleSplit(String str) {
        int spaceIdx = str.indexOf(" ");
        return new String[]{str.substring(0, spaceIdx), str.substring(spaceIdx + 1)};
    }
}