package kr.olympicschedule.onair.service;

import kr.olympicschedule.onair.domain.Game;
import kr.olympicschedule.onair.domain.GameStatus;
import kr.olympicschedule.onair.utill.HtmlClass;
import kr.olympicschedule.onair.utill.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeleniumService {

    private WebDriver driver;
    private final LocalDate date;

    public SeleniumService(LocalDate date) {
        this.date = date;
        System.setProperty("webdriver.chrome.driver", "src/test/driver/chromedriver");
        driver = new ChromeDriver();
        driver.get(UrlUtil.createUrl(date));
    }

    public List<Game> createCrawlingData() {
        // Issue : 자바스크립트가 화면에 데이터를 그릴때까지 대기 -> 완료 되었을 때 크롤링하는 다른 함수 있지 않을까??

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            List<Game> games = new ArrayList<>();
            List<WebElement> GameElements = driver.findElements(By.className(HtmlClass.GAMES));
            for (WebElement gameElement : GameElements) {
                String[] title = getTitle(gameElement);
                String event = title[0];
                String detail = title[1];
                LocalDateTime startTime = getStartTime(gameElement);
                List<String> players = getPlayers(gameElement);
                GameStatus gameStatus = getGameStatus(gameElement);
                String videoLink = "";
                boolean onAir = false;

                if (!gameStatus.equals(GameStatus.YET)) {
                    if (gameStatus.equals(GameStatus.PLAYING)) {
                        onAir = true;
                    }
                    videoLink = getVideoLink(gameElement);
                }

                Game game = Game.builder()
                        .event(event)
                        .detail(detail)
                        .startTime(startTime)
                        .players(players)
                        .gameStatus(gameStatus)
                        .videoLink(videoLink)
                        .onAir(onAir)
                        .build();

                games.add(game);
            }
            return games;

        } catch (Exception e) {

        } finally {
            driver.quit();
        }
        return null;
    }

    private String getVideoLink(WebElement gameElement) {
        return gameElement.findElement(By.className(HtmlClass.GAME_VIDEO)).getAttribute("href");
    }

    private GameStatus getGameStatus(WebElement gameElement) {
        GameStatus gameStatus;
        String status = gameElement.findElement(By.className(HtmlClass.GAME_STATUS)).getText();
        if (status.equals("경기종료")) {
            gameStatus = GameStatus.FINISHED;
        } else if (status.equals("경기 예정")) {
            gameStatus = GameStatus.YET;
        } else {
            gameStatus = GameStatus.PLAYING;
        }
        return gameStatus;
    }

    private List<String> getPlayers(WebElement gameElement) {
        return gameElement.findElements(By.className(HtmlClass.GAME_PLAYERS))
                .stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private LocalDateTime getStartTime(WebElement gameElement) {
        String time = gameElement.findElement(By.className(HtmlClass.GAME_TIME)).getText();
        int colonIndex = time.indexOf(":");
        int hour = Integer.parseInt(time.substring(0, colonIndex));
        int minute = Integer.parseInt(time.substring(colonIndex+1));
        return date.atTime(hour, minute);
    }

    private String[] getTitle(WebElement gameElement) {
        String title = gameElement.findElement(By.className(HtmlClass.GAME_TITLE)).getText();
        int spaceIndex = title.indexOf(" ");
        return new String[]{title.substring(0, spaceIndex), title.substring(spaceIndex+1)};
    }

    @PreDestroy
    public void close() {

    }
}
