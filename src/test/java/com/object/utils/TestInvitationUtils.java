package com.object.utils;

import com.object.chapter01.ticket.Invitation;

import java.time.LocalDateTime;

public class TestInvitationUtils {

    public static final LocalDateTime when = LocalDateTime.now();

    public static Invitation createMock() {
        return new Invitation(when);
    }
}
