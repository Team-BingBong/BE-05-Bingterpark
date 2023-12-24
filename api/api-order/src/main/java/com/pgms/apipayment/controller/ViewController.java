package com.pgms.apipayment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/payments")
	public String PaymentRequestView() {
		return "payments";
	}
}
