package com.pgms.apibooking.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

	@GetMapping("/payments")
	public String paymentRequestView(
		@RequestParam String orderId,
		@RequestParam String orderName,
		@RequestParam String buyerPhoneNumber,
		@RequestParam String buyerName,
		@RequestParam int amount,
		Model model) {
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderName", orderName);
		model.addAttribute("buyerPhoneNumber", buyerPhoneNumber);
		model.addAttribute("buyerName", buyerName);
		model.addAttribute("amount", amount);

		return "payments";
	}

	@GetMapping("/bookings")
	public String bookingView() {
		return "booking_input";
	}
}
