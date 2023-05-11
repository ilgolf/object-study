package com.object.chapter01.ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketOffice {
    private Long amount;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketOffice(Long amount, Ticket... tickets) {
        this.amount = amount;
        this.tickets.addAll(Arrays.asList(tickets));
    }

    public void sellTicketTo(final Audience audience) {
        plusAmount(audience.buy(this.getTicket()));
    }

    public Ticket getTicket() {
        return tickets.remove(0);
    }

    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(final Long amount) {
        this.amount += amount;
    }
}
