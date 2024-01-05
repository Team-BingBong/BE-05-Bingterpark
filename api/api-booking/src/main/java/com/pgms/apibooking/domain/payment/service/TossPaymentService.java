package com.pgms.apibooking.domain.payment.service;

import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentSuccessResponse;

public interface TossPaymentService {

	PaymentSuccessResponse requestTossPaymentConfirmation(PaymentConfirmRequest request);

	PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, PaymentCancelRequest request);
}
