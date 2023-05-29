package com.object.chapter01.utils;

import com.object.chapter01.ticket.Ticket;
import com.object.chapter01.ticket.TicketOffice;

public class TestTicketOfficeUtils {

    public static TicketOffice createMock() {
        Ticket ticket = new Ticket(1000L);

        return new TicketOffice(10000L, ticket, ticket, ticket, ticket, ticket);
    }
}
