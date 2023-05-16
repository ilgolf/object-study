package com.object.chapter02.movie;

import com.object.chapter02.utils.TestMovieUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class MovieTest {

    private final Money discountAmount = Money.wons(800);
    private final double discountPercent = 0.1;

    @Test
    @DisplayName("횟수 할인 정책과 기간 할인 정책 그리고 할인 시 금액 할인이된다.")
    void test1() {
        // given
        Movie avatar = TestMovieUtils.getAvatar(discountAmount);
        LocalDate date = LocalDate.of(2023, 5, 15);
        LocalTime time = LocalTime.of(10, 5);
        Screening screening = new Screening(avatar, 1, LocalDateTime.of(date, time));

        // when
        Reservation reserve = screening.reserve(new Customer("노경태", "ilgolf"), 1);

        // then
        assertThat(reserve.getFee()).isEqualTo(Money.wons(9200));
    }

    @Test
    @DisplayName("비율 할인 정책")
    void test2() {
        // given
        Movie titanic = TestMovieUtils.getTitanic(discountPercent);
        LocalDate date = LocalDate.of(2023, 5, 16);
        LocalTime time = LocalTime.of(14, 10);
        Screening screening = new Screening(titanic, 3, LocalDateTime.of(date, time));

        // when
        Reservation reserve = screening.reserve(new Customer("노경태", "ilgolf"), 1);

        // then
        assertThat(reserve.getFee()).isEqualTo(Money.wons(9900));
    }

    @Test
    @DisplayName("할인 정책이 없는 경우 그대로를 반환한다.")
    void test3() {
        // given
        Movie starWars = TestMovieUtils.getStarWars();
        LocalDate date = LocalDate.of(2023, 5, 16);
        LocalTime time = LocalTime.of(14, 10);
        Screening screening = new Screening(starWars, 3, LocalDateTime.of(date, time));

        // when
        Reservation reserve = screening.reserve(new Customer("노경태", "ilgolf"), 1);

        // then
        assertThat(reserve.getFee()).isEqualTo(starWars.getFee());
    }
}
