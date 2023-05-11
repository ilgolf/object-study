package com.object.chapter01.ticket;

public class Bag {
    private Long amount;
    private Invitation invitation;
    private Ticket ticket;

    // 초대장이 없고 현금만 보관할 수도 있다.
    public Bag(long amount) {
        this(null, amount);
    }

    public Bag(Invitation invitation, long amount) {
        this.invitation = invitation;
        this.amount = amount;
    }

    public Long hold(final Ticket ticket) {
        if (this.hasInvitation()) {
            this.addTicket(ticket);
            return 0L;
        }

        addTicket(ticket);
        minusAmount(ticket.getFee());
        return ticket.getFee();
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public boolean hasTicket() {
        return ticket != null;
    }

    public void addTicket(final Ticket ticket) {
        this.ticket = ticket;
    }

    public void minusAmount(final Long amount) {
        this.amount -= amount;
    }

    public void plusAmount(final Long amount) {
        this.amount += amount;
    }
}