package com.object.chapter02.movie;

public interface DiscountPolicy {

    public Money calculateDiscountAmount(final Screening screening);
}
