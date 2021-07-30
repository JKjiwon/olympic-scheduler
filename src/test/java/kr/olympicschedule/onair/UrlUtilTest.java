package kr.olympicschedule.onair;

import kr.olympicschedule.onair.utill.UrlUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class UrlUtilTest {
    @Test
    void createUrl() {
        String url = UrlUtil.createUrl(LocalDate.now());
        System.out.println("url = " + url);
    }
}