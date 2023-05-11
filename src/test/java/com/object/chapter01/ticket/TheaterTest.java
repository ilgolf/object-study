package com.object.chapter01.ticket;

import com.object.utils.TestAudienceUtils;
import com.object.utils.TestTicketSellerUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TheaterTest {

    @Test
    @DisplayName("티켓을 부여 받고 입장할 수 있다.")
    void enter() {
        // given
        Audience audience = TestAudienceUtils.createMock();
        TicketSeller seller = TestTicketSellerUtils.createMock();
        Theater theater = new Theater(seller);

        // when
        theater.enter(audience);

        // then
        assertThat(audience.hasTicket()).isTrue();
    }
}