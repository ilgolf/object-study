package com.object.chapter02.movie;

public interface DiscountCondition {

    boolean isSatisfiedBy(final Screening screening);
}
