package com.object.chapter02.pricing;

import com.object.chapter02.movie.DefaultDiscountPolicy;
import com.object.chapter02.movie.DiscountPolicy;
import com.object.chapter02.movie.Money;
import com.object.chapter02.movie.Screening;

public class NoneDiscountPolicy extends DefaultDiscountPolicy {

    @Override
    protected Money getDiscountAmount(Screening screening) {
        return Money.ZERO;
    }
}
