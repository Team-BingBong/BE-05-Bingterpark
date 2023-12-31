package com.pgms.coredomain.domain.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankCode {
	경남("39"),
	광주("34"),
	단위농협("12"),
	부산("32"),
	새마을("45"),
	신한("88"),
	신협("48"),
	씨티("27"),
	우리("20"),
	우체국("71"),
	저축("50"),
	전북("37"),
	제주("35"),
	카카오("90"),
	토스("92"),
	하나("81"),
	기업("03"),
	국민("06"),
	농협("11"),
	수협("07");

	private final String bankNumCode;

	public static BankCode getByBankNumCode(String bankNumCode) {
		for (BankCode bankCode : values()) {
			if (bankCode.getBankNumCode().equals(bankNumCode)) {
				return bankCode;
			}
		}
		throw new IllegalArgumentException("No matching constant for bankNumCode: " + bankNumCode);
	}
}
