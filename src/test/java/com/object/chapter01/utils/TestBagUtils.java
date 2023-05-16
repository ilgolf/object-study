package com.object.chapter01.utils;

import com.object.chapter01.ticket.Bag;

public class TestBagUtils {

    public static final Long amount = 10000L;

    public static Bag createMock() {
        return new Bag(TestInvitationUtils.createMock(), amount);
    }
}
