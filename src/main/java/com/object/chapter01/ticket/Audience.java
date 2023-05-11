package com.object.chapter01.ticket;

public class Audience {
    private Bag bag;

    public Audience(Bag bag) {
        this.bag = bag;
    }

    public boolean hasTicket() {
        return this.bag.hasTicket();
    }

    public Long buy(final Ticket ticket) {
        return this.bag.hold(ticket);
    }
}
