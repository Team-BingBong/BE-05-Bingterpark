package com.pgms.apibooking.service;

import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;

public interface TossPaymentService {

	PaymentSuccessResponse requestTossPaymentConfirmation(PaymentConfirmRequest request);

	PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, BookingCancelRequest request);
}
