package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.event.EventSeat;

public class TicketFactory {

	public static Ticket generate(EventSeat seat) {
		return Ticket.builder()
			.seat(seat)
			.build();
	}
}
