package com.object.utils;

import com.object.chapter01.ticket.Audience;

public class TestAudienceUtils {

    public static Audience createMock() {
        return new Audience(TestBagUtils.createMock());
    }
}
