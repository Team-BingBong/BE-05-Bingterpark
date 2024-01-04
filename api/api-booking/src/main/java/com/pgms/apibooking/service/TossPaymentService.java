package com.pgms.apibooking.service;

import com.pgms.apibooking.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;

public interface TossPaymentService {

	PaymentSuccessResponse requestTossPaymentConfirmation(PaymentConfirmRequest request);

	PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, PaymentCancelRequest request);
}
