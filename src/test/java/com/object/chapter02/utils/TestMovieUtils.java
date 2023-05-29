package com.object.chapter02.utils;

import com.object.chapter02.movie.DiscountPolicy;
import com.object.chapter02.movie.Money;
import com.object.chapter02.movie.Movie;
import com.object.chapter02.pricing.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

public class TestMovieUtils {

    public static Movie getAvatar(final Money amount) {
        return new Movie("아바타",
                Duration.ofMinutes(120),
                Money.wons(10000),
                getAvatarCondition(amount)
        );
    }

    private static DiscountPolicy getAvatarCondition(final Money amount) {
        return new AmountDiscountPolicy(amount,
                new SequenceCondition(1),
                new SequenceCondition(10),
                new PeriodCondition(DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 59)),
                new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(20, 59)));
    }

    public static Movie getTitanic(final double percent) {
        return new Movie(
                "타이타닉",
                Duration.ofMinutes(180),
                Money.wons(11000),
                getTitanicCondition(percent)
        );
    }

    private static DiscountPolicy getTitanicCondition(final double percent) {
        return new PercentDiscountPolicy(
                percent,
                new PeriodCondition(DayOfWeek.TUESDAY, LocalTime.of(14, 0), LocalTime.of(16, 59)),
                new SequenceCondition(2),
                new PeriodCondition(DayOfWeek.THURSDAY, LocalTime.of(10, 0), LocalTime.of(13, 59)));
    }

    public static Movie getStarWars() {
        return new Movie("스타워즈",
                Duration.ofMinutes(210),
                Money.wons(10000),
                new NoneDiscountPolicy());
    }
}
