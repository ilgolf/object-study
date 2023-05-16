package com.object.chapter02.pricing;

import com.object.chapter02.movie.DiscountCondition;
import com.object.chapter02.movie.Screening;

public class SequenceCondition implements DiscountCondition {

    private int sequence;

    public SequenceCondition(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public boolean isSatisfiedBy(Screening screening) {
        return screening.isSequence(sequence);
    }
}
