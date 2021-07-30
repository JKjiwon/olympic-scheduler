package kr.olympicschedule.onair.utill;

import java.time.LocalDate;

public class UrlUtil {
    public static String createUrl(LocalDate currentDate) {
        return "https://m.sports.naver.com/tokyo2020/schedule/index?type=date&date="
                + currentDate.toString()
                + "&disciplineId=&isKorean=Y";
    }
}
