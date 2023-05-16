package com.object.chapter01.utils;

import com.object.chapter01.ticket.TicketSeller;

public class TestTicketSellerUtils {

    public static TicketSeller createMock() {
        return new TicketSeller(TestTicketOfficeUtils.createMock());
    }
}
