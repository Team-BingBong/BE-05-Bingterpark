package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BankCode {
	경남("경남은행"),
	광주("광주은행"),
	단위농협("단위농협(지역농축협)"),
	부산("부산은행"),
	새마을("새마을금고"),
	신한("신한은행"),
	신협("신협"),
	씨티("씨티은행"),
	우리("우리은행"),
	우체국("우체국예금보함"),
	저축("저축은행중앙회"),
	전북("전북은행"),
	제주("제주은행"),
	카카오("카카오뱅크"),
	토스("토스뱅크"),
	하나("하나은행"),
	기업("IBK기업은행"),
	국민("KB국민은행"),
	농협("NH농협은행");

	private final String bankName;
}
