package com.pgms.batchbooking.seat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.booking.repository.TicketRepository;
import com.pgms.coredomain.domain.event.EventSeatStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SeatReleaseTasklet implements Tasklet{

	private final TicketRepository ticketRepository;

	@Transactional
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		List<Ticket> tickets = ticketRepository.findAll(LocalDateTime.now(), BookingStatus.WAITING_FOR_PAYMENT);
		tickets.forEach(ticket -> ticket.getSeat().updateStatus(EventSeatStatus.AVAILABLE));
		return RepeatStatus.FINISHED;
	}
}
